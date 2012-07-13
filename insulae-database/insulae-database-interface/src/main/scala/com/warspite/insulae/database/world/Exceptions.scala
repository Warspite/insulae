package com.warspite.insulae.database.world
import com.warspite.insulae.database.InsulaeDatabaseException

class RealmException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class RealmIdDoesNotExistException(id: Int) extends RealmException("No realm with id " + id + " exists in database.") {}



class RaceException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class RaceIdDoesNotExistException(id: Int) extends RaceException("No race with id " + id + " exists in database.") {}
