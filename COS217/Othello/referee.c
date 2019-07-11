/*--------------------------------------------------------------------*/
/* referee.c                                                          */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/

#define _POSIX_SOURCE 1 /* for fdopen */
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <signal.h>
#include <time.h>
#include <sys/resource.h>
#include <stdio.h>
#include "displaygame.h"

/* in lieu of boolean */
enum {FALSE, TRUE};
/* tile on board */
enum {OPEN, X, O};
#define SIZE 8

/* creates the starting board */
static void createBoard(int board[SIZE][SIZE])
{
	int i,j;
	for(i=0;i<SIZE;i++)
	 for(j=0;j<SIZE;j++)
	 	board[i][j] = OPEN;

	board[4][4]=O;
	board[3][3]=O;
	board[4][3]=X; 
	board[3][4]=X;
}

/* prints the ends results of game by player1name and player2name
the score to file pointer fp */
static void printEnd(FILE *fp, char *player1name, char *player2name, int score)
{
	fprintf(fp, "FIRST (%s) vs SECOND (%s)\n", player1name, player2name);
	if (score > 0)
		fprintf(fp, "\tWinner FIRST %s\n", player1name);
	else if (score < 0)
		fprintf(fp, "\tWinner SECOND %s\n", player2name);
	else
		fprintf(fp, "\tDRAW\n");
	fprintf(fp, "\tScore %d\n", score);
}


/* Set the process's "CPU time" resource limit.  After the CPU
	time limit expires, the OS will send a SIGKILL signal to the
	process. */
static void setCpuTimeLimit(void)
{
	enum {CPU_TIME_LIMIT_IN_SECONDS = 60};
	struct rlimit sRlimit;
	sRlimit.rlim_cur = CPU_TIME_LIMIT_IN_SECONDS;
	sRlimit.rlim_max = CPU_TIME_LIMIT_IN_SECONDS;
	setrlimit(RLIMIT_CPU, &sRlimit);
}


int main(int argc, char *argv[]) 
{
	int track = 0; /* boolean of for tracking parameter */
	char t[] = "-tracking";
	char player1name[25]; /* minplayer name from argument */
	char player2name[25]; /* maxplayer name from argument */
	char player1[25] = "./"; /* exec file for minplayer */
	char player2[25] = "./"; /* exec file for maxplayer */
	char *argvc[3];

	int repeat; /* does one player have back to back moves */
	int board[8][8]; /* game board */
	FILE *fp; /* file for tracking */
	char file[50]; /* file name */
	int score; /* score of game */
	int p = X; /* first player in X*/
	
	int i,j; /* loop varibles */
	int move = 0; /* number of moves in game */
	char input[4] = "";
	
	int iPid1, iPid2; /* child pids */
	int iRet;
	
	int outfd1[2];
	int infd1[2];
	int outfd2[2];
	int infd2[2];
	
	FILE *ifd1;
	FILE *ofd1;
	FILE *ifd2;
	FILE *ofd2;
	/* check for valid agruments */
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
	/* is file exist and exec */
	if (access(player1name, F_OK) == 0 && access(player1name, X_OK) == 0)
	{
		strcat(player1, player1name);
	}
	else 
	{
		fprintf(stderr, "File %s does not exist or is not executable\n", player1name);
		return 1;
	}
	/* is file exist and exec */
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
	
	/* child process 1 */
	iPid1 = fork();
	if (iPid1 == -1) {perror(argv[0]); exit(EXIT_FAILURE); }
	if (iPid1 == 0)
	{
		setCpuTimeLimit();
		argvc[0] = player1;
		argvc[1] =  "FIRST";
		argvc[2] =  NULL; 
		
		/* redirect pipes */
		close(STDOUT_FILENO);
		close(STDIN_FILENO);

		dup2(outfd1[0], STDIN_FILENO);
		dup2(infd1[1], STDOUT_FILENO);

		close(outfd1[0]); 
		close(outfd1[1]);
		close(infd1[0]);
		close(infd1[1]);
		
		/* exec player as 1 */
		execvp(argvc[0], argvc);
		perror(argvc[0]);
		exit(EXIT_FAILURE);
	}
	/* child process 2 */
	iPid2 = fork();
	if (iPid2 == -1) {perror(argv[0]); exit(EXIT_FAILURE); }
	if (iPid2 == 0)
	{	
		setCpuTimeLimit();
		argvc[0] = player2;
		argvc[1] =  "SECOND";
		argvc[2] =  NULL;
		
		/* redirect pipes */
		close(STDOUT_FILENO);
		close(STDIN_FILENO);

		dup2(outfd2[0], STDIN_FILENO);
		dup2(infd2[1], STDOUT_FILENO);

		close(outfd2[0]); 
		close(outfd2[1]);
		close(infd2[0]);
		close(infd2[1]);
		
		/* exec player as 2 */
		execvp(argvc[0], argvc);
		perror(argvc[0]);
		exit(EXIT_FAILURE);
	}
			
	close(outfd1[0]);
	close(infd1[1]);
	close(outfd2[0]); 
	close(infd2[1]);		
	
	/* wrap pipe */
	ofd1 = fdopen(outfd1[1], "w");
	ofd2 = fdopen(outfd2[1], "w");
	ifd1 = fdopen(infd1[0], "r");
	ifd2 = fdopen(infd2[0], "r");
	
	
	/* read first move and apply to board */
	if (fscanf(ifd1, "%s", input) == EOF)
	{
		score = -64;
		printf("%d\n", score);
		if (track)
		{
			printEnd(fp, player1name, player2name, score);
		}
		kill(iPid1, SIGKILL);
		kill(iPid2, SIGKILL);
		exit(0);
	}
	iRet = fflush(NULL);
	if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
	
	j = letterToNum(input[0]);
	i = letterToNum(input[1]);
	if (track)
		fprintf(fp, "\nMove #%d (by FIRST player): %s\n\n", move, input);
	move++;
	if (validMove(i, j, p, board))
	{
		flip(i, j, p, board);
		p = oppColor(p);
	}
	else 
	{
		score = -64;
		printf("%d\n", score);
		if (track)
		{
			printEnd(fp, player1name, player2name, score);
			fprintf(fp, "\tBad move\n");
		}
		kill(iPid1, SIGKILL);
		kill(iPid2, SIGKILL);
		exit(0);
	}
	/* read and write moves to correct player and apply to board until 
	game is complete */		
	while (TRUE)
	{		
		if (p == X)
		{
			if (repeat)
			{
				fprintf(ofd2, "%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				if (fscanf(ifd1, "%s", input) == -1)
				{
					score = -64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
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
					score = -64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
						fprintf(fp, "\tBad move\n");
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
			}
			else 
			{
				fprintf(ofd1, "%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				if (fscanf(ifd1, "%s", input) == -1)
				{
					score = -64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
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
					score = -64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
						fprintf(fp, "\tBad move\n");
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
			}
		}
		if (p == O)
		{
			if (repeat)
			{
				fprintf(ofd1, "%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				if (fscanf(ifd2, "%s", input) == -1)
				{
					score = 64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
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
					score = 64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
						fprintf(fp, "\tBad move\n");
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
			}
			else 
			{
				fprintf(ofd2, "%s\n", input);
				iRet = fflush(NULL);
				if (iRet == EOF) {perror(argv[0]); exit(EXIT_FAILURE);}
				
				if (fscanf(ifd2, "%s", input) == -1)
				{
					score = 64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
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
					score = 64;
					printf("%d\n", score);
					if (track)
					{
						printEnd(fp, player1name, player2name, score);
						fprintf(fp, "\tBad move\n");
					}
					kill(iPid1, SIGKILL);
					kill(iPid2, SIGKILL);
					exit(0);
				}
			}
		}
		
		if (track)
		{
			fprintf(fp, "\nMove #%d (by FIRST player): %s\n\n", move, input);
			fprintf(fp, "Current game state:\n");
			printBoard(board, fp);
		}
		
		if (boardFull(board) || (!anyValid(X, board) && !anyValid(O, board)))
		{
			kill(iPid1, SIGKILL);
			kill(iPid2, SIGKILL);
			break;
		}
			
		if (anyValid(oppColor(p), board))
		{
			repeat = FALSE;
			p = oppColor(p);
		}
		else 
		{
			repeat = TRUE;
		}
		move++;
	}
	
	close(outfd1[1]);
	close(infd1[0]);
	close(outfd2[1]);
	close(infd2[0]);
	fclose(ofd1);
	fclose(ofd2);
	fclose(ifd1);
	fclose(ifd2);
	
	score = getScore(board);
	if (track)
	{
		printEnd(fp, player1name, player1name, score);
		fclose(fp);
	}
	printf("%d\n", score);
	return 0;
}

