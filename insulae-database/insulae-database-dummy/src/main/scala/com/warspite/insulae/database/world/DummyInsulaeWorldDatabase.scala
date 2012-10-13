package com.warspite.insulae.database.world

import org.scala_tools.time.Imports._

class DummyInsulaeWorldDatabase extends WorldDatabase {
	def getRealmById(id: Int): Realm = {
	  new Realm(id, "name!", "canonicalName!", DateTime.now, DateTime.now + 3.months);
	}

	def getRealmByCanonicalName(canonicalName: String): Realm = {
	  new Realm(1, "name!", canonicalName, DateTime.now, DateTime.now + 3.months);
	}
	
	def getRealmAll(): Array[Realm] = {
	  Array[Realm](new Realm(1, "Starting realm", "startingRealm", DateTime.now, DateTime.now + 3.months), new Realm(2, "Future realm", "futureRealm", DateTime.now + 4.months, DateTime.now + 7.months));
	}

	def getRaceById(id: Int): Race = {
	  new Race(id, "name!", "description!", "canonicalName!", 6);
	}
	
	def getRaceAll(): Array[Race] = {
	  Array[Race](new Race(1, "firstRace", "firstDescription", "firstRace", 5), new Race(2, "secondRace", "secondDescription", "secondRace", 3));
	}
	
	def getRaceByRealmId(realmId: Int): Array[Race] = {
	  return getRaceAll();
	}

	def getSexById(id: Int): Sex = {
	  new Sex(id, 1, "name!", "title!", "description!");
	}
	
	def getSexByRaceId(raceId: Int): Array[Sex] = {
	  Array[Sex](new Sex(1, raceId, "firstSex", "firstTitle", "firstDescription"), new Sex(2, raceId, "secondSex", "secondTitle", "secondDescription"));
	}

	def getSexAll(): Array[Sex] = {
	  Array[Sex](new Sex(1, 1, "firstSex", "firstTitle", "firstDescription"), new Sex(2, 2, "secondSex", "secondTitle", "secondDescription"));
	}

	def getAvatarById(id: Int): Avatar = {
	  new Avatar(id, 1, 1, 1, 1, "name!");
	}
	
	def getAvatarByNameAndRealm(name: String, realmId: Int): Avatar = {
	  new Avatar(1, 1, realmId, 1, 1, name);
	}
	
	def getAvatarByAccountId(accountId: Int): Array[Avatar] = {
	  Array[Avatar](new Avatar(1, accountId, 1, 1, 1, "name1!"), new Avatar(2, accountId, 1, 1, 1, "name2!"));
	}

	def getAvatarByRealmId(realmId: Int): Array[Avatar] = {
	  Array[Avatar](new Avatar(1, 1, realmId, 1, 1, "name1!"), new Avatar(2, 1, realmId, 1, 1, "name2!"));
	}
	
	def putAvatar(a: Avatar): Avatar = {
	  a;
	}
}
