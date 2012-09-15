package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object ResourceRequiredNearActionTargetLocation extends StoredType {
  val fields = List("actionId", "resourceTypeId", "number", "maximumRange");

  def apply(r: DataRecord) = {
    new ResourceRequiredNearActionTargetLocation(
      actionId = r.get[Int]("actionId"),
      resourceTypeId = r.get[Int]("resourceTypeId"),
      number = r.get[Int]("number"),
      maximumRange = r.get[Int]("maximumRange"));
  }

  def apply(a: ResourceRequiredNearActionTargetLocation) = {
    new ResourceRequiredNearActionTargetLocation(
      actionId = a.actionId,
      resourceTypeId = a.resourceTypeId,
      number = a.number,
      maximumRange = a.maximumRange);
  }
}

class ResourceRequiredNearActionTargetLocation(var actionId: Int, var resourceTypeId: Int, var number: Int, var maximumRange: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "actionId" -> actionId,
      "resourceTypeId" -> resourceTypeId,
      "number" -> number,
      "maximumRange" -> maximumRange);

    return map
  }

  override def toString = "ResourceRequiredNearActionTargetLocation [Action: " + actionId + ", ResourceType: " + resourceTypeId + ", Number: + " + number + ", MaximumRange: " + maximumRange + "]";
}
