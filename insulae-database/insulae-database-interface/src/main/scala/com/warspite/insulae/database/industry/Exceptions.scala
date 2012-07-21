package com.warspite.insulae.database.industry
import com.warspite.insulae.database.InsulaeDatabaseException

class BuildingTypeException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class BuildingTypeIdDoesNotExistException(id: Int) extends BuildingTypeException("No building type with id " + id + " exists in database.") {}

class BuildingException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class BuildingIdDoesNotExistException(id: Int) extends BuildingException("No building with id " + id + " exists in database.") {}
