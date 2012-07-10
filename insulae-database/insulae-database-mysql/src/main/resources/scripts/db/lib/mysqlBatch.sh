DATABASE_CONFIGURATION_FILE=configuration/database.properties
. $DATABASE_CONFIGURATION_FILE

mysqlBatch="mysql -B -u$db_user -h$db_host -P$db_port -D$db_name -p$db_pass --skip-column-names"