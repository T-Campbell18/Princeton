#Macros
CC = gcc
# CC = gcc217m
CCFLAGS = -g

all: testsymtablelist testsymtablehash buzzlist buzzhash 

clean: rm testsymtablehash testsymtablelist buzzlist buzzhash *.o

testsymtablelist: testsymtable.o symtablelist.o
	$(CC) $(CCFLAGS) $< symtablelist.o -o $@

testsymtablehash: testsymtable.o symtablehash.o
	$(CC) $(CCFLAGS) $< symtablehash.o -o $@
	
buzzlist: buzz.o symtablelist.o
	$(CC) $(CCFLAGS) $< symtablelist.o -o $@	

buzzhash: buzz.o symtablehash.o
	$(CC) $(CCFLAGS) $< symtablehash.o -o $@		

buzz.o: symtable.h
testsymtable.o: symtable.h
symtablelist.o: symtable.h
symtablehash.o: symtable.h