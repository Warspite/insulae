package com.warspite.insulae.database.industry

import org.scala_tools.time.Imports._

class DummyInsulaeIndustryDatabase extends IndustryDatabase {
  def getBuildingTypeById(id: Int): BuildingType = {
    new BuildingType(id, "Some name", "Some description", "someCanonicalName", 1, 1, 10, 1.0, 0);
  }

  def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType] = {
    Array[BuildingType](new BuildingType(1, "Some name", "Some description", "someCanonicalName", raceId, 1, 10, 1.0, 0), new BuildingType(2, "Some other name", "Some other description", "someOtherCanonicalName", raceId, 2, 0, 1.5, 20));
  }

  def getBuildingTypeAll(): Array[BuildingType] = {
    Array[BuildingType](new BuildingType(1, "Some name", "Some description", "someCanonicalName", 1, 1, 10, 1.0, 0), new BuildingType(2, "Some other name", "Some other description", "someOtherCanonicalName", 2, 2, 0, 1.5, 20));
  }

  def getBuildingById(id: Int): Building = {
    new Building(id, 1, 1, 1, 10, 5, 0, 0);
  }

  def getBuildingByLocationId(locationId: Int): Building = {
    new Building(1, locationId, 1, 1, 10, 5, 0, 0);
  }

  def getBuildingByAreaId(areaId: Int): Array[Building] = {
    Array[Building](new Building(1, 1, 1, 1, 10, 5, 0, 0), new Building(2, 2, 2, 1, 0, 0, 0, 0));
  }

  def putBuilding(b: Building): Building = {
    b;
  }

  def deleteBuildingById(id: Int) {
  }

  def setBuildingTypeId(buildingId: Int, newBuildingTypeId: Int) {}

  def setHubDistanceCost(buildingId: Int, cost: Int) {}

  def getItemStorageByBuildingId(buildingId: Int): Array[ItemStorage] = {
    Array[ItemStorage](new ItemStorage(buildingId, 1, 5), new ItemStorage(buildingId, 2, 3));
  }

  def getItemStorageByAreaIdAndAvatarId(areaId: Int, avatarId: Int): Array[ItemStorage] = {
    Array[ItemStorage](new ItemStorage(1, 1, 5), new ItemStorage(1, 2, 3));
  }

  def changeItemStorageAmount(buildingId: Int, itemTypeId: Int, amount: Int): Boolean = {
    true;
  }

  def changeBuildingActionPoints(id: Int, amount: Double) {}

  def tickBuildingActionPoints(): Boolean = {
    true;
  }

  def getItemTypeById(id: Int): ItemType = {
    new ItemType(id, "Some item", "someItem");
  }

  def getItemTypeAll(): Array[ItemType] = {
    Array[ItemType](new ItemType(1, "Some item", "someItem"), new ItemType(2, "Other item", "otherItem"));
  }

  def getActionAll(): Array[Action] = {
    Array[Action](new Action(1, "Some action", "A very nice action indeed.", "someAction", 10, 1, false, 0, 0), new Action(1, "Some other action", "A very nice other action indeed.", "someOtherAction", 15, 2, false, 0, 0));
  }

  def getActionByBuildingTypeId(buildingTypeId: Int): Array[Action] = {
    Array[Action](new Action(1, "Some action", "A very nice action indeed.", "someAction", 10, 1, false, 0, 0), new Action(1, "Some other action", "A very nice other action indeed.", "someOtherAction", 15, 2, false, 0, 0));
  }

  def getActionById(id: Int): Action = {
    new Action(id, "Some action", "A very nice action indeed.", "someAction", 10, 1, false, 0, 0);
  }

  def getActionItemCostAll(): Array[ActionItemCost] = {
    Array[ActionItemCost](new ActionItemCost(1, 1, 10), new ActionItemCost(1, 2, 3), new ActionItemCost(2, 4, 7), new ActionItemCost(2, 1, 12));
  }

  def getActionItemCostByActionId(actionId: Int): Array[ActionItemCost] = {
    Array[ActionItemCost](new ActionItemCost(actionId, 1, 2), new ActionItemCost(actionId, 2, 20));
  }

  def getActionItemOutputAll(): Array[ActionItemOutput] = {
    Array[ActionItemOutput](new ActionItemOutput(1, 1, 10), new ActionItemOutput(1, 2, 3), new ActionItemOutput(2, 4, 7), new ActionItemOutput(2, 1, 12));
  }

  def getActionItemOutputByActionId(actionId: Int): Array[ActionItemOutput] = {
    Array[ActionItemOutput](new ActionItemOutput(actionId, 1, 2), new ActionItemOutput(actionId, 2, 20));
  }
}
