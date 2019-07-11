/*--------------------------------------------------------------------*/
/* heapmgrminimal.c                                                   */
/* Author: Iasonas Petras, based on COS 217 Dynamic Memory Lecture    */
/*								      */
/* On HeapMgr_malloc: Move the program break an appropriate amount    */
/* of addresses.						      */
/* On free: Do nothing.						      */
/* 								      */
/*--------------------------------------------------------------------*/

#include "heapmgr.h"

#define _BSD_SOURCE
#include <unistd.h>

/*--------------------------------------------------------------------*/

/* The state of the HeapMgr. */

/* The address of the start of the heap. */
static void* oHeapStart = NULL;

/* The address immediately beyond the end of the heap. */
static void* oHeapEnd = NULL;

/*--------------------------------------------------------------------*/


void *HeapMgr_malloc(size_t uBytes)
{
	
	char* pcPrevHeapEnd;
	char* pcNewHeapEnd;

	/* if the requested size is 0, return NULL */

	if (uBytes == 0)
		return NULL;

	/* Initialize the heap manager if this is the first call. */
	if (oHeapStart == NULL)
	{
		oHeapStart = sbrk(0);
		oHeapEnd = oHeapStart;
	}

	pcNewHeapEnd = (char*)oHeapEnd + uBytes;
	if (pcNewHeapEnd < (char*) oHeapEnd)
		return NULL;
	if (brk(pcNewHeapEnd) == -1)
		return NULL;

	pcPrevHeapEnd = (char*) oHeapEnd;
	oHeapEnd = (void*) pcNewHeapEnd;

	return (void*) pcPrevHeapEnd;

}


void HeapMgr_free(void *pv)
{
}
