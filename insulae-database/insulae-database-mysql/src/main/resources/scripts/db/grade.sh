#!/bin/bash

. scripts/db/lib/mysqlBatch.sh

printUsage() {
	echo "====Script usage:===="
	echo "grade.sh <verb> <targetVersion>"
	echo ""
	echo "verb can be either up, up-sample or down."
	echo "up-sample will, for each step, populate the database with any available sample data."
	echo ""
	echo "targetVersion must be a positive integer."
	echo "If omitted it will default to the highest available version."
}

validateVersion() {
	if [ -z "$targetVersion" ]; then
		targetVersion=@schema_version@
		echo "tagetVersion not specified. Defaulting to $targetVersion."
	fi
	
	case "$targetVersion" in
		''|*[!0-9]*) 
			echo "Invalid argument: targetVersion. '$targetVersion' is not numerical."
			printUsage
			exit 1 ;;
	esac
}

executeScript() {
	echo "  - Executing $1"
	eval $mysqlBatch < $1
	retcode=$?
	
	if [ $retcode != 0 ]; then
		echo ""
		echo "=====SCRIPT EXECUTION FAILED!====="
		echo "An error occurred while executing $1."
		echo "Not sure how to recover from this. I'll just shut down until I think of something..."
		exit 1
	fi
}

stepVersion() {
	echo ""
	
	executeScript "scripts/db/sql/$1-$verb.sql" 
	
	if $insertSampleData ; then
		executeScript "scripts/db/sql/$1-sample.sql" 
	fi

	let newVersion=$1
	if [ $verb == "down" ]; then
		let newVersion=$1-1
	fi
	
	$(echo "UPDATE SchemaVersion SET SchemaVersion=$newVersion" | $mysqlBatch 2>/dev/null)
	
}

parseVerb() {
	case $1 in
		"up")
			verb="up"
			insertSampleData=false;;
		"up-sample")
			verb="up"
			insertSampleData=true;;
		"down")
			verb="down"
			insertSampleData=false;;
		*)
			echo "Unrecognized verb '$verb'."
			printUsage
			exit 1;;
	esac
}

actOnVerb() {
	case $verb in
		"up")
			if [ $currentVersion -ge $targetVersion ]; then
				echo "Cannot upgrade any further from current version $currentVersion to target version $targetVersion."
			else
				echo "Will upgrade current version $currentVersion to target version $targetVersion."
			fi
			
			let startVersion=$currentVersion+1
			let stopVersion=$targetVersion;;
		"down")
			if [ $targetVersion -ge @schema_version@ ]; then
				echo "Cannot downgrade current maximum version of @schema_version@ to target version $targetVersion."
			else
				echo "Will downgrade from maximum version @schema_version@ to target version $targetVersion."
			fi

			let startVersion=@schema_version@
			let stopVersion=$targetVersion+1;;
	esac
	
	if [ $verb == "down" ]; then
		for (( v=$startVersion; v>=$stopVersion; v-- ))
		do
			stepVersion $v
		done
	else
		for (( v=$startVersion; v<=$stopVersion; v++ ))
		do
			stepVersion $v
		done
	fi
}

determineCurrentVersion() {
	currentVersion=$(echo "SELECT SchemaVersion FROM SchemaVersion" | $mysqlBatch 2>/dev/null)
	retcode=$?
	
	if [ $retcode != 0 ]; then
		echo "Couldn't determine the database version."
		echo "I will assume that this depends on a completely blank schema, i.e. version 0."
		echo "If the schema doesn't exist at all this will have to be created, using e.g. wipeDatabase.sh."
		currentVersion=0
	fi
}

targetVersion=$2

parseVerb $1
validateVersion
determineCurrentVersion
actOnVerb
exit 0