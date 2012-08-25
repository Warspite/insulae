package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object ActionItemCost extends StoredType {
  val fields = List("actionId", "itemTypeId", "amount");

  def apply(r: DataRecord) = {
    new ActionItemCost(
      actionId = r.get[Int]("actionId"),
      itemTypeId = r.get[Int]("itemTypeId"),
      amount = r.get[Int]("amount"))
  }

  def apply(a: ActionItemCost) = {
    new ActionItemCost(
      actionId = a.actionId,
      itemTypeId = a.itemTypeId,
      amount = a.amount)
  }
}

class ActionItemCost(var actionId: Int, var itemTypeId: Int, var amount: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "actionId" -> actionId,
      "itemTypeId" -> itemTypeId,
      "amount" -> amount)

    return map
  }
}

