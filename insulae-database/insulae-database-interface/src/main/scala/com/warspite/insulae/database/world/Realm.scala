package com.warspite.insulae.database.world

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object Realm {
  val fields = List("name", "startDate", "endDate") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Realm(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String]("name"),
      startDate = r.get[DateTime]("startDate"),
      endDate = r.get[DateTime]("endDate"))
  }

  def apply(a: Realm) = {
    new Realm(
      id = a.id,
      name = a.name,
      startDate = a.startDate,
      endDate = a.endDate)
  }
}

class Realm(id: Int, var name: String, var startDate: DateTime, var endDate: DateTime) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    
    var map = Map[String, Any](
      "name" -> name,
      "startDate" -> startDate,
      "endDate" -> endDate)

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

