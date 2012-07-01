DATABASE_CONFIGURATION_FILE=configuration/database.properties
. $DATABASE_CONFIGURATION_FILE

schemaVersion=$(echo "SELECT Version FROM Version" | mysql -B -u$db_user -h$db_host -P$db_port -D$db_name -p$db_pass --skip-column-names)
retcode=$?

if [ $retcode != 0 ]; then
	echo "Failed to connect to MySQL using properties in $DATABASE_CONFIGURATION_FILE"
	exit 1
fi

if [ $schemaVersion != "@insulae_version@" ]; then
	echo "Incorrect database version. Expected @insulae_version@, but found $schemaVersion."
	exit 1
fi