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
    return BuildingType(r.next(true).getOrElse(throw new BuildingTypeIdDoesNotExistException(id)));
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
    return Building(r.next(true).getOrElse(throw new BuildingIdDoesNotExistException(id)));
  }
  
  def getBuildingByLocationId(locationId: Int): Building = {
    val r = query(Building.fields, "FROM Building WHERE locationId = " + locationId);
    return Building(r.next(true).getOrElse(throw new BuildingAtLocationIdDoesNotExistException(locationId)));
  }
  
  def getBuildingByAreaId(areaId: Int): Array[Building] = {
    val r = query(Building.fields, "FROM Building, Location WHERE Building.locationId = Location.id AND Location.areaId = " + areaId, "Building");
    return r.buildArray[Building](Building.apply);
  }

  def putBuilding(b: Building): Building = {
	  try {
	    val existingBuilding = getBuildingByLocationId(b.locationId);
	    throw new BuildingAtLocationIdAlreadyExistsException(b.locationId);
	  }
	  catch {
	    case e: BuildingAtLocationIdDoesNotExistException => None;
	  }
	  
	  insert("Building", b.asMap(false, true));
	  
	  return getBuildingByLocationId(b.locationId);
  }
  
  def deleteBuildingById(id: Int) {
    stmt("DELETE FROM Building WHERE id = " + id);
  }

  def changeBuildingTypeId(buildingId: Int, newBuildingTypeId: Int) {
    stmt("UPDATE Building SET buildingTypeId = " + newBuildingTypeId + " WHERE id = " + buildingId);
  }
  
  def getItemStorageByBuildingId(buildingId: Int): Array[ItemStorage] = {
    val r = query(ItemStorage.fields, "FROM ItemStorage WHERE buildingId = " + buildingId);
    return r.buildArray[ItemStorage](ItemStorage.apply);
  }
  
  def getItemStorageByAreaIdAndAvatarId(areaId: Int, avatarId: Int): Array[ItemStorage] = {
    val r = query(ItemStorage.fields, "FROM ItemStorage, Location, Building WHERE ItemStorage.buildingId = Building.id AND Building.avatarId = " + avatarId + " AND Building.locationId = Location.id AND Location.areaId = " + areaId, "ItemStorage");
    return r.buildArray[ItemStorage](ItemStorage.apply);
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
    return ItemType(r.next(true).getOrElse(throw new ItemTypeIdDoesNotExistException(id)));
  }
  
  def getItemTypeAll(): Array[ItemType] = {
    val r = query(ItemType.fields, "FROM ItemType");
    return r.buildArray[ItemType](ItemType.apply);
  }

  def getActionAll(): Array[Action] = {
    val r = query(Action.fields, "FROM Action");
    return r.buildArray[Action](Action.apply);
  }
  
  def getActionByBuildingTypeId(buildingTypeId: Int): Array[Action] = {
    val r = query(Action.fields, "FROM Action, ActionByBuildingType WHERE Action.id = ActionByBuildingType.actionId AND ActionByBuildingType.buildingTypeId = " + buildingTypeId, "Action");
    return r.buildArray[Action](Action.apply);
  }
  
  def getActionById(id: Int): Action = {
    val r = query(Action.fields, "FROM Action WHERE id = " + id);
    return Action(r.next(true).getOrElse(throw new ActionIdDoesNotExistException(id)));
  }
  
  def getActionItemCostAll(): Array[ActionItemCost] = {
    val r = query(ActionItemCost.fields, "FROM ActionItemCost");
    return r.buildArray[ActionItemCost](ActionItemCost.apply);
    
  }
	
  def getActionItemCostByActionId(actionId: Int): Array[ActionItemCost] = {
    val r = query(ActionItemCost.fields, "FROM ActionItemCost WHERE actionId = " + actionId);
    return r.buildArray[ActionItemCost](ActionItemCost.apply);
  }

  def getActionItemOutputAll(): Array[ActionItemOutput] = {
    val r = query(ActionItemOutput.fields, "FROM ActionItemOutput");
    return r.buildArray[ActionItemOutput](ActionItemOutput.apply);
    
  }
	
  def getActionItemOutputByActionId(actionId: Int): Array[ActionItemOutput] = {
    val r = query(ActionItemOutput.fields, "FROM ActionItemOutput WHERE actionId = " + actionId);
    return r.buildArray[ActionItemOutput](ActionItemOutput.apply);
  }
}
