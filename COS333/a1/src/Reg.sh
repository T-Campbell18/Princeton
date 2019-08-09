#!/usr/bin/env bash

classpath=.
for jarfile in ~/lib/sqlite/*.jar; do
   classpath=$classpath:$jarfile
done

java -cp $classpath "$@"