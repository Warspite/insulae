package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.StoredType

object ItemStorage {
  val fields = List("buildingId", "itemTypeId", "amount");

  def apply(r: DataRecord) = {
    new ItemStorage(
      buildingId = r.get[Int]("buildingId"),
      itemTypeId = r.get[Int]("itemTypeId"),
      amount = r.get[Int]("amount"))
  }

  def apply(a: ItemStorage) = {
    new ItemStorage(
      buildingId = a.buildingId,
      itemTypeId = a.itemTypeId,
      amount = a.amount)
  }
}

class ItemStorage(var buildingId: Int, var itemTypeId: Int, var amount: Int) extends StoredType {
  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      "buildingId" -> buildingId,
      "itemTypeId" -> itemTypeId,
      "amount" -> amount)

    return map ++ super.asMap();
  }

  override def toString = "ItemStorage [buildingId: " + buildingId + ", itemTypeId: " + itemTypeId + ", amount: " + amount + "]";
}

