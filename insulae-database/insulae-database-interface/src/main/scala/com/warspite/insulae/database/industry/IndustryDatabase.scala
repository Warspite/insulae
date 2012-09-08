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
	def changeBuildingTypeId(buildingId: Int, newBuildingTypeId: Int);

	def getItemStorageByBuildingId(buildingId: Int): Array[ItemStorage];
	def getItemStorageByAreaIdAndAvatarId(areaId: Int, avatarId: Int): Array[ItemStorage];
	def changeItemStorageAmount(buildingId: Int, itemTypeId: Int, amount: Int): Boolean;
	def changeBuildingActionPoints(id: Int, amount: Int);

	def getItemTypeById(id: Int): ItemType;
	def getItemTypeAll(): Array[ItemType];

	def getActionAll(): Array[Action];
	def getActionById(id: Int): Action;
	def getActionByBuildingTypeId(buildingTypeId: Int): Array[Action];

	def getActionItemCostAll(): Array[ActionItemCost];
	def getActionItemCostByActionId(actionId: Int): Array[ActionItemCost];

	def getActionItemOutputAll(): Array[ActionItemOutput];
	def getActionItemOutputByActionId(actionId: Int): Array[ActionItemOutput];

}