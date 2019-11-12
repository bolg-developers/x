#!/bin/sh
git branch --merged | grep -v '*' | xargs -I % git branch -d %