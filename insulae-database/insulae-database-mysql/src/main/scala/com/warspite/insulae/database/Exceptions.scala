package com.warspite.insulae.database
import com.warspite.common.database.DatabaseException

class MysqlException(msg: String, inner: Throwable) extends DatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class QueryFailedException(query: String, inner: Throwable) extends MysqlException("Failed to execute query '" + query + "'.", inner) {}
