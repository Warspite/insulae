package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item

class ActionPerformer(val db: InsulaeDatabase, val transactor: ItemTransactor) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def perform(action: Action, agent: Object) {
    logger.debug("Performing " + action + " with agent " + agent);

    var transactionKey = transactor.acquireLock();
    try {
      val industryHub = determineIndustryHub(agent);
      transactor.deposit(industryHub, Item(db.industry.getActionItemOutputByActionId(action.id)), transactionKey);
    } 
    finally {
    	transactor.releaseLock(transactionKey);
    }
  }

  def determineIndustryHub(agent: Object): Building = {
    agent match {
      case agent: Building => return db.industry.getBuildingById(agent.asInstanceOf[Building].industryHubBuildingId);
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }
}