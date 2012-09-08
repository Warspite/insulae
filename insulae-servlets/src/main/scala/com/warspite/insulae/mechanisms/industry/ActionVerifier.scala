package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.mechanisms.geography.PathFinder
import com.warspite.insulae.database.geography.Location

class ActionVerifier(val db: InsulaeDatabase, val pathFinder: PathFinder) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def verifyAgentCanPerformAction(action: Action, agent: Object) {
    agent match {
      case a: Building => {
        var buildingType = db.industry.getBuildingTypeById(agent.asInstanceOf[Building].buildingTypeId);
        for (capableAction <- db.industry.getActionByBuildingTypeId(buildingType.id))
          if (capableAction.id == action.id)
            return ;

        throw new AgentIsNotCapableOfPerformingActionException(action, agent);
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }

  def verifyRange(action: Action, agent: Object, targetLocation: Location) {
    if (targetLocation == null)
      return;

    var maximumRange = action.maximumRange;
    var agentLocationId = 0;
    agent match {
      case a: Building => {
        agentLocationId = agent.asInstanceOf[Building].locationId;
        val hubRangeOfConstructingBuilding = db.industry.getBuildingTypeById(a.asInstanceOf[Building].buildingTypeId).industryHubRange;
        if(maximumRange == ActionPerformer.UNSET_MAXIMUM_RANGE || maximumRange > hubRangeOfConstructingBuilding)
        	maximumRange = hubRangeOfConstructingBuilding; 
        
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }

    if(maximumRange == ActionPerformer.UNSET_MAXIMUM_RANGE)
      return;
    
    if(pathFinder.findRange(agentLocationId, targetLocation.id, maximumRange) == PathFinder.TARGET_NOT_WITHIN_RANGE)
      throw new MaximumActionRangeExceededException(maximumRange);
  }

  def verifyTargetLocationHasNoBuilding(targetLocation: Location) {
    try {
      db.industry.getBuildingByLocationId(targetLocation.id)
      throw new BuildingAlreadyExistsAtTargetLocationException(targetLocation);
    } catch {
      case e: ExpectedRecordNotFoundException => None;
    }
  }

  def verifyTargetLocationHasNoRoad(targetLocation: Location) {
    if(targetLocation.road)
      throw new RoadAlreadyExistsAtTargetLocationException(targetLocation);
  }
}