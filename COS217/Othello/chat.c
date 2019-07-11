/*--------------------------------------------------------------------*/
/* chat.c                                                             */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) 
{
	int iPid1, iPid2;
	int iRet;
	int x;
	
	int outfd1[2];
	int infd1[2];
	int outfd2[2];
	int infd2[2];
	
	FILE *ifd1;
	FILE *ofd1;
	FILE *ifd2;
	FILE *ofd2;
	
	int input = 0;
	
	pipe(outfd1); /* Where the parent is going to write to */
	pipe(infd1); /* From where parent is going to read */
	pipe(outfd2); /* Where the parent is going to write to */
	pipe(infd2); /* From where parent is going to read */
	
	iRet = fflush(NULL);
	if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE); }
	
	iPid1 = fork();
	if (iPid1 == -1) {perror(argv[0]); exit(EXIT_FAILURE); }
	if (iPid1 == 0)
	{
		char *argvc[]={ "./add1", NULL};
		
		close(STDOUT_FILENO);
		close(STDIN_FILENO);

		dup2(outfd1[0], STDIN_FILENO);
		dup2(infd1[1], STDOUT_FILENO);

		close(outfd1[0]); 
		close(outfd1[1]);
		close(infd1[0]);
		close(infd1[1]);
		
		execvp(argvc[0], argvc);
		perror(argvc[0]);
		exit(EXIT_FAILURE);
	}
	iPid2 = fork();
	if (iPid2 == -1) {perror(argv[0]); exit(EXIT_FAILURE); }
	if (iPid2 == 0)
	{	
		char *argvc[]={ "./add2", NULL};
 
		
		close(STDOUT_FILENO);
		close(STDIN_FILENO);

		dup2(outfd2[0], STDIN_FILENO);
		dup2(infd2[1], STDOUT_FILENO);

		close(outfd2[0]); 
		close(outfd2[1]);
		close(infd2[0]);
		close(infd2[1]);
		
		execvp(argvc[0], argvc);
		perror(argvc[0]);
		exit(EXIT_FAILURE);
	}
	
	close(outfd1[0]); /* These are being used by the child */
	close(infd1[1]);
	close(outfd2[0]); 
	close(infd2[1]);		

	ofd1 = fdopen(outfd1[1], "w");
	ofd2 = fdopen(outfd2[1], "w");
	ifd1 = fdopen(infd1[0], "r");
	ifd2 = fdopen(infd2[0], "r");
	
	for (x = 0; x < 10; x++)
	{		
		fprintf(ofd1, "%d\n", input);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
		
		fscanf(ifd1, "%d", &input);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
		
		printf("%d\n", input);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
		fprintf(ofd2, "%d\n", input);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
		
		fscanf(ifd2, "%d", &input);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
		
		printf("%d\n", input);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
	}
	
	close(outfd1[1]);
	close(infd1[0]);
	close(outfd2[1]);
	close(infd2[0]);
			
	return 0;
}