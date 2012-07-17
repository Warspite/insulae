package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object LocationType extends StoredType {
  val fields = List("id", "name", "description");

  def apply(r: DataRecord) = {
    new LocationType(
      id = r.get[Int]("id"),
      name = r.get[String]("name"),
      description = r.get[String]("description"))
  }

  def apply(a: LocationType) = {
    new LocationType(
      id = a.id,
      name = a.name,
      description = a.description)
  }
}

class LocationType(var id: Int, var name: String, var description: String) extends Mappable {
  def asMap(includeId: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "description" -> description);

    if (includeId)
      map += "id" -> id;

    return map
  }
}

