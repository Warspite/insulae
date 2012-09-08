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

  def perform(action: Action, agent: Object, targetLocationId: Int = ActionPerformer.UNSET_TARGET_LOCATION_ID) {
    synchronized {
      logger.debug("Performing " + action + " with agent " + agent);

      val securedActionPointCost = secureActionPointCost(action, agent);
      
      var targetLocation: Location = null;
      if(targetLocationId != ActionPerformer.UNSET_TARGET_LOCATION_ID)
    	  targetLocation = db.geography.getLocationById(targetLocationId);
      
      if (action.requiresLocationId && targetLocation == null)
        throw new RequiredTargetLocationIdMissingException(action);

      actionVerifier.verifyAgentCanPerformAction(action, agent);
      actionVerifier.verifyRange(action, agent, targetLocation);
      
      if(action.constructedBuildingTypeId != 0) {
        actionVerifier.verifyTargetLocationHasNoBuilding(targetLocation);
        actionVerifier.verifyTargetLocationHasNoRoad(targetLocation);
      } 

      val industryHub = determineIndustryHub(agent);
      var transactionKey = transactor.acquireLock();
      try {
        transactor.withdraw(transactionKey, Item(db.industry.getActionItemCostByActionId(action.id)), determineAvailableItemStorages(agent, industryHub));
        transactor.deposit(transactionKey, Item(db.industry.getActionItemOutputByActionId(action.id)), industryHub);
      } finally {
        transactor.releaseLock(transactionKey);
      }

      reduceActionPoints(agent, securedActionPointCost);
      constructBuilding(action, agent, targetLocationId);
      upgradeBuilding(action, agent);
      customActionEffector.effect(action, agent, targetLocation);
    }
  }

  def determineIndustryHub(agent: Object): Building = {
    agent match {
      case agent: Building => {
        val b = agent.asInstanceOf[Building];
        if (b.industryHubBuildingId == 0)
          return b;
        else
          return db.industry.getBuildingById(agent.asInstanceOf[Building].industryHubBuildingId);
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }

  def determineAvailableItemStorages(agent: Object, industryHub: Building): Seq[Building] = {
    agent match {
      case a: Building => {
        if (a != industryHub)
          return List(agent.asInstanceOf[Building], industryHub);
        else
          return List(industryHub);
      }
      case _ => return List(industryHub);
    }
  }

  def secureActionPointCost(action: Action, agent: Object): Int = {
    var apCost = action.actionPointCost;
    var availableAp = 0.0;
    agent match {
      case a: Building => { apCost += agent.asInstanceOf[Building].hubDistanceCost; availableAp = agent.asInstanceOf[Building].actionPoints; }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }

    if (apCost > availableAp)
      throw new InsufficientActionPointsException(apCost, availableAp);

    return apCost;
  }

  def reduceActionPoints(agent: Object, amount: Int) {
    agent match {
      case a: Building => db.industry.changeBuildingActionPoints(agent.asInstanceOf[Building].id, -amount);
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }

  def constructBuilding(action: Action, agent: Object, targetLocationId: Int) {
    if (action.constructedBuildingTypeId == 0)
      return ;

    var avatarId = 0;
    var industryHubBuildingId = 0;
    var hubDistanceCost = 0;

    agent match {
      case a: Building => {
        val constructedBuildingType = db.industry.getBuildingTypeById(action.constructedBuildingTypeId);
        avatarId = a.asInstanceOf[Building].avatarId;
        
        if(!constructedBuildingType.isIndustryHub)
          industryHubBuildingId = a.asInstanceOf[Building].id;
        
        if(industryHubBuildingId != 0)
          hubDistanceCost = pathFinder.findPath(constructedBuildingType.transportationTypeId, targetLocationId, a.asInstanceOf[Building].locationId).cost();
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }

    db.industry.putBuilding(new Building(0, targetLocationId, action.constructedBuildingTypeId, avatarId, 0.0, 0, industryHubBuildingId, hubDistanceCost));
  }

  def upgradeBuilding(action: Action, agent: Object) {
    if (action.upgradesToBuildingTypeId == 0)
      return;

    agent match {
      case a: Building => {
        db.industry.setBuildingTypeId(a.asInstanceOf[Building].id, action.upgradesToBuildingTypeId);
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }
}