package com.warspite.insulae.database.meta

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object TroubleReport extends StoredType {
  val fields = List("id", "troubleReportTypeId", "slogan", "content", "creationTime");

  def apply(r: DataRecord) = {
    new TroubleReport(
      id = r.get[Int]("id"),
      troubleReportTypeId = r.get[Int]("troubleReportTypeId"),
      slogan = r.get[String]("slogan"),
      content = r.get[String]("content"),
      creationTime = r.get[DateTime]("creationTime"))
  }

  def apply(a: TroubleReport) = {
    new TroubleReport(
      id = a.id,
      troubleReportTypeId = a.troubleReportTypeId,
      slogan = a.slogan,
      content = a.content,
      creationTime = a.creationTime)
  }
}

class TroubleReport(var id: Int, var troubleReportTypeId: Int, var slogan: String, var content: String, var creationTime: DateTime) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "troubleReportTypeId" -> troubleReportTypeId,
      "slogan" -> slogan,
      "content" -> content,
      "creationTime" -> creationTime)

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
  
  override def toString = "TroubleReport #" + id + ": [" + slogan + "@" + creationTime + ": " + content + "]";
}

