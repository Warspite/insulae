package com.warspite.insulae.database

import com.warspite.common.database.DatabaseException

class InsulaeDatabaseException(msg: String, inner: Throwable) extends DatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}
