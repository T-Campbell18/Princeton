#!/usr/bin/env bash
cp=.
for jarfile in ~../lib/sqlite/*.jar; do
   cp=$cp:$jarfile
done
java -cp $cp RegDetails $@