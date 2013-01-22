package com.warspite.insulae.database.world
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import scala.collection.mutable.Queue

class MySqlInsulaeWorldDatabase(connection: Connection) extends MySqlQueryer(connection) with WorldDatabase {
	def getRealmById(id: Int): Realm = {
	  val r = query(Realm.fields, "FROM Realm WHERE id = " + id);
	  return r.buildSingle(Realm.apply);
	}
	
	def getRealmByCanonicalName(canonicalName: String): Realm = {
	  val r = query(Realm.fields, "FROM Realm WHERE canonicalName = '" + StringEscaper.escape(canonicalName) + "'");
	  return r.buildSingle(Realm.apply);
	}
	
	def getRealmAll(): Array[Realm] = {
	  val r = query(Realm.fields, "FROM Realm");
	  return r.buildArray[Realm](Realm.apply);
	}
	
	def getRaceById(id: Int): Race = {
	  val r = query(Race.fields, "FROM Race WHERE id = " + id);
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

	def getSexAll(): Array[Sex] = {
	  val r = query(Sex.fields, "FROM Sex");
	  return r.buildArray[Sex](Sex.apply);
	}
	
	def getSexById(id: Int): Sex = {
	  val r = query(Sex.fields, "FROM Sex WHERE id = " + id);
	  return Sex(r.next(true).getOrElse(throw new SexIdDoesNotExistException(id)));
	}
	
	def getSexByRaceId(raceId: Int): Array[Sex] = {
	  val r = query(Sex.fields, "FROM Sex WHERE raceId = " + raceId);
	  return r.buildArray[Sex](Sex.apply);
	}

	def getAvatarById(id: Int): Avatar = {
	  val r = query(Avatar.fields, "FROM Avatar WHERE id = " + id);
	  return Avatar(r.next(true).getOrElse(throw new AvatarIdDoesNotExistException(id)));
	}
	
	def getAvatarByNameAndRealm(name: String, realmId: Int): Avatar = {
	  val r = query(Avatar.fields, "FROM Avatar WHERE name = '" + StringEscaper.escape(name) + "' AND realmId = " + realmId);
	  return Avatar(r.next(true).getOrElse(throw new AvatarNameDoesNotExistException(name)));
	}
	
	def getAvatarByAccountId(accountId: Int): Array[Avatar] = {
	  val r = query(Avatar.fields, "FROM Avatar WHERE accountId = " + accountId);
	  return r.buildArray[Avatar](Avatar.apply);
	}

	def getAvatarByRealmId(realmId: Int): Array[Avatar] = {
	  val r = query(Avatar.fields, "FROM Avatar WHERE realmId = " + realmId);
	  return r.buildArray[Avatar](Avatar.apply);
	}

	def putAvatar(a: Avatar): Avatar = {
	  try {
	    val existingAvatar = getAvatarByNameAndRealm(a.name, a.realmId);
	    throw new AvatarNameAlreadyExistsException(a.name, a.realmId);
	  }
	  catch {
	    case e: AvatarNameDoesNotExistException => None;
	  }
	  
	  if(getSexById(a.sexId).raceId != a.raceId)
	    throw new AvatarDataInconsistentException("sexId", "raceId");
	  
	  if(!getRaceByRealmId(a.realmId).exists(raceInRealm => raceInRealm.id == a.raceId))
	    throw new AvatarDataInconsistentException("raceId", "realmId");
	  
	  insert("Avatar", a.transient(false).sensitive(true).asMap());
	  
	  return getAvatarByNameAndRealm(a.name, a.realmId);
	}
}
