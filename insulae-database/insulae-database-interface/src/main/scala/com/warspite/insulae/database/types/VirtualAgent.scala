package com.warspite.insulae.database.types
import com.warspite.common.database.types.IdentifiedType
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Action
import com.warspite.insulae.database.industry.BuildingType
import com.warspite.common.database.types.DescriptiveType

abstract class VirtualAgent(id: Int, var locationId: Int, var avatarId: Int, var actionPoints: Double) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "locationId" -> locationId,
      "avatarId" -> avatarId,
      "actionPoints" -> actionPoints);

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
}