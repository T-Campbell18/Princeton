#define MAX_BUFFER_LENGTH 72
#include <stdio.h>

/* Produces a file named dataC, that causes the grader program to 
generate a segmentation fault */
int main(void)
{
	FILE* file;
	int i;
	unsigned char data = 0;
	
	/* open file */
	file = fopen("memorymap", "w");

	for (i = 0; i < MAX_BUFFER_LENGTH; i++)
	{
		fwrite(&data, 1, 1, file);
	}
	
	return 0;
}