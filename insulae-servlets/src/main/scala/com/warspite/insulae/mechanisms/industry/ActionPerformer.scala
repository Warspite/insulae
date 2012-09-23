package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.mechanisms.geography.PathFinder
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.types.VirtualAgent

object ActionPerformer {
  val UNSET_TARGET_LOCATION_ID = -1;
  val UNSET_MAXIMUM_RANGE = -1;
}

class ActionPerformer(val db: InsulaeDatabase, val transactor: ItemTransactor, val actionVerifier: ActionVerifier, val pathFinder: PathFinder, val customActionEffector: CustomActionEffector) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def performAutomatedBuildingActions() {
    for (b <- db.industry.getBuildingByAbilityToPerformAutomatedAction()) {
      val a = db.industry.getActionById(b.automatedActionId);
      try {
        perform(a, b);
        logger.debug("Performed automated " + a + " in " + b);
      } catch {
        case e: ItemTransactionException => None;
        case e: RuntimeException => logger.error("Execution of automated " + a + " in " + b + " failed.", e);
      }
    }
  }

  def perform(action: Action, originalAgent: VirtualAgent, targetLocationId: Int = ActionPerformer.UNSET_TARGET_LOCATION_ID) {

    synchronized {
      logger.debug("Performing " + action + " with agent " + originalAgent);

      val agent = originalAgent.reload(db);
      val securedActionPointCost = secureActionPointCost(action, agent);
      val agentLocation = determineAgentLocation(agent);
      val targetLocation = determineTargetLocation(targetLocationId);

      actionVerifier.verifyAgent(agent, action);
      actionVerifier.verifyAgentLocation(agentLocation, action);
      actionVerifier.verifyTargetLocation(targetLocation, action, agentLocation, agent);

      var transactionKey = transactor.acquireLock();
      try {
        transactor.withdraw(transactionKey, Item(db.industry.getActionItemCostByActionId(action.id)), determineAvailableItemStorages(agent));
        transactor.deposit(transactionKey, Item(db.industry.getActionItemOutputByActionId(action.id)), agent.getIndustryHub(db));
      } finally {
        transactor.releaseLock(transactionKey);
      }

      agent.changeActionPoints(-securedActionPointCost, db);
      constructBuilding(action, agent, agentLocation, targetLocation);
      upgradeBuilding(action, agent);
      customActionEffector.effect(action, agent, targetLocation);
      logger.debug("Completed " + action + " with agent " + originalAgent);
    }
  }

  def determineAgentLocation(agent: VirtualAgent): Location = {
    db.geography.getLocationById(agent.locationId);
  }

  def determineTargetLocation(targetLocationId: Int): Location = {
    if (targetLocationId == ActionPerformer.UNSET_TARGET_LOCATION_ID)
      return null;

    db.geography.getLocationById(targetLocationId);
  }

  def determineAvailableItemStorages(agent: VirtualAgent): Seq[Building] = {
    var list = List[Building]();

    if (agent.isBuilding)
      list ::= agent.asInstanceOf[Building];

    val industryHub = agent.getIndustryHub(db);
    if (industryHub != null)
      list ::= industryHub;

    return list;
  }

  def secureActionPointCost(action: Action, agent: VirtualAgent): Int = {
    var apCost = action.actionPointCost + agent.getHubDistanceCost;

    if (apCost > agent.actionPoints)
      throw new InsufficientActionPointsException(apCost, agent.actionPoints);

    return apCost;
  }

  def constructBuilding(action: Action, agent: VirtualAgent, agentLocation: Location, targetLocation: Location) {
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

    db.industry.putBuilding(new Building(0, targetLocation.id, action.constructedBuildingTypeId, agent.avatarId, 0.0, 0, industryHubBuildingId, agent.getHubDistanceCost, 0));
  }

  def upgradeBuilding(action: Action, agent: VirtualAgent) {
    if (!agent.isBuilding || action.upgradesToBuildingTypeId == 0)
      return ;

    db.industry.setBuildingTypeId(agent.id, action.upgradesToBuildingTypeId);
  }
}