/*--------------------------------------------------------------------*/
/* checker2.c                                                         */
/* Author: Lauren Johnston & Tyler Campbell                           */
/*--------------------------------------------------------------------*/

#include "checker2.h"
#include <stdio.h>
#include <assert.h>

/*In lieu of a boolean data type. */
enum {FALSE, TRUE};

/* Receives Chunk_T oHeapStart, Chunk_T oHeapEnd, Chunk_T aoBins[], and
int iBinCount as input and performs tests to see if the bin/linked list
implementation is corrupt. Returns false if any of the test fail.
Returns true if all tests pass. */
int Checker_isValid(Chunk_T oHeapStart, Chunk_T oHeapEnd,
						  Chunk_T aoBins[], int iBinCount)
{
	Chunk_T oChunk;
	Chunk_T oPrevChunk;
	Chunk_T oNextChunk;
	Chunk_T oTortoiseChunk; /* slower increment in cycle check */
	Chunk_T oHareChunk;     /* faster increment in cycle check */
	Chunk_T oLastChunk;     /* last free chunk in list         */
	int iBinCounter;        /* counter for number of bins      */
	int uBinUnits;          /* intended number of units in bin */

	/* Do oHeapStart and oHeapEnd have non-NULL values? */
	if (oHeapStart == NULL)
	{
		fprintf(stderr, "The heap start is uninitialized\n");
		return FALSE;
	}
	if (oHeapEnd == NULL)
	{
		fprintf(stderr, "The heap end is uninitialized\n");
		return FALSE;
	}


	/* If the heap is empty, is the free list empty too? */
	if (oHeapStart == oHeapEnd)
	{
		for (iBinCounter = 0; iBinCounter < iBinCount; iBinCounter++)
		{
			if (aoBins[iBinCounter] != NULL)
			{
				fprintf(stderr, "The heap is empty, but the list is not.\n");
				return FALSE;
			}
		}
		return TRUE;
	}
	
	/* Traverse memory forward to check if chunks are valid */
	for (oChunk = oHeapStart;
		  oChunk != NULL;
		  oChunk = Chunk_getNextInMem(oChunk, oHeapEnd))
	{
		/* Is the chunk valid? */
		if (! Chunk_isValid(oChunk, oHeapStart, oHeapEnd))
		{
			fprintf(stderr, "Traversing memory detected a bad chunk\n");
			return FALSE;
		}
	}

	/* Traverse memory through backward links. */
	for (oChunk = Chunk_getPrevInMem(oHeapEnd, oHeapStart);
		  oChunk != NULL;
		  oChunk = Chunk_getPrevInMem(oChunk, oHeapStart))
	{
		/* Is the chunk valid? */
		if (! Chunk_isValid(oChunk, oHeapStart, oHeapEnd))
		{
			fprintf(stderr, "Traversing memory detected a bad chunk\n");
			return FALSE;
		}
	}

	/* Traverse each bin to perform forward and backward cycle check
		on the doubly-linked lists in each bin */
	for (iBinCounter = 0; iBinCounter < iBinCount; iBinCounter++)
	{
		oLastChunk = NULL;
		oTortoiseChunk = aoBins[iBinCounter];
		oHareChunk = aoBins[iBinCounter];
		if (oHareChunk != NULL) 
		{
			oLastChunk = oHareChunk;
			oHareChunk = Chunk_getNextInList(oHareChunk);
		}
		
		while (oHareChunk != NULL)
		{
			oLastChunk = oHareChunk;
			/* does the list have a cycle? */
			if (oTortoiseChunk == oHareChunk)
			{
				fprintf(stderr, "The list has a cycle\n");
				return FALSE;
			}
			/* is the chunk valid? */
			if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
			{
				fprintf(stderr, "Forward link of some element in free"
						  " list is corrupted\n");
				return FALSE;
			}
			
			oTortoiseChunk = Chunk_getNextInList(oTortoiseChunk);
			oHareChunk = Chunk_getNextInList(oHareChunk);
			
			if (oHareChunk != NULL) 
			{
				/* do List links point to meaningful positions? */
				if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
				{
					fprintf(stderr, "Forward link of some element in free"
							  " list is corrupted\n");
					return FALSE;
				}

				oLastChunk = oHareChunk;
				oHareChunk = Chunk_getNextInList(oHareChunk);
			}
		}
		/* set oTortoiseChunk and oHareChunk to the last chunk found
		previously */
		oTortoiseChunk = oLastChunk;
		oHareChunk = oLastChunk;

		if (oHareChunk != NULL)
		{
			if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
			{
				fprintf(stderr, "Backward link of the last element in the free"
						  " list is corrupted\n");
				return FALSE;
			}
			oHareChunk = Chunk_getPrevInList(oHareChunk);
		}

		while (oHareChunk != NULL)
		{
			/* does the list have a backward cycle? */
			if (oTortoiseChunk == oHareChunk)
			{
				fprintf(stderr, "The list has a cycle\n");
				return FALSE;
			}
			/* is the chunk valid for Chunk_getNextInList()? */
			if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
			{
				fprintf(stderr, "Backward link of some element in free"
						  " list is corrupted\n");
				return FALSE;
			}
			
			oTortoiseChunk = Chunk_getPrevInList(oTortoiseChunk);
			oHareChunk = Chunk_getPrevInList(oHareChunk);

			if (oHareChunk != NULL) 
			{
				/* is the chunk valid for Chunk_getPrevInList()? */
				if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
				{
					fprintf(stderr, "Backward link of some element in free"
							  " list is corrupted\n");
					return FALSE;
				}
				oHareChunk = Chunk_getPrevInList(oHareChunk);
			}
		}
	}
	
	/* Traverse each bin's free list. */
	for (iBinCounter = 0; iBinCounter < iBinCount; iBinCounter++) 
	{
		oPrevChunk = NULL;
		oNextChunk = NULL;
		for (oChunk = aoBins[iBinCounter];
			  oChunk != NULL;
			  oChunk = Chunk_getNextInList(oChunk))
		{
			oPrevChunk = Chunk_getPrevInMem(oChunk, oHeapStart);
			oNextChunk = Chunk_getNextInMem(oChunk, oHeapEnd);

			/* Is the chunk valid? */
			if (! Chunk_isValid(oChunk, oHeapStart, oHeapEnd))
			{
				fprintf(stderr, "Traversing the list detected a bad chunk\n");
				return FALSE;
			}

			/* Is status bit set correctly to CHUNK_FREE? */
			if (Chunk_getStatus(oChunk) != CHUNK_FREE)
			{
				fprintf(stderr, "The free list contains non-free chunks\n");
				return FALSE;
			}
			
			/* Is the previous chunk in memory in use? */
			if ((oPrevChunk != NULL) &&
				 (Chunk_getStatus(oPrevChunk) == CHUNK_FREE))
			{
				fprintf(stderr, "The heap contains contiguous free chunks\n");
				return FALSE;
			}
			
			/* Is the next chunk in memory in use? */
			if ((oNextChunk != NULL) &&
				 (Chunk_getStatus(oNextChunk) == CHUNK_FREE))
			{
				fprintf(stderr, "The heap contains contiguous free chunks\n");
				return FALSE;
			}
			oPrevChunk = NULL;
			if(oChunk != aoBins[iBinCounter]) /* if not in the first iteration*/
				oPrevChunk = Chunk_getPrevInList(oChunk);
			oNextChunk = Chunk_getNextInList(oChunk);
			/* Check that the list is properly linked */
			if(oPrevChunk != NULL &&
				Chunk_getNextInList(oPrevChunk) != oChunk)
			{
				fprintf(stderr, "The list not properly linked\n");
				return FALSE;
			}
			
			if(oNextChunk != NULL &&
				Chunk_getPrevInList(oNextChunk) != oChunk)
			{
				fprintf(stderr, "The list not properly linked\n");
				return FALSE;

			}
			
			
		}
	}


	for (iBinCounter = 0; iBinCounter < iBinCount - 1; iBinCounter++)

		{  

			for (oChunk = aoBins[iBinCounter];

				  oChunk != NULL;

				  oChunk = Chunk_getNextInList(oChunk))

			{

				int uUnits = (int) Chunk_getUnits(oChunk);

				if (uUnits != iBinCounter)

				{

					if	(iBinCounter != iBinCount - 1 || uUnits < iBinCount- 1) 

						fprintf(stderr, "bin %d has chunk %d units\n", iBinCounter, uUnits);

					return FALSE;

				}

			}

		}


	/* Does the free list contain all the free chunks? */
	for (oChunk = oHeapStart;
		  oChunk != NULL;
		  oChunk = Chunk_getNextInMem(oChunk, oHeapEnd))
	{
		if(Chunk_getStatus(oChunk) == CHUNK_FREE) 
		{
			Chunk_T oCurrent = NULL;
			for (iBinCounter = 0; (iBinCounter < iBinCount) && (oCurrent != oChunk); iBinCounter++)
			{
				oCurrent = aoBins[iBinCounter];
				while(oCurrent != NULL && oCurrent != oChunk) 
				{
					oCurrent = Chunk_getNextInList(oCurrent);
				}
			}
			if(oCurrent != oChunk)
			{
				fprintf(stderr, "The free list does not contain all free chunks\n");
				return FALSE;
			}
		}
	}   
	return TRUE;
}