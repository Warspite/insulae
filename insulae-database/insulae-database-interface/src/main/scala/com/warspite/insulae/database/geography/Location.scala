package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object Location {
  val fields = List("locationTypeId", "areaId", "coordinatesX", "coordinatesY", "road") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Location(
      id = r.get[Int](IdentifiedType.ID),
      locationTypeId = r.get[Int]("locationTypeId"),
      areaId = r.get[Int]("areaId"),
      coordinatesX = r.get[Int]("coordinatesX"),
      coordinatesY = r.get[Int]("coordinatesY"),
      road = r.get[Boolean]("road"))
  }

  def apply(a: Location) = {
    new Location(
      id = a.id,
      locationTypeId = a.locationTypeId,
      areaId = a.areaId,
      coordinatesX = a.coordinatesX,
      coordinatesY = a.coordinatesY,
      road = a.road);
  }
}

class Location(id: Int, var locationTypeId: Int, var areaId: Int, var coordinatesX: Int, var coordinatesY: Int, var road: Boolean) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "locationTypeId" -> locationTypeId,
      "areaId" -> areaId,
      "coordinatesX" -> coordinatesX,
      "coordinatesY" -> coordinatesY,
      "road" -> road);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
  
  override def toString = "Location #" + id +": [" + areaId + "," + coordinatesX + "," + coordinatesY + "]";
}

