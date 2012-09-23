package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType

object Action extends {
  val fields = List("actionPointCost", "constructedBuildingTypeId", "requiresLocationId", "maximumRange", "upgradesToBuildingTypeId") ++ DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new Action(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME),
      actionPointCost = r.get[Int]("actionPointCost"),
      constructedBuildingTypeId = r.get[Int]("constructedBuildingTypeId"),
      requiresLocationId = r.get[Boolean]("requiresLocationId"),
      maximumRange = r.get[Int]("maximumRange"),
      upgradesToBuildingTypeId = r.get[Int]("upgradesToBuildingTypeId"));
  }

  def apply(a: Action) = {
    new Action(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName,
      actionPointCost = a.actionPointCost,
      constructedBuildingTypeId = a.constructedBuildingTypeId,
      requiresLocationId = a.requiresLocationId,
      maximumRange = a.maximumRange,
      upgradesToBuildingTypeId = a.upgradesToBuildingTypeId);
  }
}

class Action(id: Int, name: String, description: String, canonicalName: String, var actionPointCost: Int, var constructedBuildingTypeId: Int, var requiresLocationId: Boolean, var maximumRange: Int, var upgradesToBuildingTypeId: Int) extends DescriptiveType(id, name, description, canonicalName) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "actionPointCost" -> actionPointCost,
      "constructedBuildingTypeId" -> constructedBuildingTypeId,
      "requiresLocationId" -> requiresLocationId,
      "maximumRange" -> maximumRange,
      "upgradesToBuildingTypeId" -> upgradesToBuildingTypeId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
  
  def constructsBuilding = constructedBuildingTypeId != 0;
}
