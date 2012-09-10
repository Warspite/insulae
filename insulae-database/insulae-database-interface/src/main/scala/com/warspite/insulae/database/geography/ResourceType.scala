package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object ResourceType extends StoredType {
  val fields = List("id", "name", "canonicalName", "description");

  def apply(r: DataRecord) = {
    new ResourceType(
      id = r.get[Int]("id"),
      name = r.get[String]("name"),
      canonicalName = r.get[String]("canonicalName"),
      description = r.get[String]("description"))
  }

  def apply(a: ResourceType) = {
    new ResourceType(
      id = a.id,
      name = a.name,
      canonicalName = a.canonicalName,
      description = a.description);
  }
}

class ResourceType(var id: Int, var name: String, var canonicalName: String, var description: String) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "canonicalName" -> canonicalName,
      "description" -> description);

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
  
  override def toString = "ResourceType #" + id +": [" + canonicalName + "]";
}

