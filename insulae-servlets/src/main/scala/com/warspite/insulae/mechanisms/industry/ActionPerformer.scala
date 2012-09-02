package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.ItemStorage

class ActionPerformer(val db: InsulaeDatabase, val transactor: ItemTransactor) {
  protected val logger = LoggerFactory.getLogger(getClass());
  
  def perform(action: Action, agent: Object) {
    logger.debug("Performing " + action + " with agent " + agent);
    
    val industryHub = determineIndustryHub(agent);
    transactor.deposit(industryHub, ItemStorage(industryHub.id, db.industry.getActionItemOutputByActionId(action.id)));
  }
  
  def determineIndustryHub(agent: Object): Building = {
    agent match {
      case agent: Building => return db.industry.getBuildingById(agent.asInstanceOf[Building].industryHubBuildingId);
      case _ => throw new UnrecognizedAgentTypeException(agent);
    }
  }
}