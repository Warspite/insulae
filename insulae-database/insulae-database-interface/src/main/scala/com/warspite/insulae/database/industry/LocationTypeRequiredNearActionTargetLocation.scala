package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.StoredType

object LocationTypeRequiredNearActionTargetLocation {
  val fields = List("actionId", "locationTypeId", "number", "maximumRange");

  def apply(r: DataRecord) = {
    new LocationTypeRequiredNearActionTargetLocation(
      actionId = r.get[Int]("actionId"),
      locationTypeId = r.get[Int]("locationTypeId"),
      number = r.get[Int]("number"),
      maximumRange = r.get[Int]("maximumRange"));
  }

  def apply(a: LocationTypeRequiredNearActionTargetLocation) = {
    new LocationTypeRequiredNearActionTargetLocation(
      actionId = a.actionId,
      locationTypeId = a.locationTypeId,
      number = a.number,
      maximumRange = a.maximumRange);
  }
}

class LocationTypeRequiredNearActionTargetLocation(var actionId: Int, var locationTypeId: Int, var number: Int, var maximumRange: Int) extends StoredType {
  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      "actionId" -> actionId,
      "locationTypeId" -> locationTypeId,
      "number" -> number,
      "maximumRange" -> maximumRange);

    return map ++ super.asMap();
  }

  override def toString = "LocationTypeRequiredNearActionTargetLocation [Action: " + actionId + ", LocationType: " + locationTypeId + ", Number: + " + number + ", MaximumRange: " + maximumRange + "]";
}
