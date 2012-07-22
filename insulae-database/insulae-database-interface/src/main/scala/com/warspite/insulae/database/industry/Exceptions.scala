package com.warspite.insulae.database.industry
import com.warspite.insulae.database.InsulaeDatabaseException

class BuildingTypeException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class BuildingTypeIdDoesNotExistException(id: Int) extends BuildingTypeException("No building type with id " + id + " exists in database.") {}

class BuildingException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class BuildingAtLocationIdDoesNotExistException(locationId: Int) extends BuildingException("No building with with locationId " + locationId + " exists in database.") {}
class BuildingAtLocationIdAlreadyExistsException(locationId: Int) extends BuildingException("There is already a building at location " + locationId + ".") {}
class BuildingIdDoesNotExistException(id: Int) extends BuildingException("No building with id " + id + " exists in database.") {}

class ItemTypeException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class ItemTypeIdDoesNotExistException(id: Int) extends BuildingTypeException("No item type with id " + id + " exists in database.") {}

class ItemStorageException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
