package com.warspite.insulae.database.account
import com.warspite.common.database.TableRow

object Account {
  def apply(r: TableRow) = {
    new Account(r.get[Int]("id"), r.get[String]("email"), r.get[String]("passwordHash"), r.get[String]("callSign"), r.get[String]("givenName"), r.get[String]("surname"));
  }
}

class Account(var id: Int, var email: String, var passwordHash: String, var callSign: String, var givenName: String, var surname: String) {
}

