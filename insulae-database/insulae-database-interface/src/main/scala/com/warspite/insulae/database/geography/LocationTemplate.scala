package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.StoredType

object LocationTemplate {
  val AREA_TEMPLATE_ID = "areaTemplateId";
  val LOCATION_TYPE_ID = "locationTypeId";
  val COORDINATES_X = "coordinatesX";
  val COORDINATES_Y = "coordinatesY";
  val ROAD = "road";
  val STARTING_LOCATION_OF_RACE_ID = "startingLocationOfRaceId";
  val PORTAL_TO_AREA_TYPE_ID = "portalToAreaTypeId";
  val INCOMING_PORTAL_POSSIBLE = "incomingPortalPossible";
  val fields = List(AREA_TEMPLATE_ID, LOCATION_TYPE_ID, COORDINATES_X, COORDINATES_Y, ROAD, STARTING_LOCATION_OF_RACE_ID, PORTAL_TO_AREA_TYPE_ID, INCOMING_PORTAL_POSSIBLE) ++ StoredType.fields;

  def apply(r: DataRecord) = {
    new LocationTemplate(
      areaTemplateId = r.get[Int](LocationTemplate.AREA_TEMPLATE_ID),
      locationTypeId = r.get[Int](LocationTemplate.LOCATION_TYPE_ID),
      coordinatesX = r.get[Int](LocationTemplate.COORDINATES_X),
      coordinatesY = r.get[Int](LocationTemplate.COORDINATES_Y),
      road = r.get[Boolean](LocationTemplate.ROAD),
      startingLocationOfRaceId = r.get[Int](LocationTemplate.STARTING_LOCATION_OF_RACE_ID),
      portalToAreaTypeId = r.get[Int](LocationTemplate.PORTAL_TO_AREA_TYPE_ID),
      incomingPortalPossible = r.get[Boolean](LocationTemplate.INCOMING_PORTAL_POSSIBLE));
  }

  def apply(a: LocationTemplate) = {
    new LocationTemplate(
      areaTemplateId = a.areaTemplateId,
      locationTypeId = a.locationTypeId,
      coordinatesX = a.coordinatesX,
      coordinatesY = a.coordinatesY,
      road = a.road,
      startingLocationOfRaceId = a.startingLocationOfRaceId,
      portalToAreaTypeId = a.portalToAreaTypeId,
      incomingPortalPossible = a.incomingPortalPossible);
  }
}

class LocationTemplate(var areaTemplateId: Int, var locationTypeId: Int, var coordinatesX: Int, var coordinatesY: Int, var road: Boolean, var startingLocationOfRaceId: Int, var portalToAreaTypeId: Int, var incomingPortalPossible: Boolean) extends StoredType {
  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      LocationTemplate.AREA_TEMPLATE_ID -> areaTemplateId,
      LocationTemplate.LOCATION_TYPE_ID -> locationTypeId,
      LocationTemplate.COORDINATES_X -> coordinatesX,
      LocationTemplate.COORDINATES_Y -> coordinatesY,
      LocationTemplate.ROAD -> road,
      LocationTemplate.STARTING_LOCATION_OF_RACE_ID -> startingLocationOfRaceId,
      LocationTemplate.PORTAL_TO_AREA_TYPE_ID -> portalToAreaTypeId,
      LocationTemplate.INCOMING_PORTAL_POSSIBLE -> incomingPortalPossible);

    return map ++ super.asMap();
  }

  override def toString = getClass().getSimpleName() + " [areaTemplateId: " + areaTemplateId + ", locationTypeId: " + locationTypeId + ", coordinatesX: " + coordinatesX + ", coordinatesY: " + coordinatesY + ", road: " + road + ", startingLocationOfRaceId: " + startingLocationOfRaceId + ", incomingPortalPossible: " + incomingPortalPossible + ", portalToAreaTypeId: " + portalToAreaTypeId + "]";
}

