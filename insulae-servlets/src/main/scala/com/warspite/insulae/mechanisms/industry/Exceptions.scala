package com.warspite.insulae.mechanisms.industry
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.Item
import com.warspite.insulae.database.industry.ItemStorage
import com.warspite.common.servlets.ClientReadableException
import com.warspite.insulae.database.industry.Action
import com.warspite.insulae.database.geography.Location

class InsufficientActionPointsException(apCost: Int, availableAp: Double) extends ClientReadableException("Action prevented by lack of actionPoints (" + apCost + ">" + availableAp + ").", "Darn, you don't have enough action points! You need " + apCost + ", but there's only " + availableAp + " available."); 
class RequiredTargetLocationIdMissingException(action: Action) extends ClientReadableException("Unable to perform " + action + " due to missing targetLocationId.", "There seems to be an error with the handling of your action. It requires a location target, but none was supplied."); 
class BuildingAlreadyExistsAtTargetLocationException(targetLocation: Location) extends ClientReadableException("Attempted to perform construction at " + targetLocation + ", which is already occupied.", "That location already has a building! And there can be only one...");
class RoadAlreadyExistsAtTargetLocationException(targetLocation: Location) extends ClientReadableException("Attempted to perform construction at " + targetLocation + ", which is already occupied by a road.", "That location already has a road! And there can be only one...");
class MaximumActionRangeExceededException(maximumRange: Int) extends ClientReadableException("Attempted to perform action outside of range.", "The maximum range of the action you tried to perform is " + maximumRange + ", but the target is further away than that!");
class AgentIsNotCapableOfPerformingActionException(action: Action, agent: Object) extends ClientReadableException("Attempted to perform " + action + " with agent " + agent + ".", "The agent you selected is unable to perform " + action.name + ".");
class RequiredLocationTypesNotFoundNearTargetLocationException(targetLocation: Location, action: Action, missingLocationTypeId: Int) extends ClientReadableException("Could not perform " + action + " at " + targetLocation + " due to missing location type " + missingLocationTypeId + ".", "The action you attempted requires certain location types to be present near the target location. These were missing.");
class RequiredResourcesNotFoundNearTargetLocationException(targetLocation: Location, action: Action, missingResourceTypeId: Int) extends ClientReadableException("Could not perform " + action + " at " + targetLocation + " due to missing resource type " + missingResourceTypeId + ".", "The action you attempted requires certain resources to be present near the target location. These were missing.");

class ItemTransactionException(msg: String) extends RuntimeException(msg) {}
class DepositFailedException(hub: Building, item: Item) extends ItemTransactionException("Failed to make deposit of " + item + " to " + hub);
class WithdrawalFailedException(buildingId: Int, item: Item) extends ItemTransactionException("Failed to make withdrawal of " + item + " from building #" + buildingId);
class FailedToAcquireTransactionLockException() extends ItemTransactionException("Failed to acquire a transaction lock! Maybe there is some blocking transaction.");
class IncorrectTransactionKeyException(key: Long, lock: Long) extends ItemTransactionException("Incorrect transaction key used. Key: " + key + ", lock: " + lock);
class NoBuildingsToWithdrawFromProvidedException() extends ItemTransactionException("Could not make withdrawal, because the list of buildings to withdraw from was empty.");
class InsufficientItemStorageForWithdrawalException(val withdrawal: Item, validStorages: Array[ItemStorage]) extends ItemTransactionException("Failed to withdraw " + withdrawal + " due to insufficient storage. Valid storages to withdraw from: " + validStorages.deep.mkString);

class CustomEffectException(msg: String) extends RuntimeException(msg) {}
class InvalidCustomEffectArgumentException(msg: String) extends ItemTransactionException(msg);
