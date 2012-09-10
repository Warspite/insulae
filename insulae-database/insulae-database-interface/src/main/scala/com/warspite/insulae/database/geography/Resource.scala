package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object Resource extends StoredType {
  val fields = List("resourceTypeId", "locationId");

  def apply(r: DataRecord) = {
    new Resource(
      resourceTypeId = r.get[Int]("resourceTypeId"),
      locationId = r.get[Int]("locationId"))
  }

  def apply(a: Resource) = {
    new Resource(
      resourceTypeId = a.resourceTypeId,
      locationId = a.locationId);
  }
}

class Resource(var resourceTypeId: Int, var locationId: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "resourceTypeId" -> resourceTypeId,
      "locationId" -> locationId);

    return map
  }
  
  override def toString = "Resource: [typeId: " + resourceTypeId + ", locationId: " + locationId + "]";
}

