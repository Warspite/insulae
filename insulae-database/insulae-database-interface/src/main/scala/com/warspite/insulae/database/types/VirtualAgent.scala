package com.warspite.insulae.database.types
import com.warspite.common.database.types.IdentifiedType
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Action
import com.warspite.insulae.database.industry.BuildingType
import com.warspite.common.database.types.DescriptiveType
import com.warspite.insulae.database.industry.ActionIdDoesNotExistException

object VirtualAgent {
  val LOCATION_ID = "locationId";
  val AVATAR_ID = "avatarId";
  val ACTION_POINTS = "actionPoints";
  val AUTOMATED_ACTION_ID = "automatedActionId";
  val RESERVED_ACTION_POINTS = "reservedActionPoints";

  val fields = List(LOCATION_ID, AVATAR_ID, ACTION_POINTS, AUTOMATED_ACTION_ID, RESERVED_ACTION_POINTS) ++ IdentifiedType.fields;
}

abstract class VirtualAgent(id: Int, var locationId: Int, var avatarId: Int, var actionPoints: Double, var reservedActionPoints: Int, var automatedActionId: Int) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      VirtualAgent.LOCATION_ID -> locationId,
      VirtualAgent.AVATAR_ID -> avatarId,
      VirtualAgent.ACTION_POINTS -> actionPoints,
      VirtualAgent.RESERVED_ACTION_POINTS -> reservedActionPoints,
      VirtualAgent.AUTOMATED_ACTION_ID -> automatedActionId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }

  def getType(db: InsulaeDatabase): DescriptiveType = {
    throw new UnrecognizedAgentTypeException(this);
  }

  def getHubDistanceCost: Int = {
    throw new UnrecognizedAgentTypeException(this);
  }

  def getIndustryHub(db: InsulaeDatabase): Building = {
    throw new UnrecognizedAgentTypeException(this);
  }

  def getMaximumRange(action: Action, db: InsulaeDatabase): Int = {
    throw new UnrecognizedAgentTypeException(this);
  }

  def getCapableActions(db: InsulaeDatabase): Array[Action] = {
    throw new UnrecognizedAgentTypeException(this);
  }

  def reload(db: InsulaeDatabase): VirtualAgent = {
    throw new UnrecognizedAgentTypeException(this);
  }

  def changeActionPoints(amount: Double, db: InsulaeDatabase) {
    throw new UnrecognizedAgentTypeException(this);
  }

  def isBuilding: Boolean = false;

  def getAutomatedAction(db: InsulaeDatabase): Action = {
    try {
      db.industry.getActionById(automatedActionId);
    } catch {
      case e: ActionIdDoesNotExistException => null;
    }
  }
}