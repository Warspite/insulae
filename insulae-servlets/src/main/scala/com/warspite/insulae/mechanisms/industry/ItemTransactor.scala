package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import scala.util.Random
import com.warspite.common.cli.WarspitePoller

object ItemTransactor {
  val LOCK_TIMEOUT = 1000;
  val ACQUIRE_LOCK_TIMEOUT = 2000;
  val ACQUIRE_LOCK_INTERVAL = 10;
  val LOCK_TIMEOUT_POLL_INTERVAL = 500;

  var lock: Long = 0;
  var lockAcquisitionMonitor = "";
  var lockUsageMonitor = "";
}

class ItemTransactor(val db: InsulaeDatabase) extends WarspitePoller(ItemTransactor.LOCK_TIMEOUT_POLL_INTERVAL) {
  override def setup() {}
  override def teardown() {}

  override def act() {
    ItemTransactor.lockUsageMonitor.synchronized {
      if (ItemTransactor.lock != 0 && System.currentTimeMillis() - ItemTransactor.lock > ItemTransactor.LOCK_TIMEOUT) {
        logger.debug("Resetting timed out lock.");
        releaseLock(ItemTransactor.lock);
      }
    }
  }

  def acquireLock(): Long = {
    ItemTransactor.lockAcquisitionMonitor.synchronized {
      var startTime = System.currentTimeMillis();

      while (ItemTransactor.lock != 0) {
        if (System.currentTimeMillis() - startTime > ItemTransactor.ACQUIRE_LOCK_TIMEOUT)
          throw new FailedToAcquireTransactionLockException();

        Thread.sleep(ItemTransactor.ACQUIRE_LOCK_INTERVAL);
      }

      ItemTransactor.lock = System.currentTimeMillis();
      logger.debug("Transaction lock " + ItemTransactor.lock + " acquired.");
      return ItemTransactor.lock;
    }
  }

  def releaseLock(key: Long) {
    ItemTransactor.lockUsageMonitor.synchronized {
      if (!keyIsValid(key))
        throw new IncorrectTransactionKeyException(key, ItemTransactor.lock);

      logger.debug("Transaction lock " + key + " released.");
      ItemTransactor.lock = 0;
    }
  }

  def keyIsValid(key: Long) = ItemTransactor.lock != 0 && ItemTransactor.lock == key;

  def withdraw(hub: Building, items: Array[Item], key: Long) {
    ItemTransactor.lockUsageMonitor.synchronized {
      if (!keyIsValid(key))
        throw new IncorrectTransactionKeyException(key, ItemTransactor.lock);

      logger.debug("Withdrawing " + items.length + " item types (industry hub " + hub + ").");
    }
  }

  def deposit(hub: Building, items: Array[Item], key: Long) {
    ItemTransactor.lockUsageMonitor.synchronized {
      if (!keyIsValid(key))
        throw new IncorrectTransactionKeyException(key, ItemTransactor.lock);

      logger.debug("Depositing " + items.length + " item types (industry hub " + hub + ").");
      for (item <- items) {
        if (!db.industry.changeItemStorageAmount(hub.id, item.itemTypeId, item.amount))
          throw new DepositFailedException(hub, item);
      }
    }
  }
}