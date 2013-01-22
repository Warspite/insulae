package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.insulae.database.types.VirtualAgent
import com.warspite.common.database.types.DescriptiveType

object Building {
  val fields = List("buildingTypeId", "industryHubBuildingId", "hubDistanceCost") ++ VirtualAgent.fields;

  def apply(r: DataRecord) = {
    new Building(
      id = r.get[Int](IdentifiedType.ID),
      locationId = r.get[Int](VirtualAgent.LOCATION_ID),
      buildingTypeId = r.get[Int]("buildingTypeId"),
      avatarId = r.get[Int](VirtualAgent.AVATAR_ID),
      actionPoints = r.get[Double](VirtualAgent.ACTION_POINTS),
      reservedActionPoints = r.get[Int](VirtualAgent.RESERVED_ACTION_POINTS),
      industryHubBuildingId = r.get[Int]("industryHubBuildingId"),
      hubDistanceCost = r.get[Int]("hubDistanceCost"),
      automatedActionId = r.get[Int](VirtualAgent.AUTOMATED_ACTION_ID));
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
      hubDistanceCost = a.hubDistanceCost,
      automatedActionId = a.automatedActionId);
  }
}

class Building(id: Int, locationId: Int, var buildingTypeId: Int, avatarId: Int, actionPoints: Double, reservedActionPoints: Int, var industryHubBuildingId: Int, var hubDistanceCost: Int, automatedActionId: Int) extends VirtualAgent(id, locationId, avatarId, actionPoints, reservedActionPoints, automatedActionId) {
  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      "buildingTypeId" -> buildingTypeId,
      "industryHubBuildingId" -> industryHubBuildingId,
      "hubDistanceCost" -> hubDistanceCost);

    return map ++ super.asMap();
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

