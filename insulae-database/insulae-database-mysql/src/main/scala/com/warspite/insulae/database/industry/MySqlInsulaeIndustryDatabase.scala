package com.warspite.insulae.database.industry

import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import scala.collection.mutable.Queue
import com.warspite.insulae.database.InsulaeDatabaseException

class MySqlInsulaeIndustryDatabase(connection: Connection) extends MySqlQueryer(connection) with IndustryDatabase {
  def getBuildingTypeById(id: Int): BuildingType = {
    val r = query(BuildingType.fields, "FROM BuildingType WHERE id = " + id);
    return r.buildSingle(BuildingType.apply);
  }

  def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType] = {
    val r = query(BuildingType.fields, "FROM BuildingType WHERE raceId = " + raceId);
    return r.buildArray[BuildingType](BuildingType.apply);
  }

  def getBuildingTypeAll(): Array[BuildingType] = {
    val r = query(BuildingType.fields, "FROM BuildingType");
    return r.buildArray[BuildingType](BuildingType.apply);
  }

  def getBuildingById(id: Int): Building = {
    val r = query(Building.fields, "FROM Building WHERE id = " + id);
    return r.buildSingle(Building.apply);
  }

  def getBuildingByLocationId(locationId: Int): Building = {
    val r = query(Building.fields, "FROM Building WHERE locationId = " + locationId);
    return r.buildSingle(Building.apply);
  }

  def getBuildingByMultipleLocationId(locationIds: Array[Int]): Array[Building] = {
    if(locationIds.isEmpty)
      return Array[Building]();
    
    var whereClause: String = null;
    for(i <- locationIds) {
      if(whereClause == null)
        whereClause = "WHERE locationId = " + i;
      else
        whereClause += " OR locationId = " + i;
    }
    
    val r = query(Building.fields, "FROM Building " + whereClause);
    return r.buildArray[Building](Building.apply);
  }
  
  def getBuildingByAreaId(areaId: Int): Array[Building] = {
    val r = query(Building.fields, "FROM Building, Location WHERE Building.locationId = Location.id AND Location.areaId = " + areaId, "Building");
    return r.buildArray[Building](Building.apply);
  }

  def getBuildingByAbilityToPerformAutomatedAction(): Array[Building] = {
    val r = query(Building.fields, "FROM Building, Action WHERE Building.automatedActionId != 0 AND Action.id = Building.automatedActionId AND (Action.actionPointCost + Building.hubDistanceCost) <= (Building.actionPoints - Building.reservedActionPoints)", "Building");
    return r.buildArray[Building](Building.apply);
  }

  def putBuilding(b: Building): Building = {
    try {
      val existingBuilding = getBuildingByLocationId(b.locationId);
      throw new BuildingAtLocationIdAlreadyExistsException(b.locationId);
    } catch {
      case e: ExpectedRecordNotFoundException => None;
    }

    insert("Building", b.asMap(false, true));

    return getBuildingByLocationId(b.locationId);
  }

  def deleteBuildingById(id: Int) {
    stmt("DELETE ItemStorage.* FROM Building, ItemStorage WHERE Building.industryHubBuildingId = " + id + " AND ItemStorage.buildingId = Building.id");
    stmt("DELETE FROM Building WHERE industryHubBuildingId = " + id);

    stmt("DELETE FROM ItemStorage WHERE buildingId = " + id)
    stmt("DELETE FROM Building WHERE id = " + id);
  }

  def setBuildingTypeId(buildingId: Int, newBuildingTypeId: Int) {
    stmt("UPDATE Building SET buildingTypeId = " + newBuildingTypeId + " WHERE id = " + buildingId);
  }

  def setBuildingActionAutomation(buildingId: Int, actionId: Int) {
    stmt("UPDATE Building SET automatedActionId = " + actionId + " WHERE id = " + buildingId);
  }

  def setBuildingReservedActionPoints(buildingId: Int, reservedActionPoints: Int) {
    stmt("UPDATE Building SET reservedActionPoints = " + reservedActionPoints + " WHERE id = " + buildingId);
  }

  def setHubDistanceCost(buildingId: Int, cost: Int) {
    stmt("UPDATE Building SET hubDistanceCost = " + cost + " WHERE id = " + buildingId);
  }

  def getItemStorageByBuildingId(buildingId: Int): Array[ItemStorage] = {
    val r = query(ItemStorage.fields, "FROM ItemStorage WHERE buildingId = " + buildingId);
    return r.buildArray(ItemStorage.apply);
  }

  def getItemStorageByBuildingIdAndItemTypeId(buildingId: Int, itemTypeId: Int): ItemStorage = {
    val r = query(ItemStorage.fields, "FROM ItemStorage WHERE buildingId = " + buildingId + " AND itemTypeId = " + itemTypeId);
    return r.buildSingle(ItemStorage.apply);
  }

  def getItemStorageByAreaIdAndAvatarId(areaId: Int, avatarId: Int): Array[ItemStorage] = {
    val r = query(ItemStorage.fields, "FROM ItemStorage, Location, Building WHERE ItemStorage.buildingId = Building.id AND Building.avatarId = " + avatarId + " AND Building.locationId = Location.id AND Location.areaId = " + areaId, "ItemStorage");
    return r.buildArray(ItemStorage.apply);
  }

  def changeItemStorageAmount(buildingId: Int, itemTypeId: Int, amount: Int): Boolean = {
    val r = query("SELECT changeNumberOfItemsInStorage(" + buildingId + ", " + itemTypeId + ", " + amount + ") AS success;");
    return r.next(true).getOrElse(throw new ItemStorageException("Failed to change item storage amount (attempted to change storage of item " + itemTypeId + " in building " + buildingId + " by " + amount + ".")).get[Boolean]("success");
  }

  def changeBuildingActionPoints(id: Int, amount: Double) {
    stmt("UPDATE Building SET actionPoints = actionPoints + " + amount + " WHERE id = " + id + ";");
  }

  def tickBuildingActionPoints(): Boolean = {
    val r = query("SELECT tickBuildingActionPoints() AS success;");
    return r.next(true).getOrElse(throw new ActionPointTickException("Failed to tick building action points.")).get[Boolean]("success");
  }

  def getItemTypeById(id: Int): ItemType = {
    val r = query(ItemType.fields, "FROM ItemType WHERE id = " + id);
    return r.buildSingle(ItemType.apply);
  }

  def getItemTypeAll(): Array[ItemType] = {
    val r = query(ItemType.fields, "FROM ItemType");
    return r.buildArray(ItemType.apply);
  }

  def getActionAll(): Array[Action] = {
    val r = query(Action.fields, "FROM Action");
    return r.buildArray(Action.apply);
  }

  def getActionByBuildingTypeId(buildingTypeId: Int): Array[Action] = {
    val r = query(Action.fields, "FROM Action, ActionByBuildingType WHERE Action.id = ActionByBuildingType.actionId AND ActionByBuildingType.buildingTypeId = " + buildingTypeId, "Action");
    return r.buildArray(Action.apply);
  }

  def getActionById(id: Int): Action = {
    val r = query(Action.fields, "FROM Action WHERE id = " + id);
    return r.buildSingle(Action.apply);
  }

  def getActionItemCostAll(): Array[ActionItemCost] = {
    val r = query(ActionItemCost.fields, "FROM ActionItemCost");
    return r.buildArray(ActionItemCost.apply);

  }

  def getActionItemCostByActionId(actionId: Int): Array[ActionItemCost] = {
    val r = query(ActionItemCost.fields, "FROM ActionItemCost WHERE actionId = " + actionId);
    return r.buildArray(ActionItemCost.apply);
  }

  def getActionItemOutputAll(): Array[ActionItemOutput] = {
    val r = query(ActionItemOutput.fields, "FROM ActionItemOutput");
    return r.buildArray(ActionItemOutput.apply);

  }

  def getActionItemOutputByActionId(actionId: Int): Array[ActionItemOutput] = {
    val r = query(ActionItemOutput.fields, "FROM ActionItemOutput WHERE actionId = " + actionId);
    return r.buildArray(ActionItemOutput.apply);
  }

  def getLocationTypesRequiredNearActionTargetLocationByActionId(actionId: Int): Array[LocationTypeRequiredNearActionTargetLocation] = {
    val r = query(LocationTypeRequiredNearActionTargetLocation.fields, "FROM LocationTypeRequiredNearActionTargetLocation WHERE actionId = " + actionId);
    return r.buildArray(LocationTypeRequiredNearActionTargetLocation.apply);
  }

  def getLocationTypesRequiredNearActionTargetLocationAll(): Array[LocationTypeRequiredNearActionTargetLocation] = {
    val r = query(LocationTypeRequiredNearActionTargetLocation.fields, "FROM LocationTypeRequiredNearActionTargetLocation");
    return r.buildArray(LocationTypeRequiredNearActionTargetLocation.apply);
  }

  def getResourcesRequiredNearActionTargetLocationByActionId(actionId: Int): Array[ResourceRequiredNearActionTargetLocation] = {
    val r = query(ResourceRequiredNearActionTargetLocation.fields, "FROM ResourceRequiredNearActionTargetLocation WHERE actionId = " + actionId);
    return r.buildArray(ResourceRequiredNearActionTargetLocation.apply);
  }

  def getResourcesRequiredNearActionTargetLocationAll(): Array[ResourceRequiredNearActionTargetLocation] = {
    val r = query(ResourceRequiredNearActionTargetLocation.fields, "FROM ResourceRequiredNearActionTargetLocation");
    return r.buildArray(ResourceRequiredNearActionTargetLocation.apply);
  }

  def getItemHoardingOrderAll(): Array[ItemHoardingOrder] = {
    val r = query(ItemHoardingOrder.fields, "FROM ItemHoardingOrder");
    return r.buildArray(ItemHoardingOrder.apply);
  }

  def getUnsatisfiedOrderedItemHoardingOrderAll(): Array[ItemHoardingOrder] = {
    val r = query(ItemHoardingOrder.fields, "FROM ItemHoardingOrder WHERE NOT EXISTS (SELECT * FROM ItemStorage WHERE ItemStorage.buildingId = ItemHoardingOrder.buildingId AND ItemStorage.itemTypeId = ItemHoardingOrder.itemTypeId AND ItemStorage.amount >= ItemHoardingOrder.amount) ORDER BY ItemHoardingOrder.priority ASC");
    return r.buildArray(ItemHoardingOrder.apply);
  }

  def getItemHoardingOrderByAvatarId(avatarId: Int): Array[ItemHoardingOrder] = {
    val r = query(ItemHoardingOrder.fields, "FROM ItemHoardingOrder, Building WHERE Building.id = ItemHoardingOrder.buildingId AND Building.avatarId = " + avatarId, "ItemHoardingOrder");
    return r.buildArray(ItemHoardingOrder.apply);
  }

  def getItemHoardingOrderByBuildingIdAndItemTypeId(buildingId: Int, itemTypeId: Int): ItemHoardingOrder = {
    val r = query(ItemHoardingOrder.fields, "FROM ItemHoardingOrder WHERE buildingId = " + buildingId + " AND itemTypeId = " + itemTypeId);
    return r.buildSingle(ItemHoardingOrder.apply);
  }

  def getItemHoardingOrderByBuildingId(buildingId: Int): Array[ItemHoardingOrder] = {
    val r = query(ItemHoardingOrder.fields, "FROM ItemHoardingOrder WHERE buildingId = " + buildingId);
    return r.buildArray(ItemHoardingOrder.apply);
  }

  def putItemHoardingOrder(i: ItemHoardingOrder): ItemHoardingOrder = {
    insert("ItemHoardingOrder", i.asMap(false, true));

    return getItemHoardingOrderByBuildingIdAndItemTypeId(i.buildingId, i.itemTypeId);
  }

  def deleteItemHoardingOrderByBuildingIdAndItemTypeId(buildingId: Int, itemTypeId: Int) {
    stmt("DELETE FROM ItemHoardingOrder WHERE buildingId = " + buildingId + " AND itemTypeId = " + itemTypeId);
  }

  def getStartingBuildingByRaceId(raceId: Int): Array[StartingBuilding] = {
    val r = query(StartingBuilding.fields, "FROM StartingBuilding WHERE raceId = " + raceId);
    return r.buildArray(StartingBuilding.apply);
  }
}
