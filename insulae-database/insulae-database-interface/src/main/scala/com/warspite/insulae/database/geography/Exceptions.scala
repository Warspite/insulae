package com.warspite.insulae.database.geography
import com.warspite.insulae.database.InsulaeDatabaseException

class AreaException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class AreaIdDoesNotExistException(id: Int) extends AreaException("No area with id " + id + " exists in database.") {}
