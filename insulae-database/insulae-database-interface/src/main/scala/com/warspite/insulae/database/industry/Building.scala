package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object Building extends StoredType {
  val fields = List("id", "locationId", "buildingTypeId", "avatarId", "actionPoints", "reservedActionPoints");

  def apply(r: DataRecord) = {
    new Building(
      id = r.get[Int]("id"),
      locationId = r.get[Int]("locationId"),
      buildingTypeId = r.get[Int]("buildingTypeId"),
      avatarId = r.get[Int]("avatarId"),
      actionPoints = r.get[Int]("actionPoints"),
      reservedActionPoints = r.get[Int]("reservedActionPoints"))
  }

  def apply(a: Building) = {
    new Building(
      id = a.id,
      locationId = a.locationId,
      buildingTypeId = a.buildingTypeId,
      avatarId = a.avatarId,
      actionPoints = a.actionPoints,
      reservedActionPoints = a.reservedActionPoints)
  }
}

class Building(var id: Int, var locationId: Int, var buildingTypeId: Int, var avatarId: Int, var actionPoints: Int, var reservedActionPoints: Int) extends Mappable {
  def asMap(includeId: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "locationId" -> locationId,
      "buildingTypeId" -> buildingTypeId,
      "avatarId" -> avatarId,
      "actionPoints" -> actionPoints,
      "reservedActionPoints" -> reservedActionPoints)

    if (includeId)
      map += "id" -> id;

    return map
  }
}

