package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object Location extends StoredType {
  val fields = List("id", "locationTypeId", "areaId", "coordinatesX", "coordinatesY");

  def apply(r: DataRecord) = {
    new Location(
      id = r.get[Int]("id"),
      locationTypeId = r.get[Int]("locationTypeId"),
      areaId = r.get[Int]("areaId"),
      coordinatesX = r.get[Int]("coordinatesX"),
      coordinatesY = r.get[Int]("coordinatesY"))
  }

  def apply(a: Location) = {
    new Location(
      id = a.id,
      locationTypeId = a.locationTypeId,
      areaId = a.areaId,
      coordinatesX = a.coordinatesX,
      coordinatesY = a.coordinatesY)
  }
}

class Location(var id: Int, var locationTypeId: Int, var areaId: Int, var coordinatesX: Int, var coordinatesY: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "locationTypeId" -> locationTypeId,
      "areaId" -> areaId,
      "coordinatesX" -> coordinatesX,
      "coordinatesY" -> coordinatesY);

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
  
  override def toString = "Location #" + id +": [" + areaId + "," + coordinatesX + "," + coordinatesY + "]";
}

