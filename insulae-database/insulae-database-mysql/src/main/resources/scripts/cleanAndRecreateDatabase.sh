DATABASE_CONFIGURATION_FILE=configuration/database.properties
. $DATABASE_CONFIGURATION_FILE

mysql -B -u$db_user -h$db_host -P$db_port -p$db_pass < scripts/sql/create-database.sql
retcode=$?

if [ $retcode != 0 ]; then
	echo "Failed to clean and recreate database."
	exit 1
fi
