#! /bin/bash

TESTDIR='testing'
WHERE=`dirname $0`

for dirs in $(ls $TESTDIR | grep Exp)
do
	for program in $(ls $TESTDIR/$dirs)
	do
		echo "$(tput setaf 2)Testing: $program$(tput sgr0)"
		./RUNCOMPILER.sh $TESTDIR/$dirs/$program
		echo
	done
done
