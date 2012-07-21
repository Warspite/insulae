package com.warspite.insulae.database.geography
import com.warspite.insulae.database.InsulaeDatabaseException

class AreaException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class AreaIdDoesNotExistException(id: Int) extends AreaException("No area with id " + id + " exists in database.") {}

class LocationException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class LocationIdDoesNotExistException(id: Int) extends LocationException("No location with id " + id + " exists in database.") {}
