/*--------------------------------------------------------------------*/
/* add1.c                                                             */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/
#include <stdio.h>
#include <stdlib.h> 

int main(int argc, char *argv[]) 
{
	int num;
	int iRet;
	while (1)
	{
		scanf("%d", &num);
		num++;
		printf("%d\n", num);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
	}
	return 0;
}