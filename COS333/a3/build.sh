#!/usr/bin/env bash

#-----------------------------------------------------------------------
# build
# Author: Bob Dondero
#-----------------------------------------------------------------------

cp=
for jarfile in ~/Desktop/COS333/lib/spark/*.jar; do
	cp=$cp:$jarfile
done

javac -cp $cp "$@"

