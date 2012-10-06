package com.warspite.insulae.database.geography

trait GeographyDatabase {
	def getAreaById(id: Int): Area;
	def getAreaByRealmId(realmId: Int): Array[Area];
	def getAreaByRealmIdAndCoorindates(realmId: Int, coordinatesX: Int, coordinatesY: Int): Area;
	def putArea(a: Area): Area;

	def getAreaTypeById(id: Int): AreaType;
	def getAreaTypeAll(): Array[AreaType];
	
	def getAreaTemplateByAreaTypeId(areaTypeId: Int): Array[AreaTemplate];
	def getAreaTemplateByStartingAreaOfRaceId(raceId: Int): Array[AreaTemplate];
	
	def getLocationTemplateByAreaTemplateId(areaTemplateId: Int): Array[LocationTemplate];
	
	def getLocationTypeById(id: Int): LocationType;
	def getLocationTypeAll(): Array[LocationType];

	def getLocationById(id: Int): Location;
	def getLocationByCoordinates(areaId: Int, x: Int, y: Int): Location;
	def getLocationByAreaId(areaId: Int): Array[Location];
	def putLocation(l: Location): Location;

	def getTransportationTypeById(id: Int): TransportationType;
	def getTransportationTypeAll(): Array[TransportationType];

	def getTransportationCostByLocationTypeIdAndTransportationTypeId(locationTypeId: Int, transportationTypeId: Int): TransportationCost;
	def getTransportationCostAll(): Array[TransportationCost];

	def getLocationNeighborByLocationId(locationId: Int): Array[LocationNeighbor];
	def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor];
	def putLocationNeighbor(n: Array[LocationNeighbor]);

	def setRoad(locationId: Int, road: Boolean);

	def getResourceTypeById(id: Int): ResourceType;
	def getResourceTypeAll(): Array[ResourceType];

	def getResourceByLocationId(locationId: Int): Array[Resource];
	def getResourceByAreaId(areaId: Int): Array[Resource];
	
	def getStartingLocationByRaceIdAndRealmId(raceId: Int, realmId: Int): Array[StartingLocation];
	def deleteStartingLocationByLocationIdAndRaceId(locationId: Int, raceId: Int);
	def putStartingLocation(s: StartingLocation);
}