package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object Area {
  val fields = List("name", "coordinatesX", "coordinatesY", "realmId", "areaTypeId") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Area(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String]("name"),
      coordinatesX = r.get[Int]("coordinatesX"),
      coordinatesY = r.get[Int]("coordinatesY"),
      realmId = r.get[Int]("realmId"),
      areaTypeId = r.get[Int]("areaTypeId"))
  }

  def apply(a: Area) = {
    new Area(
      id = a.id,
      name = a.name,
      coordinatesX = a.coordinatesX,
      coordinatesY = a.coordinatesY,
      realmId = a.realmId,
      areaTypeId = a.areaTypeId);
  }
}

class Area(id: Int, var name: String, var coordinatesX: Int, var coordinatesY: Int, var realmId: Int, var areaTypeId: Int) extends IdentifiedType(id) {

  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "coordinatesX" -> coordinatesX,
      "coordinatesY" -> coordinatesY,
      "realmId" -> realmId,
      "areaTypeId" -> areaTypeId);

    return map ++ super.asMap();
  }
}

