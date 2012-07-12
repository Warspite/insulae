package com.warspite.insulae.database.account
import com.warspite.common.database.DataRecord

object Account {
  val fields = List("id", "email", "passwordHash", "callSign", "givenName", "surname");

  def apply(r: DataRecord) = {
    new Account(
      id = r.get[Int]("id"),
      email = r.get[String]("email"),
      passwordHash = r.get[String]("passwordHash"),
      callSign = r.get[String]("callSign"),
      givenName = r.get[String]("givenName"),
      surname = r.get[String]("surname"))
  }

  def apply(a: Account) = {
    new Account(
      id = a.id,
      email = a.email,
      passwordHash = a.passwordHash,
      callSign = a.callSign,
      givenName = a.givenName,
      surname = a.surname)
  }
}

class Account(var id: Int, var email: String, var passwordHash: String, var callSign: String, var givenName: String, var surname: String) {
  def asMap(includeId: Boolean, includePasswordHash: Boolean): Map[String, Any] = {
    var map = Map(
      "id" -> id,
      "email" -> email,
      "givenName" -> givenName,
      "surname" -> surname,
      "callSign" -> callSign)

    if (includeId)
      map += "id" -> id;

    if (includePasswordHash)
      map += "passwordHash" -> passwordHash;

    return map
  }
}

