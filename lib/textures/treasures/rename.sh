#!/bin/bash - 
#===============================================================================
#
#          FILE: rename.sh
# 
#         USAGE: ./rename.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: Dr. Fritz Mehner (fgm), mehner.fritz@fh-swf.de
#  ORGANIZATION: FH Südwestfalen, Iserlohn, Germany
#       CREATED: 05/03/2015 19:29
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error

a=0
for i in *.png; do
	new=$(printf "tr%02d.png" "$a") #04 pad to length of 4
  mv -- "$i" "$new"
  let a=a+1
done
