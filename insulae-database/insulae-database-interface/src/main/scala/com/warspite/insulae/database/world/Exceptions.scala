package com.warspite.insulae.database.world
import com.warspite.insulae.database.InsulaeDatabaseException

class RealmException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class RealmIdDoesNotExistException(id: Int) extends RealmException("No realm with id " + id + " exists in database.") {}

class RaceException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class RaceIdDoesNotExistException(id: Int) extends RaceException("No race with id " + id + " exists in database.") {}

class SexException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class SexIdDoesNotExistException(id: Int) extends SexException("No sex with id " + id + " exists in database.") {}

class AvatarException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) { def this(msg: String) = this(msg, null); }
class AvatarIdDoesNotExistException(id: Int) extends AvatarException("No avatar with id " + id + " exists in database.") {}
class AvatarNameDoesNotExistException(name: String) extends AvatarException("No avatar with name " + name + " exists in database.") {}
class AvatarNameAlreadyExistsException(name: String, realmId: Int) extends AvatarException("An avatar with name '" + name + "' already exists in realm " + realmId + ".") {}
class AvatarDataInconsistentException(badField1: String, badField2: String) extends AvatarException("Avatar internal data contained inconsistencies. These fields mismatch: " + badField1 + ", " + badField2 + ".") {}
