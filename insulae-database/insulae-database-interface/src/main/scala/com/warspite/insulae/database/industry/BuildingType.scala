package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType

object BuildingType {
  val fields = List("raceId", "transportationTypeId", "maximumActionPoints", "actionPointRegenerationRate", "industryHubRange") ++ DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new BuildingType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME),
      raceId = r.get[Int]("raceId"),
      transportationTypeId = r.get[Int]("transportationTypeId"),
      maximumActionPoints = r.get[Int]("maximumActionPoints"),
      actionPointRegenerationRate = r.get[Double]("actionPointRegenerationRate"),
      industryHubRange = r.get[Int]("industryHubRange"))
  }

  def apply(a: BuildingType) = {
    new BuildingType(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName,
      raceId = a.raceId,
      transportationTypeId = a.transportationTypeId,
      maximumActionPoints = a.maximumActionPoints,
      actionPointRegenerationRate = a.actionPointRegenerationRate,
      industryHubRange = a.industryHubRange)
  }
}

class BuildingType(id: Int, name: String, description: String, canonicalName: String, var raceId: Int, var transportationTypeId: Int, var maximumActionPoints: Int, var actionPointRegenerationRate: Double, var industryHubRange: Int) extends DescriptiveType(id, name, description, canonicalName) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "raceId" -> raceId,
      "transportationTypeId" -> transportationTypeId,
      "maximumActionPoints" -> maximumActionPoints,
      "actionPointRegenerationRate" -> actionPointRegenerationRate,
      "industryHubRange" -> industryHubRange);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }

  def isIndustryHub = industryHubRange > 0;
}

