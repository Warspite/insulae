package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.StoredType

object StartingBuilding extends {
  val RACEID = "raceId";
  val BUILDINGTYPEID = "buildingTypeId";
  val DELTAX = "deltaX";
  val DELTAY = "deltaY";
  val fields = List(RACEID, BUILDINGTYPEID, DELTAX, DELTAY) ++ StoredType.fields;

  def apply(r: DataRecord) = {
    new StartingBuilding(
      raceId = r.get[Int](StartingBuilding.RACEID),
      buildingTypeId = r.get[Int](StartingBuilding.BUILDINGTYPEID),
      deltaX = r.get[Int](StartingBuilding.DELTAX),
      deltaY = r.get[Int](StartingBuilding.DELTAY));
  }

  def apply(a: StartingBuilding) = {
    new StartingBuilding(
      raceId = a.raceId,
      buildingTypeId = a.buildingTypeId,
      deltaX = a.deltaX,
      deltaY = a.deltaY);
  }
}

class StartingBuilding(var raceId: Int, var buildingTypeId: Int, var deltaX: Int, var deltaY: Int) extends StoredType() {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      StartingBuilding.RACEID -> raceId,
      StartingBuilding.BUILDINGTYPEID -> buildingTypeId,
      StartingBuilding.DELTAX -> deltaX,
      StartingBuilding.DELTAY -> deltaY);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}
