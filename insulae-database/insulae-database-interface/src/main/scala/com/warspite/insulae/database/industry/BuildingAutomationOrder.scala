package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.StoredType

object BuildingAutomationOrder {
  val fields = List("buildingId", "actionId");

  def apply(r: DataRecord) = {
    new BuildingAutomationOrder(
      buildingId = r.get[Int]("buildingId"),
      actionId = r.get[Int]("actionId"));
  }

  def apply(a: BuildingAutomationOrder) = {
    new BuildingAutomationOrder(
      buildingId = a.buildingId,
      actionId = a.actionId);
  }
}

class BuildingAutomationOrder(var buildingId: Int, var actionId: Int) extends StoredType {
  override def asMap(): Map[String, Any] = {
    var map = Map[String, Any](
      "buildingId" -> buildingId,
      "actionId" -> actionId);

    return map ++ super.asMap();
  }

  override def toString = "BuildingAutomation Order [buildingId: " + buildingId + ", actionId: " + actionId + "]";
}

