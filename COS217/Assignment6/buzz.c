/*--------------------------------------------------------------------*/
/* buzz.c                                                             */
/* Author: Andrew Appel, 2016, modified by Iasonas Petras, 2017       */
/* Purpose:  This program finds the 25 most common words in the input */
/* file (reads from stdin), and prints them out with their usage      */
/* counts, in descending order of their frequencies.                  */
/*                                                                    */
/* The program is intentionally inefficient; thereby it will serve as */
/* an example during the "Performance Improvement" lecture.           */
/*--------------------------------------------------------------------*/
   
#include "symtable.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>
#include <time.h>
#include "heapmgr.h"

/* Handling sbrk, brk */

#define __USE_XOPEN_EXTENDED
#include <unistd.h>


#include <sys/resource.h> /* for the setCpuTimeLimit function */

/*----------- Read the input ----------------*/

/* If no words remain in stdin, then return 0. Otherwise read a word
   from stdin into the buffer whose length is buflen and return 1. */

static int readWord(char *buffer, size_t buflen) {
   int c;
   assert(buffer != NULL);
   /* Skip non-alphabetic characters */
   do {
	  c = getchar();
	  if (c==EOF) return 0;
   } while (!isalpha(c));
   buffer[0]='\0';
   /* Process alphabetic characters */
   while (isalpha(c)) {
	  if (strlen(buffer)<buflen-1) {
		 buffer[strlen(buffer)+1]='\0';
		 buffer[strlen(buffer)]=tolower(c);
	  }
	  c=getchar();
   }
   buffer[strlen(buffer)]='\0';
   return 1;
}

/* Read words from stdin. Populate table with the words and their
   occurrence counts. */
   
static void readInput (SymTable_T table) {
   enum {MAX_WORD_LENGTH = 1000};
   char word[MAX_WORD_LENGTH+1];
   assert(table != NULL);
   while (readWord(word, (size_t)(MAX_WORD_LENGTH+1))) {
	  int *count = (int*)SymTable_get(table, word);
	  if (count == NULL) {
		 count = (int*)HeapMgr_malloc(sizeof(int));
		 if (count == NULL) exit(EXIT_FAILURE);
		 *count = 0;
		 if (SymTable_put(table, word, count) == 0) exit(EXIT_FAILURE);
	  } 
	  (*count)++;
   }
}

/*----------- Extract the counts into an array ----------------*/

/* A word_and_count structure contains a word and its occurrence
   count. */
   
struct word_and_count {
   /* The word. */
   const char *word;
   /* The word's occurrence count. */
   int count;
};

/* A counts structure contains an array of word_and_count elements
   and some associated data indicating the length of the array. */

struct counts {
   /* The number of elements of the array that are currently in use. */ 
   size_t filled;
   /* The number of elements in the array. */
   size_t max;
   /* The array. */
   struct word_and_count *array;
};

/* Create and return a counts structure whose internal (unpopulated)
   array consists of max elements of type word_and_count. */

static struct counts *makeCounts(size_t max) {
   struct counts *counts = (struct counts *) HeapMgr_malloc (sizeof (*counts));
   if (counts == NULL) exit(EXIT_FAILURE);
   counts->filled=0;
   counts->max=max;
   counts->array = (struct word_and_count *) 
	  HeapMgr_malloc (max * sizeof (struct word_and_count));
   if (counts->array == NULL) exit(EXIT_FAILURE);
   return counts;
}

/* A callback function for SymTable_map. The extra parameter points to
   a counts structure. Use key (a word from a SymTable object) and
   value (a pointer to a corresponding occurrence count) to update the
   counts structure. Free the value. */
   
static void handleBinding(const char *key, void *value, void *extra) {
   struct counts *counts;
   assert(key != NULL);
   assert(value != NULL);
   assert(extra != NULL);
   counts = (struct counts *) extra;
   assert (counts->filled < counts->max);
   counts->array[counts->filled].word = key;
   counts->array[counts->filled].count = *((int*)value);
   counts->filled += 1;
   HeapMgr_free(value);
}

/* Create a counts structure. Traverse table to populate the counts
   structure. Return the count structure's address. The caller owns
   the counts structure. The counts structure contains addresses of
   strings that are owned by table, so freeing the table corrupts the
   counts structure. */

static struct counts *extractCounts(SymTable_T table) {
   struct counts *counts;
   assert(table != NULL);
   counts = makeCounts(SymTable_getLength(table));
   SymTable_map(table, handleBinding, (void*)counts);
   return counts;
}

/*----------- Sort the counts  ----------------*/

/* Swap the contents of structures *p1 and *p2. */

static void swap (struct word_and_count *p1, struct word_and_count *p2)
{
   struct word_and_count temp;
   assert(p1 != NULL);
   assert(p2 != NULL);
   temp=*p1; *p1=*p2; *p2=temp;
}

/* Sort the array within counts in descending order by occurrence
   count. */

static void sortCounts (struct counts *counts) {
   /* insertion sort */
   size_t i,j,n;
   struct word_and_count *array;
   assert(counts != NULL);
   n = counts->filled;
   array = counts->array;
   for (i=1; i<n; i++) {
	  for (j=i; j>0 && array[j-1].count<array[j].count; j--)
		 swap(array+j, array+j-1);
   }
}  

/*----------- Main program ----------------*/

/* Write the 25 most common words of *counts to stdout. */

static void analyzeData(struct counts *counts) {
   enum {MAX_WORDS = 25};
   size_t i, n; 
   assert(counts != NULL);
   assert(counts->filled == counts->max);
   sortCounts(counts);
   n = MAX_WORDS<counts->max ? MAX_WORDS : counts->max;
   for (i=0; i<n; i++)
	  printf("%10d %s\n", counts->array[i].count,
		 counts->array[i].word);
}

/* Set the process's "CPU time" resource limit. After the CPU
   time limit expires, the OS will send a SIGKILL signal to the
   process. */

static void setCpuTimeLimit(void)
{
   enum {CPU_TIME_LIMIT_IN_SECONDS = 300};
   struct rlimit sRlimit;
   sRlimit.rlim_cur = CPU_TIME_LIMIT_IN_SECONDS;
   sRlimit.rlim_max = CPU_TIME_LIMIT_IN_SECONDS;
   setrlimit(RLIMIT_CPU, &sRlimit);
}


/* Read words from stdin until end-of-file. Write the 25 most
   common words and their counts to stdout. Return 0 if successful
   and EXIT_FAILURE otherwise. */

int main(int argc, char** argv) {
	struct counts *counts;
	/* Measuring performance */
	clock_t iInitialClock;
   	clock_t iFinalClock;
   	char *pcInitialBreak;
   	char *pcFinalBreak; 
   	unsigned int uiMemoryConsumed;
   	double dTimeConsumed;
   	SymTable_T table;

  	printf("=============================================\n");
   	printf("%s\n", argv[0]);
  	printf("---------------------------------------------\n");


   	/* Save the initial clock and program break. */
   	iInitialClock = clock();
   	pcInitialBreak = (char*) sbrk(0);

 	/* Set the process's CPU time limit. */
   	setCpuTimeLimit();

   	/* Main client code */
	table = SymTable_new();
	if (table == NULL) exit(EXIT_FAILURE);
	readInput(table);
	/* If there are no words in the file, don't analyze the data. */
	if (SymTable_getLength(table) > 0){
		counts = extractCounts(table);
		analyzeData(counts);
		HeapMgr_free(counts->array);
		HeapMgr_free(counts);
	}
	SymTable_free(table);

	/* Save the final clock and program break. */
	pcFinalBreak = (char*) sbrk(0);
   	iFinalClock = clock();

	/* Use the initial and final clocks and program breaks to compute
    CPU time and heap memory consumed. */
 	uiMemoryConsumed = (unsigned int)(pcFinalBreak - pcInitialBreak);
   	dTimeConsumed = ((double)(iFinalClock - iInitialClock)) / CLOCKS_PER_SEC;

   	/* Finish printing the results. */
   	printf("---------------------------------------------\n");
   	printf("Time Consumed \t Memory Consumed \n");
   	printf("%6.2f %10u\n", dTimeConsumed, uiMemoryConsumed);
  	printf("=============================================\n");

	return 0;
}
