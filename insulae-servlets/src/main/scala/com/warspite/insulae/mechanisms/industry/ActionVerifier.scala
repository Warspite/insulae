package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.mechanisms.geography.PathFinder
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.mechanisms.geography.Surveyor
import com.warspite.insulae.database.types.VirtualAgent

class ActionVerifier(val db: InsulaeDatabase, val surveyor: Surveyor) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def verifyAgentLocation(location: Location, action: Action) {
  }

  def verifyTargetLocation(targetLocation: Location, action: Action, agentLocation: Location, agent: VirtualAgent) {
    if (!action.requiresLocationId)
      return ;

    if (targetLocation == null)
      throw new RequiredTargetLocationIdMissingException(action);

    verifyTargetLocationIsWithinRange(agentLocation, targetLocation, action, agent);

    if (action.constructsBuilding)
      verifyTargetLocationHasNoBuilding(targetLocation);

    verifyTargetLocationIsNearRequiredLocationTypes(targetLocation, action);
    verifyTargetLocationIsNearRequiredResources(targetLocation, action);
  }

  def verifyAgent(agent: VirtualAgent, action: Action) {
    verifyAgentCanPerformAction(agent, action);
  }

  def verifyAgentCanPerformAction(agent: VirtualAgent, action: Action) {
    for (capableAction <- agent.getCapableActions(db))
      if (capableAction.id == action.id)
        return ;

    throw new AgentIsNotCapableOfPerformingActionException(action, agent);
  }

  def verifyTargetLocationIsWithinRange(agentLocation: Location, targetLocation: Location, action: Action, agent: VirtualAgent) {
    val maxRange = agent.getMaximumRange(action, db); 
    if (maxRange == ActionPerformer.UNSET_MAXIMUM_RANGE)
      return ;

    if (surveyor.findRange(agentLocation.id, targetLocation.id, maxRange) == PathFinder.TARGET_NOT_WITHIN_RANGE)
      throw new MaximumActionRangeExceededException(maxRange);
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

  def verifyTargetLocationIsNearRequiredLocationTypes(targetLocation: Location, action: Action) {
    for(req <- db.industry.getLocationTypesRequiredNearActionTargetLocationByActionId(action.id)) {
      val availableLocationTypes = surveyor.countLocationTypesWithinRange(targetLocation, req.maximumRange);
      if(!availableLocationTypes.contains(req.locationTypeId) || availableLocationTypes(req.locationTypeId) < req.number)
        throw new RequiredLocationTypesNotFoundNearTargetLocationException(targetLocation, action, req.locationTypeId);
    }
  }

  def verifyTargetLocationIsNearRequiredResources(targetLocation: Location, action: Action) {
    for(req <- db.industry.getResourcesRequiredNearActionTargetLocationByActionId(action.id)) {
      val availableResources = surveyor.countResourcesWithinRange(targetLocation, req.maximumRange);
      if(!availableResources.contains(req.resourceTypeId) || availableResources(req.resourceTypeId) < req.number)
        throw new RequiredResourcesNotFoundNearTargetLocationException(targetLocation, action, req.resourceTypeId);
    }
  }
}