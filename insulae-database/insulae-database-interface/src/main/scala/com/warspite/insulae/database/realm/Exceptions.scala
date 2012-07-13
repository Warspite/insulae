package com.warspite.insulae.database.realm
import com.warspite.insulae.database.InsulaeDatabaseException

class RealmException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class RealmIdDoesNotExistException(id: Int) extends RealmException("No realm with id " + id + " exists in database.") {}
