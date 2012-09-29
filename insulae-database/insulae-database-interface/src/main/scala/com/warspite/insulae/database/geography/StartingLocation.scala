package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.StoredType

object StartingLocation extends {
  val RACEID = "raceId";
  val LOCATIONID = "locationId";
  val fields = List(RACEID, LOCATIONID) ++ StoredType.fields;

  def apply(r: DataRecord) = {
    new StartingLocation(
      raceId = r.get[Int](StartingLocation.RACEID),
      locationId = r.get[Int](StartingLocation.LOCATIONID));
  }

  def apply(a: StartingLocation) = {
    new StartingLocation(
      raceId = a.raceId,
      locationId = a.locationId);
  }
}

class StartingLocation(var raceId: Int, var locationId: Int) extends StoredType() {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      StartingLocation.RACEID -> raceId,
      StartingLocation.LOCATIONID -> locationId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}
