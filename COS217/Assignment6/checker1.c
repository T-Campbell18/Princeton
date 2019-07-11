/*--------------------------------------------------------------------*/
/* checker1.c                                                         */
/* Author: Lauren Johnston & Tyler Campbell                           */
/*--------------------------------------------------------------------*/

#include "checker1.h"
#include <stdio.h>
#include <assert.h>

/* In lieu of a boolean data type. */
enum {FALSE, TRUE};

/*--------------------------------------------------------------------*/

int Checker_isValid(Chunk_T oHeapStart, Chunk_T oHeapEnd,
   Chunk_T oFreeList)
{
   Chunk_T oChunk;
   Chunk_T oPrevChunk;
   Chunk_T oTortoiseChunk;
   Chunk_T oHareChunk;
   Chunk_T oNextChunk;
   Chunk_T oPrevMemChunk;
   Chunk_T oLastChunk;
   int iFreeCounter;
   int iFreeListCounter;

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
      if (oFreeList == NULL)
         return TRUE;
      else
      {
         fprintf(stderr, "The heap is empty, but the list is not.\n");
         return FALSE;
      }

   }

   /* Traverse memory forward */
   iFreeCounter = 0;

   for (oChunk = oHeapStart;
        oChunk != NULL;
        oChunk = Chunk_getNextInMem(oChunk, oHeapEnd)) {

      /* Is the chunk valid? */
      if (! Chunk_isValid(oChunk, oHeapStart, oHeapEnd))
      {
         fprintf(stderr, "Traversing memory detected a bad chunk\n");
         return FALSE;
      }

      /* Is every free chunk also in the free list? */
       if (Chunk_getStatus(oChunk) == CHUNK_FREE) {
        iFreeCounter++;
      }
    }


   /* Is the list devoid of cycles? Use Floyd's algorithm to find out.
      See the Wikipedia "Cycle detection" page for a description. */

   oTortoiseChunk = oFreeList;
   oHareChunk = oFreeList;
   oLastChunk = NULL;

   if (oHareChunk != NULL) {
      oLastChunk = oHareChunk;
      oHareChunk = Chunk_getNextInList(oHareChunk);
    }
   while (oHareChunk != NULL)
   {
     oLastChunk = oHareChunk;

      if (oTortoiseChunk == oHareChunk)
      {
         fprintf(stderr, "The list has a cycle\n");
         return FALSE;
      }
      /* Move oTortoiseChunk one step. */
      oTortoiseChunk = Chunk_getNextInList(oTortoiseChunk);
      /* Move oHareChunk two steps, if possible. */
      oHareChunk = Chunk_getNextInList(oHareChunk);
      if (oHareChunk != NULL) {
         oLastChunk = oHareChunk;
         oHareChunk = Chunk_getNextInList(oHareChunk);
       }
   }
   /* Trying to traverse list backwards - seg faults */

   oTortoiseChunk = oLastChunk;
   oHareChunk = oLastChunk;

   if (oHareChunk != NULL) {
      oHareChunk = Chunk_getPrevInList(oHareChunk);
    }
   while (oHareChunk != NULL)
   {
      if (oTortoiseChunk == oHareChunk)
      {
         fprintf(stderr, "The list has a cycle\n");
         return FALSE;
      }
      /* Move oTortoiseChunk one step. */
      oTortoiseChunk = Chunk_getPrevInList(oTortoiseChunk);

      /* Move oHareChunk two steps, if possible. */

      /* do List links point to meaningful positions?*/
      if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
      {
         fprintf(stderr, "Backward link of some element in free"
                 "list is corrupted\n");
         return FALSE;
      }

      oHareChunk = Chunk_getPrevInList(oHareChunk);

      if (oHareChunk != NULL)
      {
         /* do List links point to meaningful positions?*/
         if(!Chunk_isValid(oHareChunk, oHeapStart, oHeapEnd))
         {
            fprintf(stderr, "Backward link of some element in free"
                    "list is corrupted\n");
            return FALSE;
         }
         oHareChunk = Chunk_getPrevInList(oHareChunk);
      }
   }



   /* Traverse the free list. */
   oPrevChunk = NULL;
   iFreeListCounter = 0;

   for (oChunk = oFreeList;
        oChunk != NULL;
        oChunk = Chunk_getNextInList(oChunk))
   {
      /* Is the chunk valid? */
      if (! Chunk_isValid(oChunk, oHeapStart, oHeapEnd))
      {
         fprintf(stderr, "Traversing the list detected a bad chunk\n");
         return FALSE;
      }

      /* Is the chunk in the proper place in the list? */
      if ((oPrevChunk != NULL) && Chunk_getPrevInList(oChunk) != oPrevChunk)
      {
         fprintf(stderr, "The list not properly linked\n");
         return FALSE;
      }

      /* Is the chunk's status correctly set to free? */
      if (Chunk_getStatus(oChunk) != CHUNK_FREE) {
        fprintf(stderr, "The free list contains non-free chunks\n");
        return FALSE;
      }

      /* Is the previous chunk in memory in use? */
      oPrevMemChunk = Chunk_getPrevInMem(oChunk, oHeapStart);
      if ((oPrevMemChunk != NULL) && (Chunk_getStatus(oPrevMemChunk) == CHUNK_FREE))
      {
         fprintf(stderr, "The heap contains contiguous free chunks\n");
         return FALSE;
      }


      /* Is the next chunk in memory in use? */
      oNextChunk = Chunk_getNextInMem(oChunk, oHeapEnd);
      if ((oNextChunk != NULL) && (Chunk_getStatus(oNextChunk) == CHUNK_FREE))
      {
         fprintf(stderr, "The heap contains contiguous free chunks\n");
         return FALSE;
      }

      oPrevChunk = oChunk;
      iFreeListCounter++;

   }

   /* Does the free list contain all the free chunks? */
   if (iFreeListCounter != iFreeCounter)
   {
      fprintf(stderr, "The free list does not contain all free chunks\n");
      return FALSE;
   }

   return TRUE;
}
