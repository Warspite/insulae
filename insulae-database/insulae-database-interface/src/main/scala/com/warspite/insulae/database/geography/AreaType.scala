package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.DescriptiveType

object AreaType {
  val STARTING_AREA_OF_RACE_ID = "startingAreaOfRaceId";
  val fields = List(STARTING_AREA_OF_RACE_ID) ++ DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new AreaType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME),
      startingAreaOfRaceId = r.get[Int](STARTING_AREA_OF_RACE_ID));
  }

  def apply(a: AreaType) = {
    new AreaType(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName,
      startingAreaOfRaceId = a.startingAreaOfRaceId);
  }
}

class AreaType(id: Int, name: String, description: String, canonicalName: String, var startingAreaOfRaceId: Int) extends DescriptiveType(id, name, description, canonicalName) {

  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      AreaType.STARTING_AREA_OF_RACE_ID -> startingAreaOfRaceId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

