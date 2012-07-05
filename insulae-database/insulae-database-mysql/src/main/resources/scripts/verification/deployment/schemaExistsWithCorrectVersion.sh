. scripts/lib/mysqlBatch.sh

schemaVersion=$(echo "SELECT SchemaVersion FROM SchemaVersion" | $mysqlBatch)
retcode=$?

if [ $retcode != 0 ]; then
	echo "Failed to connect to MySQL using properties in $DATABASE_CONFIGURATION_FILE"
	exit 1
fi

if [ $schemaVersion != "@schema_version@" ]; then
	echo "Incorrect database version. Expected @schema_version@, but found $schemaVersion."
	exit 1
fi