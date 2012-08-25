package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object Action extends StoredType {
  val fields = List("id", "name", "description", "canonicalName", "actionPointCost", "constructedBuildingTypeId"); 

  def apply(r: DataRecord) = {
    new Action(
      id = r.get[Int]("id"),
      name= r.get[String]("name"),
      description = r.get[String]("description"),
      canonicalName = r.get[String]("canonicalName"),
      actionPointCost= r.get[Int]("actionPointCost"),
      constructedBuildingTypeId = r.get[Int]("constructedBuildingTypeId"));
  }

  def apply(a: Action) = {
    new Action(
     id = a.id,
     name= a.name,
     description = a.description,
     canonicalName = a.canonicalName,
     actionPointCost= a.actionPointCost,
     constructedBuildingTypeId = a.constructedBuildingTypeId)
  }
}

class Action(var id: Int, var name: String, var description: String, var canonicalName: String, var actionPointCost: Int, var constructedBuildingTypeId: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
     "name" -> name,
     "description" -> description, 
     "canonicalName" -> canonicalName,
     "actionPointCost" -> actionPointCost,
     "constructedBuildingTypeId" -> constructedBuildingTypeId)

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
}
