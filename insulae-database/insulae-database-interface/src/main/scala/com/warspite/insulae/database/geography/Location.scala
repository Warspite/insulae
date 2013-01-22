package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.json.JsonSerializable

object Location {
  val INCOMING_PORTAL_POSSIBLE = "incomingPortalPossible";
  val fields = List("locationTypeId", "areaId", "coordinatesX", "coordinatesY", "road", INCOMING_PORTAL_POSSIBLE) ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Location(
      id = r.get[Int](IdentifiedType.ID),
      locationTypeId = r.get[Int]("locationTypeId"),
      areaId = r.get[Int]("areaId"),
      coordinatesX = r.get[Int]("coordinatesX"),
      coordinatesY = r.get[Int]("coordinatesY"),
      road = r.get[Boolean]("road"),
      incomingPortalPossible = r.get[Boolean](Location.INCOMING_PORTAL_POSSIBLE));
  }

  def apply(a: Location) = {
    new Location(
      id = a.id,
      locationTypeId = a.locationTypeId,
      areaId = a.areaId,
      coordinatesX = a.coordinatesX,
      coordinatesY = a.coordinatesY,
      road = a.road,
      incomingPortalPossible = a.incomingPortalPossible);
  }
}

class Location(id: Int, var locationTypeId: Int, var areaId: Int, var coordinatesX: Int, var coordinatesY: Int, var road: Boolean, var incomingPortalPossible: Boolean) extends IdentifiedType(id) {
  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      "locationTypeId" -> locationTypeId,
      "areaId" -> areaId,
      "coordinatesX" -> coordinatesX,
      "coordinatesY" -> coordinatesY,
      "road" -> road,
      Location.INCOMING_PORTAL_POSSIBLE -> incomingPortalPossible);

    return map ++ super.asMap();
  }
  
  override def toString = "Location #" + id +": [" + areaId + "," + coordinatesX + "," + coordinatesY + "]";
}

