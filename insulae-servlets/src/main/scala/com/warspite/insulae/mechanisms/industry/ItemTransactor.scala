package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.ItemStorage

class ItemTransactor(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());
  
  def withdraw(hub: Building, items: Array[ItemStorage]) {
    logger.debug("Withdrawing " + items.length + " item types (industry hub " + hub + ").");
  }
  
  def deposit(hub: Building, items: Array[ItemStorage]) {
    logger.debug("Depositing " + items.length + " item types (industry hub " + hub + ").");
    for(item <- items) {
      if(!db.industry.changeItemStorageAmount(hub.id, item.itemTypeId, item.amount))
        throw new DepositFailedException(hub, item);
    }
  }
}