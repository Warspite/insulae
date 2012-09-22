package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.insulae.database.types.VirtualAgent
import com.warspite.common.database.types.DescriptiveType

object Building {
  val fields = List("locationId", "buildingTypeId", "avatarId", "actionPoints", "reservedActionPoints", "industryHubBuildingId", "hubDistanceCost") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Building(
      id = r.get[Int](IdentifiedType.ID),
      locationId = r.get[Int]("locationId"),
      buildingTypeId = r.get[Int]("buildingTypeId"),
      avatarId = r.get[Int]("avatarId"),
      actionPoints = r.get[Double]("actionPoints"),
      reservedActionPoints = r.get[Int]("reservedActionPoints"),
      industryHubBuildingId = r.get[Int]("industryHubBuildingId"),
      hubDistanceCost = r.get[Int]("hubDistanceCost"));
  }

  def apply(a: Building) = {
    new Building(
      id = a.id,
      locationId = a.locationId,
      buildingTypeId = a.buildingTypeId,
      avatarId = a.avatarId,
      actionPoints = a.actionPoints,
      reservedActionPoints = a.reservedActionPoints,
      industryHubBuildingId = a.industryHubBuildingId,
      hubDistanceCost = a.hubDistanceCost)
  }
}

class Building(id: Int, locationId: Int, var buildingTypeId: Int, avatarId: Int, actionPoints: Double, var reservedActionPoints: Int, var industryHubBuildingId: Int, var hubDistanceCost: Int) extends VirtualAgent(id, locationId, avatarId, actionPoints) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "buildingTypeId" -> buildingTypeId,
      "reservedActionPoints" -> reservedActionPoints,
      "industryHubBuildingId" -> industryHubBuildingId,
      "hubDistanceCost" -> hubDistanceCost);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }

  def isIndustryHub = industryHubBuildingId == 0;

  override def getType(db: InsulaeDatabase): DescriptiveType = db.industry.getBuildingTypeById(buildingTypeId);
  override def getHubDistanceCost: Int = hubDistanceCost;

  override def getIndustryHub(db: InsulaeDatabase): Building = isIndustryHub match {
    case true => null;
    case false => db.industry.getBuildingById(industryHubBuildingId);
  }

  override def getMaximumRange(action: Action, db: InsulaeDatabase): Int = {
    if (!action.constructsBuilding)
      return action.maximumRange;

    scala.math.min(db.industry.getBuildingTypeById(buildingTypeId).industryHubRange, action.maximumRange);
  }

  override def changeActionPoints(amount: Double, db: InsulaeDatabase) {
    db.industry.changeBuildingActionPoints(id, amount);
  }

  override def getCapableActions(db: InsulaeDatabase): Array[Action] = db.industry.getActionByBuildingTypeId(buildingTypeId);
  override def reload(db: InsulaeDatabase): VirtualAgent = db.industry.getBuildingById(id);
  override def isBuilding: Boolean = true;
}

