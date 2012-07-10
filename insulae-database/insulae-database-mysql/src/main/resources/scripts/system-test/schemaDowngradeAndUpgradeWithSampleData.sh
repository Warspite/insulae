#!/bin/bash

execScript() {
	eval $1
	retcode=$?

	if [ $retcode != 0 ]; then
		exit 1
	fi
}

execScript "scripts/db/wipeDatabase.sh"
execScript "eval scripts/db/grade.sh up-sample"
execScript "scripts/db/grade.sh down 0"
execScript "scripts/db/grade.sh up-sample"
