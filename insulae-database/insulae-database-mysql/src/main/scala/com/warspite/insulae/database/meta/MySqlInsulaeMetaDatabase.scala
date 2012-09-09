package com.warspite.insulae.database.meta
import com.warspite.common.database.sql.MySqlQueryer
import java.sql.Connection

class MySqlInsulaeMetaDatabase(connection: Connection) extends MySqlQueryer(connection) with MetaDatabase {
  def getTroubleReportTypeById(id: Int): TroubleReportType = {
    val r = query(TroubleReportType.fields, "FROM TroubleReportType WHERE id = " + id);
    return TroubleReportType(r.next(true).getOrElse(throw new TroubleReportTypeIdDoesNotExistException(id)));
  }

  def getTroubleReportTypeAll(): Array[TroubleReportType] = {
    val r = query(TroubleReportType.fields, "FROM TroubleReportType");
    return r.buildArray[TroubleReportType](TroubleReportType.apply);
  }

  def putTroubleReport(tr: TroubleReport) {
    insert("TroubleReport", tr.asMap(false, true));
  }
}