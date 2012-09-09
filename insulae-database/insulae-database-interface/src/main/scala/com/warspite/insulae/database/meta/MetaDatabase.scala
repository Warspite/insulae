package com.warspite.insulae.database.meta

trait MetaDatabase {
	def getTroubleReportTypeById(id: Int): TroubleReportType;
	def getTroubleReportTypeAll(): Array[TroubleReportType];
	
	def putTroubleReport(tr: TroubleReport);
}