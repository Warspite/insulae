#!/bin/bash

. scripts/db/lib/mysqlBatch.sh

mysql -B -u$db_user -h$db_host -P$db_port -p$db_pass -eexit
retcode=$?

if [ $retcode != 0 ]; then
	echo "Failed to connect to MySQL using properties in $DATABASE_CONFIGURATION_FILE"
	exit 1
fi