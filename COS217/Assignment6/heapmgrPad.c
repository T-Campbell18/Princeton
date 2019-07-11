/*---------------------------------------------------------------------*/
/* heapmgrPad.c 	                                               */
/* Author: Iasonas Petras, based on COS 217 Dynamic Memory Lecture     */
/*								       */
/* On HeapMgr_malloc: Assign new space in the heap in "bulk", creating */
/* padded space.						       */
/* When you get a new request, either assign space from the remaining  */
/* memory in the pad, or request more memory from the OS in "bulk"     */
/* On free: Do nothing.						       */
/* 								       */
/*---------------------------------------------------------------------*/

#include "heapmgr.h"

#define _BSD_SOURCE
#include <unistd.h>

/*--------------------------------------------------------------------*/

/* The state of the HeapMgr. */

/* The address of the start of the heap. */
static void* oHeapStart = NULL;

/* The address immediately beyond the end of the heap. */
static void* oHeapEnd = NULL;

/* The pointer to the end of the INUSE part of the heap */
static void* oPad = NULL;

/*--------------------------------------------------------------------*/


void *HeapMgr_malloc(size_t uBytes)
{
	enum {MIN_BYTES_FROM_OS = 8192}; /* Same size of memory as 512 units requested in implementations 1 and 2. */
	char* pcNewHeapEnd;
	char* pcOldoPad;

	/* if the requested size is 0, return NULL */
	if (uBytes == 0)
		return NULL;

	/* Initialize the heap manager if this is the first call. */
	if (oHeapStart == NULL)
	{
		oHeapStart = sbrk(0);
		oHeapEnd = oHeapStart;
		oPad = oHeapEnd;
	}

	/* Check if there is enough space. If yes, then simply move the oPad pointer. Otherwise, request for 
	more memory */

	if ((char*) oPad + uBytes > (char*) oHeapEnd)
	{

		pcNewHeapEnd = (char*)oPad + uBytes > (char*)oHeapEnd + MIN_BYTES_FROM_OS ?  
			(char*)oPad + uBytes : (char*)oHeapEnd + MIN_BYTES_FROM_OS;
		if(brk(pcNewHeapEnd) == -1)
			return NULL;
		oHeapEnd = (void*) pcNewHeapEnd;
	}

	pcOldoPad = (char*) oPad;
	oPad = (void*)((char*) oPad + uBytes);

	return (void*) pcOldoPad;
}


void HeapMgr_free(void *pv)
{

}
