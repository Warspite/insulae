package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.mechanisms.geography.PathFinder
import com.warspite.insulae.database.geography.Location

class EffectArguments(val action: Action, var agentBuilding: Building = null, var targetLocation: Location = null) {
}

class EffectDescriptor(val effectMethod: (EffectArguments) => Unit, val agentBuildingRequired: Boolean = false, val targetLocationRequired: Boolean = false) {
}

class CustomActionEffector(val db: InsulaeDatabase, val pathFinder: PathFinder, val actionVerifier: ActionVerifier) {
  protected val logger = LoggerFactory.getLogger(getClass());
  val methods = Map("constructRoadTargeted" -> new EffectDescriptor(effectMethod = constructRoadTargeted, targetLocationRequired = true));

  def effect(action: Action, agent: Object, targetLocation: Location) {
    val descriptor = methods.getOrElse(action.canonicalName, return );
    val arg = new EffectArguments(action);

    if (descriptor.agentBuildingRequired) {
      if (agent == null || !agent.isInstanceOf[Building]) throw new InvalidCustomEffectArgumentException("Custom effect " + action.canonicalName + " requires an agent building, but " + agent + " was received.");
      arg.agentBuilding = agent.asInstanceOf[Building];
    }

    if (descriptor.targetLocationRequired) {
      if (targetLocation == null) throw new InvalidCustomEffectArgumentException("Custom effect " + action.canonicalName + " requires a target location, but " + targetLocation + " was received.");
      arg.targetLocation = targetLocation;
    }

    logger.info("Performing custom effect of " + action);
    descriptor.effectMethod(arg);
  }

  def constructRoadTargeted(arg: EffectArguments) {
    actionVerifier.verifyTargetLocationHasNoBuilding(arg.targetLocation);
    actionVerifier.verifyTargetLocationHasNoRoad(arg.targetLocation);
    db.geography.setRoad(arg.targetLocation.id, true);
    
    for(b <- db.industry.getBuildingByAreaId(arg.targetLocation.areaId))
      updateBuildingHubDistanceCost(b);
  }
  
  def updateBuildingHubDistanceCost(b: Building) {
      if(b.industryHubBuildingId == 0)
        return;
      
      val hub = db.industry.getBuildingById(b.industryHubBuildingId);
      val buildingType = db.industry.getBuildingTypeById(b.buildingTypeId);
      val hubDistanceCost = pathFinder.findPath(buildingType.transportationTypeId, b.locationId, hub.locationId).cost();
      db.industry.setHubDistanceCost(b.id, hubDistanceCost);
  }
}

