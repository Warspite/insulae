package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object ItemStorage extends StoredType {
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

class ItemStorage(var buildingId: Int, var itemTypeId: Int, var amount: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "buildingId" -> buildingId,
      "itemTypeId" -> itemTypeId,
      "amount" -> amount)

    return map
  }
}

