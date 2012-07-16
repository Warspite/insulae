package com.warspite.insulae.database.geography
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import scala.collection.mutable.Queue

class MySqlInsulaeGeographyDatabase(connection: Connection) extends MySqlQueryer(connection) with GeographyDatabase {
	def getAreaById(id: Int): Area = {
	  val r = query(Area.fields, "FROM Area WHERE id = " + id);
	  return Area(r.next(true).getOrElse(throw new AreaIdDoesNotExistException(id)));
	}
	
	def getAreaByRealmId(realmId: Int): Array[Area] = {
	  val r = query(Area.fields, "FROM Area WHERE realmId = " + realmId);
	  return r.buildArray[Area](Area.apply);
	}
}
