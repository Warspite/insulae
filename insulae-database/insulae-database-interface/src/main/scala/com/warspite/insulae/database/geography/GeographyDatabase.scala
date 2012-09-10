package com.warspite.insulae.database.geography

trait GeographyDatabase {
	def getAreaById(id: Int): Area;
	def getAreaByRealmId(realmId: Int): Array[Area];

	def getLocationTypeById(id: Int): LocationType;
	def getLocationTypeAll(): Array[LocationType];

	def getLocationById(id: Int): Location;
	def getLocationByAreaId(areaId: Int): Array[Location];

	def getTransportationTypeById(id: Int): TransportationType;
	def getTransportationTypeAll(): Array[TransportationType];

	def getTransportationCostByLocationTypeIdAndTransportationTypeId(locationTypeId: Int, transportationTypeId: Int): TransportationCost;
	def getTransportationCostAll(): Array[TransportationCost];

	def getLocationNeighborByLocationId(locationId: Int): Array[LocationNeighbor];
	def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor];

	def setRoad(locationId: Int, road: Boolean);

	def getResourceTypeById(id: Int): ResourceType;
	def getResourceTypeAll(): Array[ResourceType];

	def getResourceByLocationId(locationId: Int): Array[Resource];
	def getResourceByAreaId(areaId: Int): Array[Resource];
}