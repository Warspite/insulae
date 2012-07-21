package com.warspite.insulae.database.industry

trait IndustryDatabase {
	def getBuildingTypeById(id: Int): BuildingType;
	def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType];
	def getBuildingTypeAll(): Array[BuildingType];

	def getBuildingById(id: Int): Building;
	def getBuildingByLocationId(locationId: Int): Building;
	def getBuildingByAreaId(areaId: Int): Array[Building];
	def putBuilding(b: Building): Building;
	def deleteBuildingById(id: Int);
}