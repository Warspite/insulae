package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object BuildingType extends StoredType {
  val fields = List("id", "name", "description", "canonicalName", "raceId", "transportationTypeId", "maximumActionPoints", "actionPointRegenerationRate", "industryHubRange");

  def apply(r: DataRecord) = {
    new BuildingType(
      id = r.get[Int]("id"),
      name = r.get[String]("name"),
      description = r.get[String]("description"),
      canonicalName = r.get[String]("canonicalName"),
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

class BuildingType(var id: Int, var name: String, var description: String, var canonicalName: String, var raceId: Int, var transportationTypeId: Int, var maximumActionPoints: Int, var actionPointRegenerationRate: Double, var industryHubRange: Int) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "description" -> description,
      "canonicalName" -> canonicalName,
      "raceId" -> raceId,
      "transportationTypeId" -> transportationTypeId,
      "maximumActionPoints" -> maximumActionPoints,
      "actionPointRegenerationRate" -> actionPointRegenerationRate,
      "industryHubRange" -> industryHubRange);

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
  
  def isIndustryHub = industryHubRange > 0;
}

