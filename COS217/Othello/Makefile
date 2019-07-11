#---------------------------------------------------------------------
# Makefile
# Author: Tyler Campbell
#---------------------------------------------------------------------

all: referee

clean:
	rm -f referee *.o

referee: referee.o displaygame.o
	gcc217 referee.o displaygame.o -o referee
	
referee.o: referee.c displaygame.h
	gcc217 -c referee.c

displaygame.o: displaygame.c displaygame.h
	gcc217 -c displaygame.c
