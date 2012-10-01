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
    return Area(r.next(true).getOrElse(throw new AreaIdDoesNotExistException(id)));
  }

  def getAreaByRealmId(realmId: Int): Array[Area] = {
    val r = query(Area.fields, "FROM Area WHERE realmId = " + realmId);
    return r.buildArray[Area](Area.apply);
  }

  def getAreaTypeById(id: Int): AreaType = {
    val r = query(AreaType.fields, "FROM AreaType WHERE id = " + id);
    return AreaType(r.next(true).getOrElse(throw new AreaTypeIdDoesNotExistException(id)));
  }

  def getAreaTypeAll(): Array[AreaType] = {
    val r = query(AreaType.fields, "FROM AreaType");
    return r.buildArray[AreaType](AreaType.apply);
  }

  def getLocationTypeById(id: Int): LocationType = {
    val r = query(LocationType.fields, "FROM LocationType WHERE id = " + id);
    return LocationType(r.next(true).getOrElse(throw new LocationTypeIdDoesNotExistException(id)));
  }

  def getLocationTypeAll(): Array[LocationType] = {
    val r = query(LocationType.fields, "FROM LocationType");
    return r.buildArray[LocationType](LocationType.apply);
  }

  def getLocationById(id: Int): Location = {
    val r = query(Location.fields, "FROM Location WHERE id = " + id);
    return Location(r.next(true).getOrElse(throw new LocationIdDoesNotExistException(id)));
  }

  def getLocationByCoordinates(areaId: Int, x: Int, y: Int): Location = {
    val r = query(Location.fields, "FROM Location WHERE areaId = " + areaId + " AND coordinatesX = " + x + " AND coordinatesY = " + y);
    return Location(r.next(true).getOrElse(throw new LocationCoordinatesDoNotExistException(areaId, x, y)));
  }
  
  def getLocationByAreaId(areaId: Int): Array[Location] = {
    val r = query(Location.fields, "FROM Location WHERE areaId = " + areaId);
    return r.buildArray[Location](Location.apply);
  }

  def getTransportationTypeById(id: Int): TransportationType = {
    val r = query(TransportationType.fields, "FROM TransportationType WHERE id = " + id);
    return TransportationType(r.next(true).getOrElse(throw new TransportationTypeIdDoesNotExistException(id)));
  }

  def getTransportationTypeAll(): Array[TransportationType] = {
    val r = query(TransportationType.fields, "FROM TransportationType");
    return r.buildArray[TransportationType](TransportationType.apply);
  }

  def getTransportationCostByLocationTypeIdAndTransportationTypeId(locationTypeId: Int, transportationTypeId: Int): TransportationCost = {
    val r = query(TransportationCost.fields, "FROM TransportationCost WHERE locationTypeId = " + locationTypeId + " AND transportationTypeId = " + transportationTypeId);
    return TransportationCost(r.next(true).getOrElse(throw new TransportationCostDoesNotExistException(locationTypeId, transportationTypeId)));
  }

  def getTransportationCostAll(): Array[TransportationCost] = {
    val r = query(TransportationCost.fields, "FROM TransportationCost");
    return r.buildArray[TransportationCost](TransportationCost.apply);
  }

  def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor] = {
    val r = query(LocationNeighbor.fields, "FROM LocationNeighbor, Location WHERE Location.areaId = " + areaId + " AND LocationNeighbor.locationId = Location.id", "LocationNeighbor");
    return r.buildArray[LocationNeighbor](LocationNeighbor.apply);
  }

  def getLocationNeighborByLocationId(locationId: Int): Array[LocationNeighbor] = {
    val r = query(LocationNeighbor.fields, "FROM LocationNeighbor WHERE locationId = " + locationId);
    return r.buildArray[LocationNeighbor](LocationNeighbor.apply);
  }

  def setRoad(locationId: Int, road: Boolean) {
    stmt("UPDATE Location SET road = " + road + " WHERE id = " + locationId);
  }

  def getResourceTypeById(id: Int): ResourceType = {
    val r = query(ResourceType.fields, "FROM ResourceType WHERE id = " + id);
    return ResourceType(r.next(true).getOrElse(throw new ResourceTypeIdDoesNotExistException(id)));
  }

  def getResourceTypeAll(): Array[ResourceType] = {
    val r = query(ResourceType.fields, "FROM ResourceType");
    return r.buildArray[ResourceType](ResourceType.apply);
  }

  def getResourceByLocationId(locationId: Int): Array[Resource] = {
    val r = query(Resource.fields, "FROM Resource WHERE locationId = " + locationId);
    return r.buildArray[Resource](Resource.apply);
  }

  def getResourceByAreaId(areaId: Int): Array[Resource] = {
    val r = query(Resource.fields, "FROM Resource, Location WHERE Resource.locationId = Location.id AND Location.areaId = " + areaId, "Resource");
    return r.buildArray[Resource](Resource.apply);
  }

  def getStartingLocationByRaceIdAndRealmId(raceId: Int, realmId: Int): Array[StartingLocation] = {
    val r = query(StartingLocation.fields, "FROM StartingLocation, Location, Area WHERE StartingLocation.raceId = " + raceId + " AND Location.id = StartingLocation.locationId AND Area.id = Location.areaId AND Area.realmId = " + realmId, "StartingLocation");
    return r.buildArray[StartingLocation](StartingLocation.apply);
  }

  def deleteStartingLocationByLocationIdAndRaceId(locationId: Int, raceId: Int) {
    stmt("DELETE FROM StartingLocation WHERE locationId = " + locationId + " AND raceId = " + raceId);
  }
}
