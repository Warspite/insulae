package com.warspite.insulae.database.geography

import org.scala_tools.time.Imports._

class DummyInsulaeGeographyDatabase extends GeographyDatabase {
  def getAreaById(id: Int): Area = {
    new Area(id, "name!", 1, 1, 1, 1);
  }

  def getAreaByRealmId(realmId: Int): Array[Area] = {
    Array[Area](new Area(1, "name!", 1, 1, realmId, 1), new Area(2, "other name!", 1, 2, realmId, 1));
  }

  def getAreaByRealmIdAndCoorindates(realmId: Int, coordinatesX: Int, coordinatesY: Int): Area = {
    new Area(1, "name!", realmId, coordinatesX, coordinatesY, 1);
  }

  def putArea(a: Area): Area = a;

  def getAreaTypeById(id: Int): AreaType = {
    new AreaType(1, "name!", "description!", "canonical!", 0);
  }

  def getAreaTypeAll(): Array[AreaType] = {
    Array(new AreaType(1, "name!", "description!", "canonical!", 0), new AreaType(2, "other name!", "other description!", "other canonical!", 0));
  }

  def getAreaTemplateByAreaTypeId(areaTypeId: Int): Array[AreaTemplate] = {
    Array(new AreaTemplate(1, areaTypeId), new AreaTemplate(2, areaTypeId));
  }

  def getAreaTemplateByStartingAreaOfRaceId(raceId: Int): Array[AreaTemplate] = {
    Array(new AreaTemplate(1, 1), new AreaTemplate(2, 5));
  }

  def getLocationTemplateByAreaTemplateId(areaTemplateId: Int): Array[LocationTemplate] = {
    Array(new LocationTemplate(areaTemplateId, 1, 0, 0, false, 0, 0, false), new LocationTemplate(areaTemplateId, 1, 0, 1, true, 0, 1, false), new LocationTemplate(areaTemplateId, 2, 1, 0, false, 1, 0, true));
  }

  def getLocationTypeById(id: Int): LocationType = {
    new LocationType(id, "name!", "description", "loc-name");
  }

  def getLocationTypeAll(): Array[LocationType] = {
    Array[LocationType](new LocationType(1, "name!", "description", "loc-name"), new LocationType(2, "other name!", "other description", "loc-otherName"));
  }

  def getLocationById(id: Int): Location = {
    new Location(id, 1, 1, 1, 1, false, false);
  }

  def getLocationByCoordinates(areaId: Int, x: Int, y: Int): Location = {
    new Location(1, 1, areaId, x, y, false, false);
  }

  def getLocationByAreaId(areaId: Int): Array[Location] = {
    Array[Location](new Location(1, 1, areaId, 1, 1, true, false), new Location(2, 2, areaId, 1, 2, false, true), new Location(3, 1, areaId, 2, 1, true, false), new Location(4, 1, areaId, 2, 2, false, false));
  }

  def getLocationByPotentialPortalEndpoint(areaTypeId: Int, realmId: Int, excludedAreaId: Int): Array[Location] = {
    Array[Location](new Location(1, 1, 1, 1, 1, true, false), new Location(2, 2, 2, 1, 2, false, true), new Location(3, 1, 3, 2, 1, true, false), new Location(4, 1, 4, 2, 2, false, false));
  }

  def putLocation(l: Location): Location = l;

  def getTransportationTypeById(id: Int): TransportationType = {
    new TransportationType(id, "Some transportation type");
  }

  def getTransportationTypeAll(): Array[TransportationType] = {
    Array[TransportationType](new TransportationType(1, "Some transportation type"), new TransportationType(2, "Some other transportation type"));
  }

  def getTransportationCostAll(): Array[TransportationCost] = {
    Array[TransportationCost](new TransportationCost(1, 1, 5, 3), new TransportationCost(2, 1, 7, 5));
  }

  def getTransportationCostByLocationTypeIdAndTransportationTypeId(locationTypeId: Int, transportationTypeId: Int): TransportationCost = {
    new TransportationCost(locationTypeId, transportationTypeId, 2, 1);
  }

  def getLocationNeighborByLocationId(locationId: Int): Array[LocationNeighbor] = {
    Array[LocationNeighbor](new LocationNeighbor(1, 2), new LocationNeighbor(2, 1));
  }

  def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor] = {
    Array[LocationNeighbor](new LocationNeighbor(1, 2), new LocationNeighbor(2, 1));
  }

  def putLocationNeighbor(n: Array[LocationNeighbor]) {}

  def setRoad(locationId: Int, road: Boolean) {}
  def setIncomingPortalPossible(locationId: Int, portalPossible: Boolean) {}

  def getResourceTypeById(id: Int): ResourceType = {
    new ResourceType(id, "Type 1", "type1", "A very fine resource.");
  }

  def getResourceTypeAll(): Array[ResourceType] = {
    Array(new ResourceType(1, "Type 1", "type1", "A very fine resource."), new ResourceType(1, "Type 2", "type2", "Another fine resource."));
  }

  def getResourceByLocationId(locationId: Int): Array[Resource] = {
    Array(new Resource(1, locationId));
  }

  def getResourceByAreaId(areaId: Int): Array[Resource] = {
    Array(new Resource(1, 1), new Resource(3, 5), new Resource(2, 10));
  }

  def putResource(r: Resource) {}

  def getStartingLocationByRaceIdAndRealmId(raceId: Int, realmId: Int): Array[StartingLocation] = {
    Array(new StartingLocation(raceId, 1));
  }

  def deleteStartingLocationByLocationIdAndRaceId(locationId: Int, raceId: Int) {}

  def putStartingLocation(s: StartingLocation) {}

  def getAreaNameByAreaTypeId(areaTypeId: Int): Array[AreaName] = {
    Array(new AreaName("Some name", areaTypeId), new AreaName("Some other name", areaTypeId));
  }

  def getResourceOccurrenceByAreaTypeId(areaTypeId: Int): Array[ResourceOccurrence] = {
    Array(new ResourceOccurrence(areaTypeId, 1, 1, 0.03), new ResourceOccurrence(areaTypeId, 1, 2, 0.01), new ResourceOccurrence(areaTypeId, 2, 5, 0.1));
  }
}
