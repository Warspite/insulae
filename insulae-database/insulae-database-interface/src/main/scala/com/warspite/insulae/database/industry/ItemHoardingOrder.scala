package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.StoredType

object ItemHoardingOrder {
  val fields = List("buildingId", "itemTypeId", "amount", "priority");

  def apply(r: DataRecord) = {
    new ItemHoardingOrder(
      buildingId = r.get[Int]("buildingId"),
      itemTypeId = r.get[Int]("itemTypeId"),
      amount = r.get[Int]("amount"),
      priority = r.get[Int]("priority"))
  }

  def apply(a: ItemHoardingOrder) = {
    new ItemHoardingOrder(
      buildingId = a.buildingId,
      itemTypeId = a.itemTypeId,
      amount = a.amount,
      priority = a.priority);
  }
}

class ItemHoardingOrder(var buildingId: Int, var itemTypeId: Int, var amount: Int, var priority: Int) extends StoredType {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "buildingId" -> buildingId,
      "itemTypeId" -> itemTypeId,
      "amount" -> amount,
      "priority" -> priority);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }

  override def toString = "ItemHoardingOrder [buildingId: " + buildingId + ", itemTypeId: " + itemTypeId + ", amount: " + amount + ", priority: " + priority + "]";
}

