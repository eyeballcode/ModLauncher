#!/usr/bin/env bash
echo -n "Commit Message: "
MESSAGE=`read`
echo "Commit: $MESSAGE"
gradle genJava
git add .
git commit -m $MESSAGE