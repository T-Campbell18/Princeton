/*--------------------------------------------------------------------*/
/* heapmgr2.c                                                         */
/* Author: Tyler Campbell & Lauren Johnston			                      */
/*--------------------------------------------------------------------*/

#include "heapmgr.h"
#include "checker2.h"
#include "chunk.h"
#include <stddef.h>
#include <assert.h>

#define __USE_XOPEN_EXTENDED
#include <unistd.h>

/* In lieu of a boolean data type. */
enum {FALSE, TRUE};

/* direction of chunk for coalesce */
enum {PREV, NEXT};

/* The minimum number of units to request of the OS. */
enum {MIN_UNITS_FROM_OS = 512};

/* number of bins in freelist array */
enum {BINCOUNT = 1024};

/* Minimum size of the free chunk to split */
enum {MINSPLIT = 3};

/*--------------------------------------------------------------------*/

/* The state of the HeapMgr. */

/* The address of the start of the heap. */
static Chunk_T oHeapStart = NULL;

/* The address immediately beyond the end of the heap. */
static Chunk_T oHeapEnd = NULL;

/* array of bins, eacj been has a free list,a list of all free Chunks */
static Chunk_T bins[BINCOUNT]; 

/*--------------------------------------------------------------------*/

/* Request more memory from the operating system -- enough to store
	uUnits units. Create a new chunk, and either append it to the
	free list after oPrevChunk or increase the size of oPrevChunk.
	Return the address of the new (or enlarged) chunk. */
static Chunk_T HeapMgr_getMoreMemory(size_t uUnits)
{
	Chunk_T oChunk;
	Chunk_T oNewHeapEnd;
	
	if (uUnits < (size_t)MIN_UNITS_FROM_OS)
		uUnits = (size_t)MIN_UNITS_FROM_OS;
	
	oNewHeapEnd = (Chunk_T)((char*)oHeapEnd + Chunk_unitsToBytes(uUnits));
	
	if (oNewHeapEnd < oHeapEnd)
		return NULL;

	if (brk(oNewHeapEnd) == -1)
		return NULL;
	
	oChunk = oHeapEnd;
	oHeapEnd = oNewHeapEnd;
		
	Chunk_setUnits(oChunk, uUnits);
	Chunk_setNextInList(oChunk, NULL);
	Chunk_setPrevInList(oChunk, NULL);
		
	return oChunk;	
}

/* insert oChunk to front of freelist */
static void HeapMgr_insert(Chunk_T oChunk)
{
	Chunk_T oTmp; 
	size_t uIndex;
	
	assert(Chunk_isValid(oChunk, oHeapStart, oHeapEnd));
	
	uIndex = Chunk_getUnits(oChunk);
	
	if(uIndex >= (size_t) BINCOUNT) 
		uIndex = (size_t) BINCOUNT - 1;
		
	Chunk_setNextInList(oChunk, NULL);
	Chunk_setPrevInList(oChunk, NULL);	
	
	if (bins[uIndex] == NULL)
	{
		bins[uIndex] = oChunk;
		return;
	}
	
	oTmp = bins[uIndex];
	bins[uIndex] = oChunk;
	
	Chunk_setNextInList(bins[uIndex], oTmp);
	Chunk_setPrevInList(oTmp, bins[uIndex]);
	
	return;
}

/* remove oChunk from the free list and return it */
static Chunk_T HeapMgr_remove(Chunk_T oChunk)
{
	Chunk_T oPrev; 
	Chunk_T oNext;
	size_t uIndex;
	
	assert(Chunk_isValid(oChunk, oHeapStart, oHeapEnd));
	
	uIndex = Chunk_getUnits(oChunk);
	if(uIndex >= (size_t) BINCOUNT) 
		uIndex = (size_t) BINCOUNT - 1;
	
	oPrev = Chunk_getPrevInList(oChunk);
	oNext = Chunk_getNextInList(oChunk);	
	
	if (oPrev == NULL && oNext == NULL)
	{
		bins[uIndex] = NULL;
		return oChunk;
	}
	
	if (oPrev == NULL)
	{
		bins[uIndex] = oNext;

		Chunk_setPrevInList(bins[uIndex], NULL);
		Chunk_setNextInList(oChunk, NULL);
		return oChunk;
	}
	
	if (oNext == NULL)
	{
		Chunk_setNextInList(oPrev, NULL);
		Chunk_setPrevInList(oChunk, NULL);
		return oChunk;
	}
	
	Chunk_setNextInList(oPrev, oNext);
	Chunk_setPrevInList(oNext, oPrev);

	Chunk_setNextInList(oChunk, NULL);
	Chunk_setPrevInList(oChunk, NULL);

	return oChunk; 
}

/* Split oChunk into two Chunks
 * the first has length uUnits 
 * the second has length of the rest of the chunks.
 * Return oTail the tail of the split */
static Chunk_T HeapMgr_split(Chunk_T oChunk, size_t uUnits)
{
	Chunk_T oTail;
	
	assert(Chunk_isValid(oChunk, oHeapStart, oHeapEnd));
	
	oTail = (Chunk_T)((char*)oChunk + Chunk_unitsToBytes(uUnits));

	Chunk_setUnits(oTail, Chunk_getUnits(oChunk) - uUnits);
	Chunk_setUnits(oChunk, uUnits);

	return oTail;
}

/* remove oChunk and PREV or NEXT chunk depeding on direction wanted
 * merge both chunks then insert into front of free list
 * return the merged Chunk */
static Chunk_T HeapMgr_coalesce(Chunk_T oChunk, int direction)
{
	Chunk_T oTmp;
	size_t uUnits;
	
	assert(Chunk_isValid(oChunk, oHeapStart, oHeapEnd));
	
	if (direction == PREV)
		oTmp = Chunk_getPrevInMem(oChunk, oHeapStart);
	else
		oTmp = Chunk_getNextInMem(oChunk, oHeapEnd);
		
	uUnits = Chunk_getUnits(oChunk) + Chunk_getUnits(oTmp);	
	
	/* remove the given chunk from the free list */
	(void) HeapMgr_remove(oChunk);
	
	/* remove the previous or next chunk in memory from
	the free list */
	(void) HeapMgr_remove(oTmp);
	
	if (direction == PREV)
		oChunk = oTmp;
	
	/* coalesce them to form a larger chunk */
	Chunk_setUnits(oChunk, uUnits);
	Chunk_setStatus(oChunk, CHUNK_FREE);
	
	/* insert the larger chunk into
	the free list at the front */
	HeapMgr_insert(oChunk);
	return oChunk;
}

void *HeapMgr_malloc(size_t uBytes)
{
	Chunk_T oChunk;
	Chunk_T oTail;
	Chunk_T oTmp;
	size_t uUnits;
	size_t uIndex;
		
	if (uBytes == 0)
		return NULL;
		
	/* Step 1: Initialize the heap manager if this is the first call. */
	if (oHeapStart == NULL)
	{
		oHeapStart = (Chunk_T)sbrk(0);
		oHeapEnd = oHeapStart;
	}
	
	assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
	
	/* Step 2: Determine the number of units the new chunk should
		contain. */
	uUnits = Chunk_bytesToUnits(uBytes);
	
	if (uUnits >= (size_t) BINCOUNT)
		uIndex = (size_t) BINCOUNT - 1;
	else
		uIndex = uUnits;
		
	while(uIndex < ((size_t) BINCOUNT - 1) && bins[uIndex] == NULL)
		uIndex++;		
	
	/* For each chunk in the current bin... */
	for (oChunk = bins[uIndex]; oChunk != NULL; oChunk = Chunk_getNextInList(oChunk))
	{
		/* if the current free list chunk is not big enough */
		if(Chunk_getUnits(oChunk) < uUnits) continue;
		
		/* If the current chunk is too big */
		if ((Chunk_getUnits(oChunk) - uUnits) >= MINSPLIT)
		{	
			/* remove from list */
			oChunk = HeapMgr_remove(oChunk);
			
			/* split */
			oTail = HeapMgr_split(oChunk, uUnits);
			
			/* add to list */
			HeapMgr_insert(oTail);
			
			/* set front to in use*/
			Chunk_setStatus(oChunk, CHUNK_INUSE);
			
			/* set tail to free */
			Chunk_setStatus(oTail, CHUNK_FREE);
			
			assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
			
			/* return the front */
			return Chunk_toPayload(oChunk);
		}
		/* else current free list chunk is close to the requested size */
		
		/* remove from free list */
		oChunk = HeapMgr_remove(oChunk);
		
		/* set its status to INUSE */
		Chunk_setStatus(oChunk, CHUNK_INUSE);
 			
		assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
		
		return Chunk_toPayload(oChunk); 
	}
	
	/* Step 4: Ask the OS for more memory, and create a new chunk (or
			expand the existing chunk) at the end of the free list. */
	oChunk = HeapMgr_getMoreMemory(uUnits);
	if (oChunk == NULL)
	{
		assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
		return NULL;
	}
		
	Chunk_setStatus(oChunk, CHUNK_FREE);
	HeapMgr_insert(oChunk);
	
	/* coalesce previous chunk if needed */
	oTmp = Chunk_getPrevInMem(oChunk, oHeapStart);
	if(oTmp != NULL && Chunk_getStatus(oTmp) == CHUNK_FREE)
		oChunk = HeapMgr_coalesce(oChunk, PREV);
		
	/* Step 5: if the chunk is too big */
	if ((Chunk_getUnits(oChunk) - uUnits) >= MINSPLIT)
	{
		/* removing chunk from list */
		oChunk = HeapMgr_remove(oChunk);
		
		/* split the chunk */
		oTail = HeapMgr_split(oChunk, uUnits);

		/* set tail to free*/
		Chunk_setStatus(oTail, CHUNK_FREE);
		
		/* insert tail to front of list */
		HeapMgr_insert(oTail);

		/* set front to inuse */
		Chunk_setStatus(oChunk, CHUNK_INUSE);
		
		assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
		
		return Chunk_toPayload(oChunk);   
	}
	
	/* current free list chunk is close to the requested size */
	/* remove from list */
	(void) HeapMgr_remove(oChunk);
	
	/* set status to INUSE*/
	Chunk_setStatus(oChunk, CHUNK_INUSE);

	assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
	return Chunk_toPayload(oChunk);
}


void HeapMgr_free(void *pv)
{
	Chunk_T oChunk;
	Chunk_T oNext;
	Chunk_T oPrev;
	
	assert(pv != NULL);
	assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
			
	oChunk = Chunk_fromPayload(pv);
	
	/* 1. set status of the given chunk to free */
	Chunk_setStatus(oChunk, CHUNK_FREE);
	
	/* 2. Insert chunk into the free list at the front */	
	HeapMgr_insert(oChunk);
	
	/* 3. coalesce next chunk if needed */
	oNext = Chunk_getNextInMem(oChunk, oHeapEnd);
	if(oNext != NULL && Chunk_getStatus(oNext) == CHUNK_FREE)
		oChunk = HeapMgr_coalesce(oChunk, NEXT);
		
	/* 4. coalesce previous chunk if needed */		
	oPrev = Chunk_getPrevInMem(oChunk, oHeapStart);		
	if(oPrev != NULL && Chunk_getStatus(oPrev) == CHUNK_FREE)
		oChunk = HeapMgr_coalesce(oChunk, PREV);
		
	assert(Checker_isValid(oHeapStart, oHeapEnd, bins, BINCOUNT));
	
	return;
}
	