package com.warspite.insulae.database.account
import com.warspite.insulae.database.InsulaeDatabaseException

class AccountException(msg: String, inner: Throwable) extends InsulaeDatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class AccountIdDoesNotExistException(id: Int) extends AccountException("No account with id " + id + " exists in database.") {}
class AccountEmailDoesNotExistException(email: String) extends AccountException("No account with email " + email + " exists in database.") {}