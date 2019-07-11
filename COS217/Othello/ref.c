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
	int track = 0;
	char t[] = "-tracking";
	char player1name[25];
	char player2name[25];
	char player1[25] = "./";
	char player2[25] = "./";
	
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
	
	if (argc != 3 && argc != 4)
	{
		fprintf(stderr, "[player1] [player2] or -tracking [player1][player2]\n");
		return 1;
	}
	if (argc == 4 && strncmp(t, argv[1], 9) == 0)
	{
		track = TRUE;
	}
	if (track)
	{
		strcpy(player1name, argv[2]);
		strcpy(player2name, argv[3]);
	}
	else 
	{
		strcpy(player1name, argv[1]);
		strcpy(player2name, argv[2]);
	}
	if (access(player1name, F_OK) == 0 && access(player1name, X_OK) == 0)
	{
		strcat(player1, player1name);
	}
	else 
	{
		fprintf(stderr, "File %s does not exist or is not executable\n", player1name);
		return 1;
	}
	if (access(player2name, F_OK) == 0 && access(player2name, X_OK) == 0)
	{
		strcat(player2, player2name);
	}
	else 
	{
		fprintf(stderr, "File %s does not exist or is not executable\n", player2name);
		return 1;
	}
		
	createBoard(board);
		
	if (track)
	{
		strcpy(file, player1name);
		strcat(file, "_vs_");
		strcat(file, player2name);
			
		fp = fopen(file, "w");
			
		fprintf(fp, "Initial game state:\n");
		printBoard(board, fp);
	}
		
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
		argvc[0] = player1;
		argvc[1] =  "FIRST";
		argvc[2] =  NULL; 
			
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
		argvc[0] = player2;
		argvc[1] =  "SECOND";
		argvc[2] =  NULL;
		 
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
				
		execvp(argvc[0], argvc);
		perror(argvc[0]);
		exit(EXIT_FAILURE);
	}
					
	close(outfd1[0]);
	close(infd1[1]);
	close(outfd2[0]); 
	close(infd2[1]);		
		
	ofd1 = fdopen(outfd1[1], "w");
	ofd2 = fdopen(outfd2[1], "w");
	ifd1 = fdopen(infd1[0], "r");
	ifd2 = fdopen(infd2[0], "r");
	
	fscanf(ifd1, "%s", &input);
	iRet = fflush(NULL);
	if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
	
	printf("%s\n", input);
	j = letterToNum(input[0]);
	i = letterToNum(input[1]);


	
			
	while (TRUE)
		{		
			if (p == X)
			{
				fscanf(ifd1, "%s", &input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				j = letterToNum(input[0]);
				i = letterToNum(input[1]);

				if (validMove(i, j, p, board))
				{
					flip(i, j, p, board);
				}
				else 
				{
					printf("1 exit game\n");
					exit(0);
				}
			}
			if (p == O)
			{	
				fscanf(ifd2, "%s", &input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				j = letterToNum(input[0]);
				i = letterToNum(input[1]);

				if (validMove(i, j, p, board))
				{
					flip(i, j, p, board);
				}
				else 
				{
					printf("2 exit game\n");
					exit(0);
				}
			}
			
			
			if (track)
			{
				
			}
			
			if (boardFull(board) || (!anyValid(X, board) && !anyValid(O, board)))
			{
				kill(iPid1, SIGKILL);
				kill(iPid2, SIGKILL);
				break;
			}
				
			if (anyValid(oppColor(p), board))
				p = oppColor(p);
		}
			
			
	close(outfd1[1]);
	close(infd1[0]);
	close(outfd2[1]);
	close(infd2[0]);
	
	score = getScore(board);
	if (track)
	{
		fprintf(fp, "FIRST (%s) vs SECOND (%s)\n", player1name, player2name);
		if (score > 0)
			fprintf(fp, "\tWinner FIRST %s\n", player1name);
		else if (score < 0)
			fprintf(fp, "\tWinner SECOND %s\n", player2name);
		else
			fprintf(fp, "\tDRAW\n");
		fprintf(fp, "\tScore %d\n", score);
		fclose(fp);
	}
	printf("%d\n", score);
	return 0;
}
