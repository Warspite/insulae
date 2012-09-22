package com.warspite.insulae.database.world

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.IdentifiedType

object Avatar {
  val fields = IdentifiedType.fields ++ List("accountId", "realmId", "raceId", "sexId", "name")

  def apply(r: DataRecord) = {
    new Avatar(
      id = r.get[Int](IdentifiedType.ID),
      accountId = r.get[Int]("accountId"),
      realmId = r.get[Int]("realmId"),
      raceId = r.get[Int]("raceId"),
      sexId = r.get[Int]("sexId"),
      name = r.get[String]("name"))
  }

  def apply(a: Avatar) = {
    new Avatar(
      id = a.id,
      accountId = a.accountId,
      realmId = a.realmId,
      raceId = a.raceId,
      sexId = a.sexId,
      name = a.name)
  }
}

class Avatar(id: Int, var accountId: Int, var realmId: Int, var raceId: Int, var sexId: Int, var name: String) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "realmId" -> realmId,
      "raceId" -> raceId,
      "sexId" -> sexId,
      "name" -> name);

    if (includeSensitiveInformation)
      map += "accountId" -> accountId;
      
    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

