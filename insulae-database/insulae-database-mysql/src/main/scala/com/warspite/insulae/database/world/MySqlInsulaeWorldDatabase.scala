package com.warspite.insulae.database.world
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import com.warspite.insulae.database.realm._
import scala.collection.mutable.Queue

class MySqlInsulaeWorldDatabase(connection: Connection) extends MySqlQueryer(connection) with WorldDatabase {
	def getRealmById(id: Int): Realm = {
	  val r = query(Realm.fields, "FROM Realm WHERE id=" + id);
	  return Realm(r.next(true).getOrElse(throw new RealmIdDoesNotExistException(id)));
	}
	
	def getRealmAll(): Array[Realm] = {
	  val r = query(Realm.fields, "FROM Realm");
	  return r.buildArray[Realm](Realm.apply);
	}
	
	def getRaceById(id: Int): Race = {
	  val r = query(Race.fields, "FROM Race WHERE id=" + id);
	  return Race(r.next(true).getOrElse(throw new RaceIdDoesNotExistException(id)));
	}
	
	def getRaceAll(): Array[Race] = {
	  val r = query(Race.fields, "FROM Race");
	  return r.buildArray[Race](Race.apply);
	}
	
	def getRaceByRealmId(realmId: Int): Array[Race] = {
	  val r = query(Race.fields, "FROM Race, RaceByRealm WHERE RaceByRealm.realmId = " + realmId +" AND Race.id = RaceByRealm.raceId", "Race");
	  return r.buildArray[Race](Race.apply);
	}
}
