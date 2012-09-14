package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.mechanisms.geography.PathFinder
import com.warspite.insulae.database.geography.Location

object ActionPerformer {
  val UNSET_TARGET_LOCATION_ID = -1;
  val UNSET_MAXIMUM_RANGE = -1;
}

class ActionPerformer(val db: InsulaeDatabase, val transactor: ItemTransactor, val actionVerifier: ActionVerifier, val pathFinder: PathFinder, val customActionEffector: CustomActionEffector) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def perform(action: Action, agentObj: Object, targetLocationId: Int = ActionPerformer.UNSET_TARGET_LOCATION_ID) {

    synchronized {
      logger.debug("Performing " + action + " with agent " + agentObj);

      val agent = new TemporaryAgent(agentObj, db);
      val securedActionPointCost = secureActionPointCost(action, agent);
      val agentLocation = determineAgentLocation(agent);
      val targetLocation = determineTargetLocation(targetLocationId);

      actionVerifier.verifyAgent(agent, action);
      actionVerifier.verifyAgentLocation(agentLocation, action);
      actionVerifier.verifyTargetLocation(targetLocation, action, agentLocation, agent);

      var transactionKey = transactor.acquireLock();
      try {
        transactor.withdraw(transactionKey, Item(db.industry.getActionItemCostByActionId(action.id)), determineAvailableItemStorages(agent));
        transactor.deposit(transactionKey, Item(db.industry.getActionItemOutputByActionId(action.id)), agent.industryHub);
      } finally {
        transactor.releaseLock(transactionKey);
      }

      reduceActionPoints(agent, securedActionPointCost);
      constructBuilding(action, agent, agentLocation, targetLocation);
      upgradeBuilding(action, agent);
      customActionEffector.effect(action, agent, targetLocation);
    }
  }

  def determineAgentLocation(agent: TemporaryAgent): Location = {
    db.geography.getLocationById(agent.locationId);
  }

  def determineTargetLocation(targetLocationId: Int): Location = {
    if (targetLocationId == ActionPerformer.UNSET_TARGET_LOCATION_ID)
      return null;

    db.geography.getLocationById(targetLocationId);
  }

  def determineAvailableItemStorages(agent: TemporaryAgent): Seq[Building] = {
    if (agent.isBuilding && agent.o != agent.industryHub)
      return List(agent.o.asInstanceOf[Building], agent.industryHub);
    else
      return List(agent.industryHub);
  }

  def secureActionPointCost(action: Action, agent: TemporaryAgent): Int = {
    var apCost = action.actionPointCost + agent.hubDistanceCost;

    if (apCost > agent.actionPoints)
      throw new InsufficientActionPointsException(apCost, agent.actionPoints);

    return apCost;
  }

  def reduceActionPoints(agent: TemporaryAgent, amount: Int) {
    if (agent.isBuilding)
      db.industry.changeBuildingActionPoints(agent.id, -amount);
    else
      throw new UnrecognizedAgentTypeException(agent);
  }

  def constructBuilding(action: Action, agent: TemporaryAgent, agentLocation: Location, targetLocation: Location) {
    if (!action.constructsBuilding || !agent.isBuilding)
      return ;

    val constructedBuildingType = db.industry.getBuildingTypeById(action.constructedBuildingTypeId);
    val industryHubBuildingId = constructedBuildingType.isIndustryHub match {
      case true => agent.id;
      case false => 0;
    };
    val hubDistanceCost = industryHubBuildingId match {
      case 0 => 0;
      case _ => pathFinder.findPath(constructedBuildingType.transportationTypeId, targetLocation.id, agentLocation.id).cost();
    };

    db.industry.putBuilding(new Building(0, targetLocation.id, action.constructedBuildingTypeId, agent.avatarId, 0.0, 0, industryHubBuildingId, agent.hubDistanceCost));
  }

  def upgradeBuilding(action: Action, agent: TemporaryAgent) {
    if (!agent.isBuilding || action.upgradesToBuildingTypeId == 0)
      return ;
    
    db.industry.setBuildingTypeId(agent.id, action.upgradesToBuildingTypeId);
  }
}