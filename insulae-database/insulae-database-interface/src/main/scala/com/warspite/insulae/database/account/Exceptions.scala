package com.warspite.insulae.database.account
import com.warspite.insulae.database.InsulaeDatabaseException

class AccountException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class AccountIdDoesNotExistException(id: Int) extends AccountException("No account with id " + id + " exists in database.") {}

class AccountEmailDoesNotExistException(email: String) extends AccountException("No account with email " + email + " exists in database.") {}
class AccountEmailAlreadyExistsException(email: String) extends AccountException("An account with email " + email + " already exists in database.") {}

class AccountCallSignDoesNotExistException(callSign: String) extends AccountException("No account with callSign " + callSign + " exists in database.") {}
class AccountCallSignAlreadyExistsException(callSign: String) extends AccountException("An account with callSign " + callSign + " already exists in database.") {}
