package com.warspite.insulae.database.world

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object Sex {
  val fields = List("raceId", "name", "title", "description") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Sex(
      id = r.get[Int](IdentifiedType.ID),
      raceId = r.get[Int]("raceId"),
      name = r.get[String]("name"),
      title = r.get[String]("title"),
      description = r.get[String]("description"))
  }

  def apply(a: Sex) = {
    new Sex(
      id = a.id,
      raceId = a.raceId,
      name = a.name,
      title = a.title,
      description = a.description)
  }
}

class Sex(id: Int, var raceId: Int, var name: String, var title: String, var description: String) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {

    var map = Map[String, Any](
      "raceId" -> raceId,
      "name" -> name,
      "title" -> title,
      "description" -> description)

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

