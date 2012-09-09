package com.warspite.insulae.database.meta

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object TroubleReportType extends StoredType {
  val fields = List("id", "name");

  def apply(r: DataRecord) = {
    new TroubleReportType(
      id = r.get[Int]("id"),
      name = r.get[String]("name"))
  }

  def apply(a: TroubleReportType) = {
    new TroubleReportType(
      id = a.id,
      name = a.name);
  }
}

class TroubleReportType(var id: Int, var name: String) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name)

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
}

