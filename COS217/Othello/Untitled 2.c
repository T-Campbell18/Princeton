#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>
#include "displaygame.h"

enum {FALSE, TRUE};
enum {OPEN, X, O};
void createBoard(int board[8][8])
{
	int i,j;
	for(i=0;i<8;i++)
	 for(j=0;j<8;j++)
	 	board[i][j] = OPEN;


	board[4][4]=O;
	board[3][3]=O;
	board[4][3]=X; 
	board[3][4]=X;
}

int main(int argc, char *argv[]) 
{
	char *argvc[3];

		int over;
		int board[8][8];
		FILE *fp;
		char file[50];
		int score;
		int p = X;
		
		int i,j;
		char input[4] = "";
		
		int iPid1, iPid2;
		int iRet;
		
		int outfd1[2];
		int infd1[2];
		int outfd2[2];
		int infd2[2];
		
		FILE *ifd1;
		FILE *ofd1;
		FILE *ifd2;
		FILE *ofd2;
			
		createBoard(board);
			
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
				char *argv[]={ "./add1", NULL};
				
				close(STDOUT_FILENO);
				close(STDIN_FILENO);

				dup2(outfd1[0], STDIN_FILENO);
				dup2(infd1[1], STDOUT_FILENO);

				close(outfd1[0]); 
				close(outfd1[1]);
				close(infd1[0]);
				close(infd1[1]);
				
				execvp(argv[0], argv);
				perror(argv[0]);
				exit(EXIT_FAILURE);
			}
			iPid2 = fork();
			if (iPid2 == -1) {perror(argv[0]); exit(EXIT_FAILURE); }
			if (iPid2 == 0)
			{	
				char *argv[]={ "./add2", NULL};
		 
				/* Close fds not required by child. Also, we don't
				   want the exec'ed program to know these existed */
				close(STDOUT_FILENO);
				close(STDIN_FILENO);

				dup2(outfd2[0], STDIN_FILENO);
				dup2(infd2[1], STDOUT_FILENO);

				close(outfd2[0]); 
				close(outfd2[1]);
				close(infd2[0]);
				close(infd2[1]);
				
				execvp(argv[0], argv);
				perror(argv[0]);
				exit(EXIT_FAILURE);
			}
			
			close(outfd1[0]); /* These are being used by the child */
			close(infd1[1]);
			close(outfd2[0]); 
			close(infd2[1]);		
			int input = 0;
			ofd1 = fdopen(outfd1[1], "w");
			ofd2 = fdopen(outfd2[1], "w");
			ifd1 = fdopen(infd1[0], "r");
			ifd2 = fdopen(infd2[0], "r");
		
		fscanf(ifd1, "%s", &input[4]);
		iRet = fflush(NULL);
		if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
		
		j = letterToNum(input[0]);
		i = letterToNum(input[1]);
		printf("%s", input);

		if (validMove(i, j, p, board))
		{
			flip(i, j, p, board);
			p = oppColor(p);
		}
		else 
		{
			printf("exit game");
		}
				
		while (1)
		{		
			if (p == O)
			{
				fprintf(ofd1, "%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
					
				fscanf(ifd1, "%s", &input[4]);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
						
				printf("%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				j = letterToNum(input[0]);
				i = letterToNum(input[1]);
				printf("%s", input);
				if (validMove(i, j, p, board))
				{
					flip(i, j, p, board);
				}
				else 
				{
					printf("exit game");
				}
			}
			if (p == X)
			{
				fprintf(ofd2, "%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
						
				fscanf(ifd2, "%s", &input[4]);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
						
				printf("%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				j = letterToNum(input[0]);
				i = letterToNum(input[1]);
				printf("%s", input);
				if (validMove(i, j, p, board))
				{
					flip(i, j, p, board);
				}
				else 
				{
					printf("exit game");
				}
				
			}
			if (anyValid(oppColor(p), board))
				p = oppColor(p);
			if (!anyValid(X, board) && !anyValid(O, board))
			{
				break;
			}
			break;
		}
				
				
		close(outfd1[1]);
		close(infd1[0]);
		close(outfd2[1]);
		close(infd2[0]);
}