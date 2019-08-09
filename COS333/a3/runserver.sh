#!/usr/bin/env bash

#-----------------------------------------------------------------------
# runserver
# Author: Bob Dondero
#-----------------------------------------------------------------------

cp=.
for jarfile in ~/Desktop/COS333/lib/spark/*.jar ~/Desktop/COS333/lib/sqlite/*.jar; do
	cp=$cp:$jarfile
done

java -cp $cp Web "$@"