#!/bin/bash

. scripts/db/lib/mysqlBatch.sh

echo "DROP DATABASE IF EXISTS $db_name; CREATE DATABASE $db_name;" | $mysqlBatch
retcode=$?

if [ $retcode != 0 ]; then
	echo "Failed to wipe and recreate database schema."
	exit 1
fi
