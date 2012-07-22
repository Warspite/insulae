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

	def getItemStorageByBuildingId(buildingId: Int): Array[ItemStorage];
	def getItemStorageByAreaIdAndAvatarId(areaId: Int, avatarId: Int): Array[ItemStorage];
	def changeItemStorageAmount(buildingId: Int, itemTypeId: Int, amount: Int): Boolean;

	def getItemTypeById(id: Int): ItemType;
	def getItemTypeAll(): Array[ItemType];
}