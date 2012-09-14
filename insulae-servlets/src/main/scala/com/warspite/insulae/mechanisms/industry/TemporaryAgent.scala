package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Action
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.BuildingType

object TemporaryAgent {
  def createSynchronized(unsynchronizedObj: Object, db: InsulaeDatabase): TemporaryAgent = {
    unsynchronizedObj match {
      case o: Building => new TemporaryAgent(db.industry.getBuildingById(o.asInstanceOf[Building].id), db);
      case o => throw new UnrecognizedAgentTypeException(o);
    }
  }
}

class TemporaryAgent(val o: Object, val db: InsulaeDatabase) {
  val id = o match {
    case o: Building => o.asInstanceOf[Building].id;
  }

  val t: Object = o match {
    case o: Building => db.industry.getBuildingTypeById(o.asInstanceOf[Building].buildingTypeId);
  }

  val locationId: Int = o match {
    case o: Building => o.asInstanceOf[Building].locationId;
  }

  val actionPoints: Double = o match {
    case o: Building => o.asInstanceOf[Building].actionPoints;
  }

  val hubDistanceCost: Int = o match {
    case o: Building => o.asInstanceOf[Building].hubDistanceCost;
  }

  val avatarId: Int = o match {
    case o: Building => o.asInstanceOf[Building].avatarId;
  }

  val industryHub: Building = {
    o match {
      case o: Building => {
        val b = o.asInstanceOf[Building];
        if (b.industryHubBuildingId == 0)
          b;
        else
          db.industry.getBuildingById(o.asInstanceOf[Building].industryHubBuildingId);
      }
      case _ => throw new UnrecognizedAgentTypeException(o);
    }
  }

  def isBuilding: Boolean = o match {
    case o: Building => true;
    case _ => false;
  }

  def maximumRange(action: Action): Int = {
    if (!action.constructsBuilding)
      return action.maximumRange;

    o match {
      case o: Building => return t.asInstanceOf[BuildingType].industryHubRange;
      case _ => throw new UnrecognizedAgentTypeException(o);
    }
  }

  def capableActions = o match {
    case o: Building => db.industry.getActionByBuildingTypeId(t.asInstanceOf[BuildingType].id);
    case _ => throw new UnrecognizedAgentTypeException(o);
  }

  override def toString(): String = {
    "Agent (" + o + ")";
  }
}