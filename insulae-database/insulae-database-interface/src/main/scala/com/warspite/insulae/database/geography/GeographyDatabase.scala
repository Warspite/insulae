package com.warspite.insulae.database.geography

trait GeographyDatabase {
	def getAreaById(id: Int): Area;
	def getAreaByRealmId(realmId: Int): Array[Area];

	def getLocationTypeAll(): Array[LocationType];

	def getLocationById(id: Int): Location;
	def getLocationByAreaId(areaId: Int): Array[Location];

	def getTransportationTypeAll(): Array[TransportationType];
	def getTransportationCostAll(): Array[TransportationCost];

	def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor];
}