package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object TransportationCost extends StoredType {
  val fields = List("locationTypeId", "transportationTypeId", "costWithoutRoad", "costWithRoad");

  def apply(r: DataRecord) = {
    new TransportationCost(
      locationTypeId = r.get[Int]("locationTypeId"),
      transportationTypeId = r.get[Int]("transportationTypeId"),
      costWithoutRoad = r.get[Int]("costWithoutRoad"),
      costWithRoad = r.get[Int]("costWithRoad"))
  }

  def apply(a: TransportationCost) = {
    new TransportationCost(
      locationTypeId = a.locationTypeId,
      transportationTypeId = a.transportationTypeId,
      costWithoutRoad = a.costWithoutRoad,
      costWithRoad = a.costWithRoad)
  }
}

class TransportationCost(var locationTypeId: Int, var transportationTypeId: Int, var costWithoutRoad: Int, var costWithRoad: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "locationTypeId" -> locationTypeId,
      "transportationTypeId" -> transportationTypeId,
      "costWithoutRoad" -> costWithoutRoad,
      "costWithRoad" -> costWithRoad)

    return map
  }
}

