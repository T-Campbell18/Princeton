
#include <stdio.h>

enum {FALSE, TRUE};
enum {OPEN, X, O};
void printBoard(int brd[8][8]);
int validMove(int i, int j, int color, int brd[8][8]);
int letterToNum(char c);
int oppColor(int color);
void flip(int i, int j, int color, int brd[8][8]);
int anyValid(int color, int brd[8][8]);
int getScore(int brd[8][8]);

void printBoard(int brd[8][8])
{
	int i,j;
	char tmp;
	printf("FIRST = x, SECOND = o\n\n");

	printf("  A B C D E F G H\n");
	for (i = 0; i < 8; i++)
	{
		printf("%d", i);
		for (j = 0; j < 8; j++)
		{
			if (brd[i][j] == X)
				tmp = 'x';
			else if(brd[i][j] == O)
				tmp = 'o';
			else
				tmp = '.';
			printf(" %c", tmp);
		}
		printf("\n");
	}
}
int outOfBounds(int i, int j)
{
	if (i < 0 || i >= 8)
		return TRUE;
	if (j < 0 || j >= 8)
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
	}
	return -1;	
}
int validMove(int i, int j, int color, int brd[8][8])
{
	int x, y;
	int i2, j2;
	if (outOfBounds(i, j))
		return FALSE;
	if (brd[i][j] == 0)
	{
		for (x = -1; x <= 1; x++)
		{
			for (y = -1; y <= 1; y++)
			{
				if (x == 0 && y == 0)
					continue;
				i2 = x + i;
				j2 = y + j;
				if (outOfBounds(i2, j2))
					continue;
				if (brd[i2][j2] == 0 || brd[i2][j2] == color)
					continue; 
				while (TRUE)
				{
					i2 += x;
					j2 += y;
					if (outOfBounds(i2, j2) || brd[i2][j2] == 0)
						break;
					if (brd[i2][j2] == color)
						return TRUE;	
				}			
			}
		}
	}
	return FALSE;
}
int oppColor(int color)
{
	if (color == X)
		return O;
	return X;	
		
}
void flip2(int i, int j, int color, int brd[8][8])
{
	int x, y;
	int i2, j2;
	if (outOfBounds(i, j))
		return;
	if (brd[i][j] == 0)
	{
		for (x = -1; x <= 1; x++)
		{
			for (y = -1; y <= 1; y++)
			{
				if (x == 0 && y == 0)
					continue;
				i2 = x + i;
				j2 = y + j;
				if (outOfBounds(i2, j2))
					continue;
				if (brd[i2][j2] == 0 || brd[i2][j2] == color)
					continue; 
				while (TRUE)
				{
					i2 += x;
					j2 += y;
					if (outOfBounds(i2, j2) || brd[i2][j2] == 0)
						break;
					if (brd[i2][j2] == color)
						break;	
				}
				while (i != i2 || j != j2)
				{
					brd[i2][j2] = color;
					i2 -= x;
					j2 -= y;
				}			
			}
		}
		brd[i][j] = color;
	}
}
void flip(int i, int j, int color, int brd[8][8])
{
	int x, y;
	int i2, j2;
	if (outOfBounds(i, j))
		return;
	if (brd[i][j] == 0)
	{
		for (x = -1; x <= 1; x++)
		{
			for (y = -1; y <= 1; y++)
			{
				if (x == 0 && y == 0)
					continue;
				i2 = x + i;
				j2 = y + j;
				if (outOfBounds(i2, j2))
					continue;
				if (brd[i2][j2] == oppColor(color) && !outOfBounds(i2+x, j2+y))
				{
					while (!outOfBounds(i2, j2))
					{
						if (brd[i2][j2] == color || brd[i2][j2] == 0)
							break;
						i2 += x;
						j2 += y;
						
					}
					if (brd[i2][j2] == color)
					{
						while (i != i2 || j != j2)
						{
							brd[i2][j2] = color;
							i2 -= x;
							j2 -= y;
						}
					}
				}			
			}
		}
		brd[i][j] = color;
	}
}

int anyValid(int color, int brd[8][8])
{
	int i,j;
	
	for (i = 0; i < 8; i++)
	{
		for (j = 0; j < 8; j++)
		{
			if (brd[i][j] != 0)
				continue;
			if (validMove(i, j, color, brd))
				return TRUE;
		}
	}
	return FALSE;
}

int getScore(int brd[8][8])
{
	int i,j;
	int score = 0;
		
	for (i = 0; i < 8; i++)
	{
		for (j = 0; j < 8; j++)
		{
			if (brd[i][j] == 1)
				score++;
			if (brd[i][j] == 2)
				score--;
		}
	}
	return score;
}


int main(int argc, char *argv[]) 
{
	int b[8][8];
	char m[8] ="";
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

	printBoard(b);
	
	printf("Turn: %d\n",p);
	while ((c = getchar()) != EOF)
	{
		m[0] =c;
		y = letterToNum(c);
		c = getchar();
		m[1] =c;
		x = letterToNum(c);
		if (validMove(x, y, p, b))
		{
			printf("Move: %s\n",m);	
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
		{
			printf("LOLOLOLOL");
			break;
		}
	}
	s = getScore(b);
	if (s > 0)
		printf("Score: %d Winner: %d\n", s, X);
	else if (s < 0)
		printf("Score: %d Winner: %d\n", s, O);
	else
		printf("Score: %d Draw\n", s);
	return 0;
} 
 