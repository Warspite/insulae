package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item

class UnrecognizedAgentTypeException(agent: Object) extends RuntimeException ("Received an unrecognized type of agent: " + agent.getClass()) {}

class ItemTransactionException(msg: String) extends RuntimeException(msg) {}
class DepositFailedException(hub: Building, item: Item) extends ItemTransactionException("Failed to make deposit of " + item + " to " + hub);
class FailedToAcquireTransactionLockException() extends ItemTransactionException("Failed to acquire a transaction lock! Maybe there is some blocking transaction.");
class IncorrectTransactionKeyException(key: Long, lock: Long) extends ItemTransactionException("Incorrect transaction key used. Key: " + key + ", lock: " + lock);