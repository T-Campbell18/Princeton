/*--------------------------------------------------------------------*/
/* displaygame.h                                                      */
/* Author: Tyler Campbell                                             */
/*--------------------------------------------------------------------*/
#ifndef DISPLAYGAME
#define DISPLAYGAME

#include <stdio.h>

/* prints representation of board to the file pointer fp */
void printBoard(int board[8][8], FILE *fp);

/* returns TRUE if move at i, j is valid for color on board */
int validMove(int i, int j, int color, int board[8][8]);

/* returns the int value of char on the board */
int letterToNum(char c);

/* returns the oppostion color (tile) */
int oppColor(int color);

/* make flips of move i, j by color on board */
void flip(int i, int j, int color, int board[8][8]);

/* returns TRUE if there are any valid moves on board for color */
int anyValid(int color, int board[8][8]);

/* returns score for board */
int getScore(int board[8][8]);

/* returns TRUE if the board is full */
int boardFull(int board[8][8]);

#endif