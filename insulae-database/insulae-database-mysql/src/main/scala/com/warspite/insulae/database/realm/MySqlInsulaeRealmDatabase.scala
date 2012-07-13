package com.warspite.insulae.database.realm
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import com.warspite.insulae.database.realm._
import scala.collection.mutable.Queue

class MySqlInsulaeRealmDatabase(connection: Connection) extends MySqlQueryer(connection) with RealmDatabase {
	def getRealmById(id: Int): Realm = {
	  val r = query(Realm.fields, "FROM Realm WHERE id=" + id);
	  return Realm(r.next(true).getOrElse(throw new RealmIdDoesNotExistException(id)));
	}
	
	def getRealmAll(): Array[Realm] = {
	  val r = query(Realm.fields, "FROM Realm");
	  
	  var q = new Queue[Realm]();
	  var done = false;
	  while (!done) r.next() match {
	    case Some(x) => q += Realm(x);
	    case None => done = true;
	  }

	  q.toArray;
	}
	
}
