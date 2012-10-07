package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.StoredType

object AreaName {
  val NAME = "name";
  val AREA_TYPE_ID = "areaTypeId";
  val fields = List(NAME, AREA_TYPE_ID) ++ StoredType.fields;

  def apply(r: DataRecord) = {
    new AreaName(
      name = r.get[String](AreaName.NAME),
      areaTypeId = r.get[Int](AreaName.AREA_TYPE_ID));
  }

  def apply(a: AreaName) = {
    new AreaName(
      name = a.name,
      areaTypeId = a.areaTypeId);
  }
}

class AreaName(var name: String, var areaTypeId: Int) extends StoredType {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      AreaName.NAME -> name,
      AreaName.AREA_TYPE_ID -> areaTypeId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

