package com.warspite.insulae.database.meta

import org.scala_tools.time.Imports._

class DummyInsulaeMetaDatabase extends MetaDatabase {
  def getTroubleReportTypeById(id: Int): TroubleReportType = {
    new TroubleReportType(1, "Type1");
  };

  def getTroubleReportTypeAll(): Array[TroubleReportType] = {
    Array(new TroubleReportType(1, "Type1"), new TroubleReportType(2, "Type2"));
  }

  def putTroubleReport(tr: TroubleReport) {}
}
