package com.warspite.insulae.database.meta

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object TroubleReportType {
  val fields = List("name") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new TroubleReportType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String]("name"))
  }

  def apply(a: TroubleReportType) = {
    new TroubleReportType(
      id = a.id,
      name = a.name);
  }
}

class TroubleReportType(id: Int, var name: String) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name)

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

