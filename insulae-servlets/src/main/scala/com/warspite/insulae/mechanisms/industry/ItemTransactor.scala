package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Action
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import scala.util.Random
import com.warspite.common.cli.WarspitePoller
import scala.collection.mutable.Queue
import com.warspite.insulae.database.industry.ItemStorage

object ItemTransactor {
  val LOCK_TIMEOUT = 1000;
  val ACQUIRE_LOCK_TIMEOUT = 2000;
  val ACQUIRE_LOCK_INTERVAL = 10;
  val LOCK_TIMEOUT_POLL_INTERVAL = 500;

  var lock: Long = 0;
}

class ItemTransactor(val db: InsulaeDatabase) extends WarspitePoller(ItemTransactor.LOCK_TIMEOUT_POLL_INTERVAL) {
  override def setup() {}
  override def teardown() {}

  override def act() {
    ItemTransactor.synchronized {
      if (ItemTransactor.lock != 0 && System.currentTimeMillis() - ItemTransactor.lock > ItemTransactor.LOCK_TIMEOUT) {
        logger.debug("Resetting timed out lock.");
        releaseLock(ItemTransactor.lock);
      }
    }
  }

  def acquireLock(): Long = {
    var startTime = System.currentTimeMillis();

    while (System.currentTimeMillis() - startTime < ItemTransactor.ACQUIRE_LOCK_TIMEOUT) {
      ItemTransactor.synchronized {
        if (ItemTransactor.lock == 0) {
          ItemTransactor.lock = System.currentTimeMillis();
          logger.debug("Transaction lock " + ItemTransactor.lock + " acquired.");
          return ItemTransactor.lock;
        }
      }

      Thread.sleep(ItemTransactor.ACQUIRE_LOCK_INTERVAL);
    }

    throw new FailedToAcquireTransactionLockException();
  }

  def releaseLock(key: Long) {
    ItemTransactor.synchronized {
      if (!keyIsValid(key))
        throw new IncorrectTransactionKeyException(key, ItemTransactor.lock);

      logger.debug("Transaction lock " + key + " released.");
      ItemTransactor.lock = 0;
    }
  }

  def keyIsValid(key: Long) = ItemTransactor.lock != 0 && ItemTransactor.lock == key;

  def withdraw(key: Long, items: Array[Item], storageBuildings: Seq[Building]) {
    if (storageBuildings.isEmpty)
      throw new NoBuildingsToWithdrawFromProvidedException();

    ItemTransactor.synchronized {
      if (!keyIsValid(key))
        throw new IncorrectTransactionKeyException(key, ItemTransactor.lock);

      logger.debug("Withdrawing " + items.deep.mkString + " from " + storageBuildings.mkString + ".");

      for ((buildingId, items) <- getWithdrawalsByBuilding(items, storageBuildings)) {
        for (item <- items) {
          if (!db.industry.changeItemStorageAmount(buildingId, item.itemTypeId, -item.amount))
            throw new WithdrawalFailedException(buildingId, item);
        }
      }
    }
  }

  def deposit(key: Long, items: Array[Item], hub: Building) {
    ItemTransactor.synchronized {
      if (!keyIsValid(key))
        throw new IncorrectTransactionKeyException(key, ItemTransactor.lock);

      for (item <- items) {
        if (!db.industry.changeItemStorageAmount(hub.id, item.itemTypeId, item.amount))
          throw new DepositFailedException(hub, item);
      }
    }
  }

  def getWithdrawalsByBuilding(items: Array[Item], buildings: Seq[Building]): scala.collection.mutable.Map[Int, Array[Item]] = {
    val withdrawalsByBuilding = scala.collection.mutable.Map[Int, Array[Item]]();
    val availableStorages = getAvailableStorages(buildings);

    for (item <- items)
      splitWithdrawalByAvailableStorage(withdrawalsByBuilding, availableStorages, item);

    return withdrawalsByBuilding;
  }

  def getAvailableStorages(buildings: Seq[Building]): Array[ItemStorage] = {
    var availableStorages = Array[ItemStorage]();
    for (b <- buildings)
      availableStorages = availableStorages ++ db.industry.getItemStorageByBuildingId(b.id);

    return availableStorages;
  }

  def splitWithdrawalByAvailableStorage(withdrawalsByBuilding: scala.collection.mutable.Map[Int, Array[Item]], availableStorages: Array[ItemStorage], withdrawal: Item) {
    var remainingAmount = withdrawal.amount;
    val validStorages = availableStorages.filter(s => s.itemTypeId == withdrawal.itemTypeId && s.amount > 0);

    for (storage <- validStorages) {
      var amountToWithdraw = scala.math.min(storage.amount, remainingAmount);
      if (!withdrawalsByBuilding.contains(storage.buildingId))
        withdrawalsByBuilding += storage.buildingId -> Array[Item]();

      withdrawalsByBuilding(storage.buildingId) = withdrawalsByBuilding(storage.buildingId) :+ new Item(withdrawal.itemTypeId, amountToWithdraw);

      remainingAmount -= amountToWithdraw;
      if (remainingAmount == 0)
        return;
    }

    throw new InsufficientItemStorageForWithdrawalException(withdrawal, validStorages);
  }
}