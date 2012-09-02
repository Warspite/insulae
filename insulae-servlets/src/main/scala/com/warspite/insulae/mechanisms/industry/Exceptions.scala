package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.ItemStorage

class UnrecognizedAgentTypeException(agent: Object) extends RuntimeException ("Received an unrecognized type of agent: " + agent.getClass()) {}
class ItemTransactionException(msg: String) extends RuntimeException(msg) {}
class DepositFailedException(hub: Building, item: ItemStorage) extends ItemTransactionException("Failed to make deposit of " + item + " to " + hub);
