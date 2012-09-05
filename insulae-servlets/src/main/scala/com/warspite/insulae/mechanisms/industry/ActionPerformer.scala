package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item

class ActionPerformer(val db: InsulaeDatabase, val transactor: ItemTransactor) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def perform(action: Action, agent: Object) {
    synchronized {
      logger.debug("Performing " + action + " with agent " + agent);

      val securedActionPointCost = secureActionPointCost(action, agent);

      var transactionKey = transactor.acquireLock();
      try {
        val industryHub = determineIndustryHub(agent);

        transactor.withdraw(transactionKey, Item(db.industry.getActionItemCostByActionId(action.id)), determineAvailableItemStorages(agent, industryHub));
        transactor.deposit(transactionKey, Item(db.industry.getActionItemOutputByActionId(action.id)), industryHub);
        reduceActionPoints(agent, securedActionPointCost);
      } finally {
        transactor.releaseLock(transactionKey);
      }
    }
  }

  def determineIndustryHub(agent: Object): Building = {
    agent match {
      case agent: Building => return db.industry.getBuildingById(agent.asInstanceOf[Building].industryHubBuildingId);
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }

  def determineAvailableItemStorages(agent: Object, industryHub: Building): Seq[Building] = {
    agent match {
      case a: Building => return List(agent.asInstanceOf[Building], industryHub);
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
}