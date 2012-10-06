package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.ItemHoardingOrder
import com.warspite.insulae.database.industry.Item
import org.slf4j.LoggerFactory
import com.warspite.common.database.ExpectedRecordNotFoundException

class ItemHoarder(val db: InsulaeDatabase, val transactor: ItemTransactor) {
  val logger = LoggerFactory.getLogger(getClass());
  
  def satisfyAllUnsatisfied() {
    for (order <- db.industry.getUnsatisfiedOrderedItemHoardingOrderAll())
      hoardItems(order);
  }

  def hoardItems(order: ItemHoardingOrder) {
    val hoardingBuilding = db.industry.getBuildingById(order.buildingId);

    if (hoardingBuilding.isIndustryHub)
      return ;
    
    val hub = db.industry.getBuildingById(hoardingBuilding.industryHubBuildingId); 
    var transactionKey = transactor.acquireLock();
    try {
      val hubStorage = db.industry.getItemStorageByBuildingIdAndItemTypeId(hoardingBuilding.industryHubBuildingId, order.itemTypeId);
      val transfer = new Item(order.itemTypeId, scala.math.min(order.amount, hubStorage.amount));

      logger.debug("Hoarding " + order + " from " + hub + " (actual amount " + transfer.amount + ")");
      
      transactor.withdraw(transactionKey, Array(transfer), Seq(hub));
      transactor.deposit(transactionKey, Array(transfer), hoardingBuilding);

      logger.debug("Successfully hoarded " + order + " from " + hub + " (actual amount " + transfer.amount + ")");
    } catch {
      case e: ExpectedRecordNotFoundException => return ;
    } finally {
      transactor.releaseLock(transactionKey);
    }

  }
}