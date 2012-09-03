package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object Item extends StoredType {
  val fields = List("buildingId", "itemTypeId", "amount");

  def apply(r: DataRecord) = {
    new Item(
      itemTypeId = r.get[Int]("itemTypeId"),
      amount = r.get[Int]("amount"))
  }

  def apply(a: Item) = {
    new Item(
      itemTypeId = a.itemTypeId,
      amount = a.amount)
  }

  def apply(a: Array[ActionItemCost]): Array[Item] = {
    a.map(cost => new Item(cost.itemTypeId, cost.amount));
  }

  def apply(a: Array[ActionItemOutput]): Array[Item] = {
    a.map(output => new Item(output.itemTypeId, output.amount));
  }
}

class Item(var itemTypeId: Int, var amount: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "itemTypeId" -> itemTypeId,
      "amount" -> amount)

    return map
  }

  override def toString = "Item [itemTypeId: " + itemTypeId + ", amount: " + amount + "]";
}

