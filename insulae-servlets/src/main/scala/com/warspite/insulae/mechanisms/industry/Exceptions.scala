package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.insulae.database.industry.ItemStorage
import com.warspite.common.servlets.ClientReadableException

class UnrecognizedAgentTypeException(agent: Object) extends RuntimeException ("Received an unrecognized type of agent: " + agent.getClass()) {}

class InsufficientActionPointsException(apCost: Int, availableAp: Double) extends ClientReadableException("Action prevented by lack of actionPoints (" + apCost + ">" + availableAp + ").", "Darn, you don't have enough action points! You need " + apCost + ", but there's only " + availableAp + " available."); 

class ItemTransactionException(msg: String) extends RuntimeException(msg) {}
class DepositFailedException(hub: Building, item: Item) extends ItemTransactionException("Failed to make deposit of " + item + " to " + hub);
class WithdrawalFailedException(buildingId: Int, item: Item) extends ItemTransactionException("Failed to make withdrawal of " + item + " from building #" + buildingId);
class FailedToAcquireTransactionLockException() extends ItemTransactionException("Failed to acquire a transaction lock! Maybe there is some blocking transaction.");
class IncorrectTransactionKeyException(key: Long, lock: Long) extends ItemTransactionException("Incorrect transaction key used. Key: " + key + ", lock: " + lock);
class NoBuildingsToWithdrawFromProvidedException() extends ItemTransactionException("Could not make withdrawal, because the list of buildings to withdraw from was empty.");
class InsufficientItemStorageForWithdrawalException(val withdrawal: Item, validStorages: Array[ItemStorage]) extends ItemTransactionException("Failed to withdraw " + withdrawal + " due to insufficient storage. Valid storages to withdraw from: " + validStorages.deep.mkString);

