package com.warspite.insulae.account.database

import com.warspite.common.database.DatabaseException

class AccountException(msg: String, inner: Throwable) extends DatabaseException(msg, inner) {
  def this(msg: String) = this(msg, null);
}

class AccountIdDoesNotExistException(id: Int) extends AccountException("No account with id " + id + " exists in database.") {}
class AccountEmailDoesNotExistException(email: String) extends AccountException("No account with email " + email + " exists in database.") {}