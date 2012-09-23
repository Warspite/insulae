package com.warspite.insulae.database.industry

trait IndustryDatabase {
	def getBuildingTypeById(id: Int): BuildingType;
	def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType];
	def getBuildingTypeAll(): Array[BuildingType];

	def getBuildingById(id: Int): Building;
	def getBuildingByLocationId(locationId: Int): Building;
	def getBuildingByAreaId(areaId: Int): Array[Building];
	def getBuildingByAbilityToPerformAutomatedAction(): Array[Building];
	def putBuilding(b: Building): Building;
	def deleteBuildingById(id: Int);
	def setBuildingTypeId(buildingId: Int, newBuildingTypeId: Int);
	def setBuildingActionAutomation(buildingId: Int, actionId: Int);
	def setBuildingReservedActionPoints(buildingId: Int, reservedActionPoints: Int);
	def setHubDistanceCost(buildingId: Int, cost: Int);

	def getItemStorageByBuildingId(buildingId: Int): Array[ItemStorage];
	def getItemStorageByBuildingIdAndItemTypeId(buildingId: Int, itemTypeId: Int): ItemStorage;
	def getItemStorageByAreaIdAndAvatarId(areaId: Int, avatarId: Int): Array[ItemStorage];
	def changeItemStorageAmount(buildingId: Int, itemTypeId: Int, amount: Int): Boolean;
	def changeBuildingActionPoints(id: Int, amount: Double);
	def tickBuildingActionPoints(): Boolean;

	def getItemTypeById(id: Int): ItemType;
	def getItemTypeAll(): Array[ItemType];

	def getActionAll(): Array[Action];
	def getActionById(id: Int): Action;
	def getActionByBuildingTypeId(buildingTypeId: Int): Array[Action];

	def getActionItemCostAll(): Array[ActionItemCost];
	def getActionItemCostByActionId(actionId: Int): Array[ActionItemCost];

	def getActionItemOutputAll(): Array[ActionItemOutput];
	def getActionItemOutputByActionId(actionId: Int): Array[ActionItemOutput];

	def getLocationTypesRequiredNearActionTargetLocationByActionId(actionId: Int): Array[LocationTypeRequiredNearActionTargetLocation];
	def getLocationTypesRequiredNearActionTargetLocationAll(): Array[LocationTypeRequiredNearActionTargetLocation];

	def getResourcesRequiredNearActionTargetLocationByActionId(actionId: Int): Array[ResourceRequiredNearActionTargetLocation];
	def getResourcesRequiredNearActionTargetLocationAll(): Array[ResourceRequiredNearActionTargetLocation];
	
	def getItemHoardingOrderAll(): Array[ItemHoardingOrder];
	def getItemHoardingOrderByAvatarId(avatarId: Int): Array[ItemHoardingOrder];
	def getItemHoardingOrderByBuildingIdAndItemTypeId(buildingId: Int, itemTypeId: Int): ItemHoardingOrder;
	def getItemHoardingOrderByBuildingId(buildingId: Int): Array[ItemHoardingOrder];
	def getUnsatisfiedOrderedItemHoardingOrderAll(): Array[ItemHoardingOrder];
	def putItemHoardingOrder(i: ItemHoardingOrder): ItemHoardingOrder;
	def deleteItemHoardingOrderByBuildingIdAndItemTypeId(buildingId: Int, itemTypeId: Int);
}