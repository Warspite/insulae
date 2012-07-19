package com.warspite.insulae.database.geography

import org.scala_tools.time.Imports._

class DummyInsulaeGeographyDatabase extends GeographyDatabase {
	def getAreaById(id: Int): Area = {
	  new Area(id, "name!", "description", 1, 1, 1);
	}

	def getAreaByRealmId(realmId: Int): Array[Area] = {
	  Array[Area](new Area(1, "name!", "description", 1, 1, realmId), new Area(2, "other name!", "other description", 1, 2, realmId));
	}
	
	def getLocationTypeAll(): Array[LocationType] = {
	  Array[LocationType](new LocationType(1, "name!", "description", "loc-name"), new LocationType(2, "other name!", "other description", "loc-otherName"));
	}

	def getLocationByAreaId(areaId: Int): Array[Location] = {
	  Array[Location](new Location(1, 1, areaId, 1, 1), new Location(2, 2, areaId, 1, 2), new Location(3, 1, areaId, 2, 1), new Location(4, 1, areaId, 2, 2));
	}

	def getTransportationTypeAll(): Array[TransportationType] = {
	  Array[TransportationType](new TransportationType(1, "Some transportation type"), new TransportationType(2, "Some other transportation type"));
	}

	def getTransportationCostAll(): Array[TransportationCost] = {
	  Array[TransportationCost](new TransportationCost(1, 1, 5, 3), new TransportationCost(2, 1, 7, 5));
	}

	def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor] = {
	  Array[LocationNeighbor](new LocationNeighbor(1, 2), new LocationNeighbor(2, 1));
	}
}
