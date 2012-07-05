package com.warspite.insulae.database
import java.sql.Connection
import scala.collection.mutable.Queue
import com.warspite.common.database.Table
import java.sql.SQLException
import com.warspite.common.database.UnexpectedNumberOfRowsException
import org.slf4j.LoggerFactory
import com.warspite.common.database.TableRow

class MySqlQueryer(val connection: Connection) {
  protected val log = LoggerFactory.getLogger(getClass());
  
  def query(columnNames: List[String], q: String, expectedNum: java.lang.Integer = null): Table = {
    val query = "SELECT " + columnNames.reduceLeft(_ + ", " + _) + " " + q;
    try {
      val rs = connection.createStatement().executeQuery(query);
      val table = new Table;

      log.debug("Reading resultset returned by query '" + query + "'.");
      while (rs.next) {
    	  log.debug("Yay, entered a row!");
        var row = new TableRow;
        for (i <- 1 to columnNames.length) {
          row.addCell(columnNames(i - 1), rs.getObject(i));
        }
        table.addRow(row);
    	  log.debug("Added row to table, it now has " + table.size + " rows.");
      }

      if (expectedNum != null && expectedNum != table.size)
        throw new UnexpectedNumberOfRowsException(expectedNum, table.size, query);

      return table;
    } catch {
      case e: SQLException => throw new QueryFailedException(query, e);
    }
  }
}
