package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object Area {
  val fields = List("name", "description", "coordinatesX", "coordinatesY", "realmId") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Area(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String]("name"),
      description = r.get[String]("description"),
      coordinatesX = r.get[Int]("coordinatesX"),
      coordinatesY = r.get[Int]("coordinatesY"),
      realmId = r.get[Int]("realmId"))
  }

  def apply(a: Area) = {
    new Area(
      id = a.id,
      name = a.name,
      description = a.description,
      coordinatesX = a.coordinatesX,
      coordinatesY = a.coordinatesY,
      realmId = a.realmId)
  }
}

class Area(id: Int, var name: String, var description: String, val coordinatesX: Int, val coordinatesY: Int, val realmId: Int) extends IdentifiedType(id) {

  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "description" -> description,
      "coordinatesX" -> coordinatesX,
      "coordinatesY" -> coordinatesY,
      "realmId" -> realmId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

