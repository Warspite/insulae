package com.warspite.insulae.database
import com.warspite.insulae.database.account.MySqlInsulaeAccountDatabase
import java.util.Properties
import java.sql.DriverManager
import java.sql.Connection
import com.warspite.common.database.DatabaseCreationException

class MySqlInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
  def account = accountDb;
  
  var accountDb: MySqlInsulaeAccountDatabase = null;
  var connection: Connection = null;

  def connect() {
    try {
      log.debug("Attempting to load JDBC Driver class.");
      Class.forName("com.mysql.jdbc.Driver");
      log.debug("Attempting to create a connection.");
      connection = DriverManager.getConnection("jdbc:mysql://localhost/" + props.getProperty("db_name") + "?user=" + props.getProperty("db_user") + "&password=" + props.getProperty("db_pass"));
      log.debug("MySQL connection established.")
    } catch {
      case e => throw new DatabaseCreationException("Failed to connect to database.", e);
    }
    
    accountDb = new MySqlInsulaeAccountDatabase(connection);
  }
}
