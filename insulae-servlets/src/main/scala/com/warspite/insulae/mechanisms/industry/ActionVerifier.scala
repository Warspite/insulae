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

  def verifyAgentLocation(location: Location, action: Action) {
  }

  def verifyTargetLocation(targetLocation: Location, action: Action, agentLocation: Location, agent: TemporaryAgent) {
    if (!action.requiresLocationId)
      return ;

    if (targetLocation == null)
      throw new RequiredTargetLocationIdMissingException(action);

    verifyTargetLocationIsWithinRange(agentLocation, targetLocation, action, agent);

    if (action.constructsBuilding)
      verifyTargetLocationHasNoBuilding(targetLocation);
  }

  def verifyAgent(agent: TemporaryAgent, action: Action) {
    verifyAgentCanPerformAction(agent, action);
  }

  def verifyAgentCanPerformAction(agent: TemporaryAgent, action: Action) {
    for (capableAction <- agent.capableActions)
      if (capableAction.id == action.id)
        return ;

    throw new AgentIsNotCapableOfPerformingActionException(action, agent);
  }

  def verifyTargetLocationIsWithinRange(agentLocation: Location, targetLocation: Location, action: Action, agent: TemporaryAgent) {
    if (agent.maximumRange(action) == ActionPerformer.UNSET_MAXIMUM_RANGE)
      return ;

    if (pathFinder.findRange(agentLocation.id, targetLocation.id, agent.maximumRange(action)) == PathFinder.TARGET_NOT_WITHIN_RANGE)
      throw new MaximumActionRangeExceededException(agent.maximumRange(action));
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
    if (targetLocation.road)
      throw new RoadAlreadyExistsAtTargetLocationException(targetLocation);
  }
}