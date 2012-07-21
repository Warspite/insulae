package com.warspite.insulae.database.industry

trait IndustryDatabase {
	def getBuildingTypeById(id: Int): BuildingType;
	def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType];
	def getBuildingTypeAll(): Array[BuildingType];

	def getBuildingByAreaId(areaId: Int): Array[Building];
}