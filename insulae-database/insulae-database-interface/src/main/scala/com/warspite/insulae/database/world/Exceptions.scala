package com.warspite.insulae.database.world
import com.warspite.insulae.database.InsulaeDatabaseException
import com.warspite.common.database.ExpectedRecordNotFoundException

class AvatarException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class AvatarNameDoesNotExistException(name: String) extends AvatarException("No avatar with name " + name + " exists in database.") {}
class AvatarNameAlreadyExistsException(name: String, realmId: Int) extends AvatarException("An avatar with name '" + name + "' already exists in realm " + realmId + ".") {}
class AvatarDataInconsistentException(badField1: String, badField2: String) extends AvatarException("Avatar internal data contained inconsistencies. These fields mismatch: " + badField1 + ", " + badField2 + ".") {}

class AvatarIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No avatar with id " + id + " exists in database.") {}
class SexIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No sex with id " + id + " exists in database.") {}
class RaceIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No race with id " + id + " exists in database.") {}
