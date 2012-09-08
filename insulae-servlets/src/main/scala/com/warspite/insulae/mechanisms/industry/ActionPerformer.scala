package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.mechanisms.geography.PathFinder

object ActionPerformer {
  val UNSET_TARGET_LOCATION_ID = -1;
  val UNSET_MAXIMUM_RANGE = -1;
}

class ActionPerformer(val db: InsulaeDatabase, val transactor: ItemTransactor, val pathFinder: PathFinder) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def perform(action: Action, agent: Object, targetLocationId: Int = ActionPerformer.UNSET_TARGET_LOCATION_ID) {
    synchronized {
      logger.debug("Performing " + action + " with agent " + agent);

      val securedActionPointCost = secureActionPointCost(action, agent);

      if (action.requiresLocationId && targetLocationId == -1)
        throw new RequiredTargetLocationIdMissingException(action);

      verifyAgentCanPerformAction(action, agent);
      verifyRange(action, agent, targetLocationId);
      verifyBuildingConstruction(action, agent, targetLocationId);

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

  def verifyRange(action: Action, agent: Object, targetLocationId: Int) {
    if (targetLocationId == ActionPerformer.UNSET_TARGET_LOCATION_ID)
      return ;

    var maximumRange = action.maximumRange;
    var agentLocationId = 0;
    agent match {
      case a: Building => {
        logger.debug("verifyRange(): agent is a building.");
        agentLocationId = agent.asInstanceOf[Building].locationId;
        logger.debug("verifyRange(): agentlocation is " + agentLocationId);
        val hubRangeOfConstructingBuilding = db.industry.getBuildingTypeById(a.asInstanceOf[Building].buildingTypeId).industryHubRange;
        logger.debug("verifyRange(): hubrange of constructing building is " + hubRangeOfConstructingBuilding);
        if(maximumRange == ActionPerformer.UNSET_MAXIMUM_RANGE || maximumRange > hubRangeOfConstructingBuilding)
        	maximumRange = hubRangeOfConstructingBuilding; 
        
        logger.debug("verifyRange(): final maximumRange is " + maximumRange);
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }

    if(maximumRange == ActionPerformer.UNSET_MAXIMUM_RANGE)
      return;
    
    if(pathFinder.findRange(agentLocationId, targetLocationId, maximumRange) == PathFinder.TARGET_NOT_WITHIN_RANGE)
      throw new MaximumActionRangeExceededException(maximumRange);
  }

  def verifyBuildingConstruction(action: Action, agent: Object, targetLocationId: Int) {
    if (action.constructedBuildingTypeId == 0)
      return ;

    logger.debug("Checking whether " + action + " can construct its building (#" + action.constructedBuildingTypeId + ") at location (#" + targetLocationId + ").");
    try {
      db.industry.getBuildingByLocationId(targetLocationId)
      throw new BuildingAlreadyExistsAtTargetLocationException(targetLocationId);
    } catch {
      case e: ExpectedRecordNotFoundException => None;
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
        db.industry.changeBuildingTypeId(a.asInstanceOf[Building].id, action.upgradesToBuildingTypeId);
      }
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }
}