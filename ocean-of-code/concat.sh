#!/usr/bin/env bash

SRC_FOLDER=src/main/java
GENERATED_FOLDER=target
TEMPORARY_FILENAME=concat

# Clean temporary files
rm output $TEMPORARY_FILENAME

cp $SRC_FOLDER/Player.java output
for i in $(find src/main/java -name "*.java" ! -name 'Player.java'); do cat "$i" >> $TEMPORARY_FILENAME ; done
#// Concat other classes here

sed -i 's/old-text/$(cat concat)/g' output