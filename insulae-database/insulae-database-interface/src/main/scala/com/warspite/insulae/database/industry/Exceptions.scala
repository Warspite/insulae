package com.warspite.insulae.database.industry
import com.warspite.insulae.database.InsulaeDatabaseException
import com.warspite.common.database.ExpectedRecordNotFoundException

class BuildingTypeException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }

class BuildingException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class BuildingAtLocationIdAlreadyExistsException(locationId: Int) extends BuildingException("There is already a building at location " + locationId + ".") {}

class ItemStorageException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class ActionPointTickException(msg: String) extends InsulaeDatabaseException(msg);