package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.DescriptiveType

object AreaTemplate {
  val AREA_TYPE_ID = "areaTypeId";
  val fields = List(AREA_TYPE_ID) ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new AreaTemplate(
      id = r.get[Int](IdentifiedType.ID),
      areaTypeId = r.get[Int](AreaTemplate.AREA_TYPE_ID));
  }

  def apply(a: AreaTemplate) = {
    new AreaTemplate(
      id = a.id,
      areaTypeId = a.areaTypeId);
  }
}

class AreaTemplate(id: Int, var areaTypeId: Int) extends IdentifiedType(id) {

  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      AreaTemplate.AREA_TYPE_ID -> areaTypeId);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

