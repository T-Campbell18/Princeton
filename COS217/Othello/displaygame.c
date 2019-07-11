/*--------------------------------------------------------------------*/
/* displaygame.c                                                      */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/

#include "displaygame.h"
#include <stdio.h>
#include <assert.h>
/* in lieu of boolean */
enum {FALSE, TRUE};
/* tile on board */
enum {OPEN, X, O};
#define SIZE 8


void printBoard(int board[SIZE][SIZE], FILE *fp)
{
	int i,j;
	char tmp;
	assert(fp != NULL);
	fprintf(fp,"FIRST = x, SECOND = o\n\n");

	fprintf(fp, "  A B C D E F G H\n");
	for (i = 0; i < SIZE; i++)
	{
		fprintf(fp, "%d", i);
		for (j = 0; j < SIZE; j++)
		{
			if (board[i][j] == X)
				tmp = 'x';
			else if(board[i][j] == O)
				tmp = 'o';
			else
				tmp = '.';
			fprintf(fp, " %c", tmp);
		}
		fprintf(fp, "\n");
	}
}
/* returns if the postion i, j is off the board or not */
int outOfBounds(int i, int j)
{
	if (i < 0 || i >= SIZE)
		return TRUE;
	if (j < 0 || j >= SIZE)
		return TRUE;	
	return FALSE;	
}

int letterToNum(char c)
{
	switch (c) 
	{
		case 'A':
			return 0;
		case 'B':
			return 1;
		case 'C':
			return 2;
		case 'D':
			return 3;
		case 'E':
			return 4;
		case 'F':
			return 5;
		case 'G':
			return 6;
		case 'H':	
			return 7;
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':	
			return 7;	
		default:
			break;		
	}
	return -1;	
}
int validMove(int i, int j, int color, int board[SIZE][SIZE])
{
	int x, y;
	int i2, j2;
	/* is location on board */
	if (outOfBounds(i, j))
		return FALSE;
	/* if location empty */
	if (board[i][j] == 0)
	{
		/* check all direction */
		for (x = -1; x <= 1; x++)
		{
			for (y = -1; y <= 1; y++)
			{
				/* skip if on same location */
				if (x == 0 && y == 0)
					continue;
				i2 = x + i;
				j2 = y + j;
				if (outOfBounds(i2, j2))
					continue;
				if (board[i2][j2] == OPEN || board[i2][j2] == color)
					continue; 
				/* immedaite neighbor is oppColr */ 
				while (TRUE)
				{
					i2 += x;
					j2 += y;
					if (outOfBounds(i2, j2) || board[i2][j2] == OPEN)
						break;
					/* is valid move */
					if (board[i2][j2] == color)
						return TRUE;	
				}			
			}
		}
	}
	/* no valid move found */
	return FALSE;
}
int oppColor(int color)
{
	if (color == X)
		return O;
	return X;	
}
void flip(int i, int j, int color, int board[SIZE][SIZE])
{
	int x, y;
	int i2, j2;
	/* is location on board */
	if (outOfBounds(i, j))
		return;
	/* if location empty */
	if (board[i][j] == 0)
	{
		/* check all direction */
		for (x = -1; x <= 1; x++)
		{
			for (y = -1; y <= 1; y++)
			{
				/* skip if on same location */
				if (x == 0 && y == 0)
					continue;
				i2 = x + i;
				j2 = y + j;
				if (outOfBounds(i2, j2))
					continue;
				if (board[i2][j2] == oppColor(color) && !outOfBounds(i2 + x, j2 + y))
				{
					/* immedaite neighbor is oppColr */ 
					while (!outOfBounds(i2 + x, j2 + y))
					{
						i2 += x;
						j2 += y;
						if (board[i2][j2] == color || board[i2][j2] == OPEN)
							break;
					}
					if (board[i2][j2] == color)
					{
						/* go back and flip colors */
						while (i != i2 || j != j2)
						{
							board[i2][j2] = color;
							i2 -= x;
							j2 -= y;
						}
					}
				}			
			}
		}
		/* mark the places location */
		board[i][j] = color;
	}
}

int anyValid(int color, int board[SIZE][SIZE])
{
	int i,j;
	
	for (i = 0; i < SIZE; i++)
	{
		for (j = 0; j < SIZE; j++)
		{
			if (board[i][j] != OPEN)
				continue;
			if (validMove(i, j, color, board))
				return TRUE;
		}
	}
	return FALSE;
}
int boardFull(int board[SIZE][SIZE])
{
	int i,j;
		
		for (i = 0; i < SIZE; i++)
		{
			for (j = 0; j < SIZE; j++)
			{
				if (board[i][j] == OPEN)
					return FALSE;
			}
		}
		return TRUE;
}

int getScore(int board[SIZE][SIZE])
{
	int i,j;
	int score = 0;
		
	for (i = 0; i < SIZE; i++)
	{
		for (j = 0; j < SIZE; j++)
		{
			if (board[i][j] == X)
				score++;
			if (board[i][j] == O)
				score--;
		}
	}
	return score;
}


/*int main(int argc, char *argv[]) 
{
	int b[8][8];
	int i,j,c;
	int x,y,s;
	int p = X;
	int repeat = FALSE;
	//fill board with 0's
	for(i=0;i<8;i++)
	for(j=0;j<8;j++)
		b[i][j]=OPEN;
	//initialize positions
	b[4][4]=O;
	b[3][3]=O;
	b[4][3]=X; 
	b[3][4]=X;
	
	printf("Intial game state: \n");
	printKey("First", "Second");
	printBoard(b);
	
	printf("Turn: %d\n",p);
	while ((c = getchar()) != EOF)
	{
		y = letterToNum(c);
		c = getchar();
		x = letterToNum(c);
		if (validMove(x, y, p, b))
		{
			printf("%d %d\n",x,y);
			flip(x, y, p, b);
			printBoard(b);
			p = oppColor(p);
		}
		else
		{
			printf("");
			//printf("illegal move\n");
			c = getchar();
			continue;
		}		
		c = getchar();
		if (!anyValid(p, b))
		{
			p = oppColor(p);
		}
		if (!anyValid(X, b) && !anyValid(O, b))
			break;
		printf("Turn: %d\n",p);	
	}
	s = getScore(b);
	if (s < 0)
		printf("Score: %d Winner: %d\n", s, X);
	else if (s > 0)
		printf("Score: %d Winner: %d\n", s, O);
	else
		printf("Score: %d Draw\n", s);
	return 0;
} */
 
