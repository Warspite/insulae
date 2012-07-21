package com.warspite.insulae.database

import com.warspite.insulae.database.account.MySqlInsulaeAccountDatabase
import com.warspite.insulae.database.world.MySqlInsulaeWorldDatabase
import com.warspite.insulae.database.geography.MySqlInsulaeGeographyDatabase
import com.warspite.insulae.database.industry.MySqlInsulaeIndustryDatabase
import java.util.Properties
import java.sql.DriverManager
import java.sql.Connection
import com.warspite.common.database.DatabaseCreationException

class MySqlInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
  def account = accountDb;
  def world = worldDb;
  def geography = geographyDb;
  def industry = industryDb;
  
  var accountDb: MySqlInsulaeAccountDatabase = null;
  var worldDb: MySqlInsulaeWorldDatabase = null;
  var geographyDb: MySqlInsulaeGeographyDatabase = null;
  var industryDb: MySqlInsulaeIndustryDatabase = null;
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
    worldDb = new MySqlInsulaeWorldDatabase(connection);
    geographyDb = new MySqlInsulaeGeographyDatabase(connection);
    industryDb = new MySqlInsulaeIndustryDatabase(connection);
  }
}
