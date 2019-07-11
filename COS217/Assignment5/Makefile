#-----------------------------------------------------------------------
# Makefile
# Authors: Andrew Appel and Bob Dondero
#-----------------------------------------------------------------------

all: grader dataC dataB dataA dataAplus

clean:
	rm -f createdataC createdataB createdataA createdataAplus \
	dataC dataB dataA dataAplus grader

grader: grader.c
	gcc217 -O -static grader.c -o grader

dataC: createdataC
	./createdataC

dataB: createdataB
	./createdataB

dataA: createdataA
	./createdataA

dataAplus: createdataAplus
	./createdataAplus

createdataC: createdataC.c
	gcc createdataC.c -o createdataC

createdataB: createdataB.c
	gcc createdataB.c -o createdataB

createdataA: createdataA.c
	gcc createdataA.c -o createdataA

createdataAplus: createdataAplus.c
	gcc createdataAplus.c -o createdataAplus
