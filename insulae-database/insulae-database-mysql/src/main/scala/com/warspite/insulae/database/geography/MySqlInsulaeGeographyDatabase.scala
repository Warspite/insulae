package com.warspite.insulae.database.geography
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import scala.collection.mutable.Queue

class MySqlInsulaeGeographyDatabase(connection: Connection) extends MySqlQueryer(connection) with GeographyDatabase {
  def getAreaById(id: Int): Area = {
    val r = query(Area.fields, "FROM Area WHERE id = " + id);
    return r.buildSingle(Area.apply);
  }

  def getAreaByRealmId(realmId: Int): Array[Area] = {
    val r = query(Area.fields, "FROM Area WHERE realmId = " + realmId);
    return r.buildArray(Area.apply);
  }

  def getAreaByRealmIdAndCoorindates(realmId: Int, coordinatesX: Int, coordinatesY: Int): Area = {
    val r = query(Area.fields, "FROM Area WHERE realmId = " + realmId + " AND coordinatesX = " + coordinatesX + " AND coordinatesY = " + coordinatesY);
    return r.buildSingle(Area.apply);
  }

  def putArea(a: Area): Area = {
    try {
      val existingArea = getAreaByRealmIdAndCoorindates(a.realmId, a.coordinatesX, a.coordinatesY);
      throw new AreaAtCoordinatesAlreadyExistsException(a.realmId, a.coordinatesX, a.coordinatesY);
    } catch {
      case e: ExpectedRecordNotFoundException => None;
    }

    insert("Area", a.asMap(false, true));

    return getAreaByRealmIdAndCoorindates(a.realmId, a.coordinatesX, a.coordinatesY);
  }

  def getAreaTypeById(id: Int): AreaType = {
    val r = query(AreaType.fields, "FROM AreaType WHERE id = " + id);
    return r.buildSingle(AreaType.apply);
  }

  def getAreaTypeAll(): Array[AreaType] = {
    val r = query(AreaType.fields, "FROM AreaType");
    return r.buildArray(AreaType.apply);
  }

  def getAreaTemplateByAreaTypeId(areaTypeId: Int): Array[AreaTemplate] = {
    val r = query(AreaTemplate.fields, "FROM AreaTemplate WHERE areaTypeId = " + areaTypeId);
    return r.buildArray(AreaTemplate.apply);
  }

  def getAreaTemplateByStartingAreaOfRaceId(raceId: Int): Array[AreaTemplate] = {
    val r = query(AreaTemplate.fields, "FROM AreaTemplate, AreaType WHERE AreaTemplate.areaTypeId = AreaType.id AND AreaType.startingAreaOfRaceId = " + raceId, "AreaTemplate");
    return r.buildArray(AreaTemplate.apply);
  }

  def getLocationTemplateByAreaTemplateId(areaTemplateId: Int): Array[LocationTemplate] = {
    val r = query(LocationTemplate.fields, "FROM LocationTemplate WHERE areaTemplateId = " + areaTemplateId);
    return r.buildArray(LocationTemplate.apply);
  }

  def getLocationTypeById(id: Int): LocationType = {
    val r = query(LocationType.fields, "FROM LocationType WHERE id = " + id);
    return r.buildSingle(LocationType.apply);
  }

  def getLocationTypeAll(): Array[LocationType] = {
    val r = query(LocationType.fields, "FROM LocationType");
    return r.buildArray(LocationType.apply);
  }

  def getLocationById(id: Int): Location = {
    val r = query(Location.fields, "FROM Location WHERE id = " + id);
    return r.buildSingle(Location.apply);
  }

  def getLocationByCoordinates(areaId: Int, x: Int, y: Int): Location = {
    val r = query(Location.fields, "FROM Location WHERE areaId = " + areaId + " AND coordinatesX = " + x + " AND coordinatesY = " + y);
    return r.buildSingle(Location.apply);
  }

  def getLocationByAreaId(areaId: Int): Array[Location] = {
    val r = query(Location.fields, "FROM Location WHERE areaId = " + areaId);
    return r.buildArray(Location.apply);
  }

  def putLocation(l: Location): Location = {
    try {
      val existingLocation = getLocationByCoordinates(l.areaId, l.coordinatesX, l.coordinatesY);
      throw new LocationAtCoordinatesAlreadyExistsException(l.areaId, l.coordinatesX, l.coordinatesY);
    } catch {
      case e: ExpectedRecordNotFoundException => None;
    }

    insert("Location", l.asMap(false, true));

    return getLocationByCoordinates(l.areaId, l.coordinatesX, l.coordinatesY);
  }

  def getTransportationTypeById(id: Int): TransportationType = {
    val r = query(TransportationType.fields, "FROM TransportationType WHERE id = " + id);
    return r.buildSingle(TransportationType.apply);
  }

  def getTransportationTypeAll(): Array[TransportationType] = {
    val r = query(TransportationType.fields, "FROM TransportationType");
    return r.buildArray(TransportationType.apply);
  }

  def getTransportationCostByLocationTypeIdAndTransportationTypeId(locationTypeId: Int, transportationTypeId: Int): TransportationCost = {
    val r = query(TransportationCost.fields, "FROM TransportationCost WHERE locationTypeId = " + locationTypeId + " AND transportationTypeId = " + transportationTypeId);
    return r.buildSingle(TransportationCost.apply);
  }

  def getTransportationCostAll(): Array[TransportationCost] = {
    val r = query(TransportationCost.fields, "FROM TransportationCost");
    return r.buildArray(TransportationCost.apply);
  }

  def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor] = {
    val r = query(LocationNeighbor.fields, "FROM LocationNeighbor, Location WHERE Location.areaId = " + areaId + " AND LocationNeighbor.locationId = Location.id", "LocationNeighbor");
    return r.buildArray(LocationNeighbor.apply);
  }

  def getLocationNeighborByLocationId(locationId: Int): Array[LocationNeighbor] = {
    val r = query(LocationNeighbor.fields, "FROM LocationNeighbor WHERE locationId = " + locationId);
    return r.buildArray(LocationNeighbor.apply);
  }

  def setRoad(locationId: Int, road: Boolean) {
    stmt("UPDATE Location SET road = " + road + " WHERE id = " + locationId);
  }

  def getResourceTypeById(id: Int): ResourceType = {
    val r = query(ResourceType.fields, "FROM ResourceType WHERE id = " + id);
    return r.buildSingle(ResourceType.apply);
  }

  def getResourceTypeAll(): Array[ResourceType] = {
    val r = query(ResourceType.fields, "FROM ResourceType");
    return r.buildArray(ResourceType.apply);
  }

  def getResourceByLocationId(locationId: Int): Array[Resource] = {
    val r = query(Resource.fields, "FROM Resource WHERE locationId = " + locationId);
    return r.buildArray(Resource.apply);
  }

  def getResourceByAreaId(areaId: Int): Array[Resource] = {
    val r = query(Resource.fields, "FROM Resource, Location WHERE Resource.locationId = Location.id AND Location.areaId = " + areaId, "Resource");
    return r.buildArray(Resource.apply);
  }

  def getStartingLocationByRaceIdAndRealmId(raceId: Int, realmId: Int): Array[StartingLocation] = {
    val r = query(StartingLocation.fields, "FROM StartingLocation, Location, Area WHERE StartingLocation.raceId = " + raceId + " AND Location.id = StartingLocation.locationId AND Area.id = Location.areaId AND Area.realmId = " + realmId, "StartingLocation");
    return r.buildArray(StartingLocation.apply);
  }

  def deleteStartingLocationByLocationIdAndRaceId(locationId: Int, raceId: Int) {
    stmt("DELETE FROM StartingLocation WHERE locationId = " + locationId + " AND raceId = " + raceId);
  }

  def putStartingLocation(s: StartingLocation) {
    insert("StartingLocation", s.asMap(false, true));
  }
}
