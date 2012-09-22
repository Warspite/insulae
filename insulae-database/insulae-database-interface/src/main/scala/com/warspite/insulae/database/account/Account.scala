package com.warspite.insulae.database.account
import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object Account {
  val fields = List("email", "passwordHash", "callSign", "givenName", "surname") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Account(
      id = r.get[Int](IdentifiedType.ID),
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

class Account(id: Int, var email: String, var passwordHash: String, var callSign: String, var givenName: String, var surname: String) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "email" -> email,
      "givenName" -> givenName,
      "surname" -> surname,
      "callSign" -> callSign)

    if (includeSensitiveInformation)
      map += "passwordHash" -> passwordHash;

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
  
  def asDataRecord(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): DataRecord = DataRecord(this.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation));
}

