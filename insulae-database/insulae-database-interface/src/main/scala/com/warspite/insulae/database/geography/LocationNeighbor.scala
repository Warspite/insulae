package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.StoredType

object LocationNeighbor {
  val fields = List("locationId", "neighborLocationId");

  def apply(r: DataRecord) = {
    new LocationNeighbor(
      locationId = r.get[Int]("locationId"),
      neighborLocationId = r.get[Int]("neighborLocationId"))
  }

  def apply(a: LocationNeighbor) = {
    new LocationNeighbor(
      locationId = a.locationId,
      neighborLocationId = a.neighborLocationId)
  }
}

class LocationNeighbor(var locationId: Int, var neighborLocationId: Int) extends StoredType {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "locationId" -> locationId,
      "neighborLocationId" -> neighborLocationId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

