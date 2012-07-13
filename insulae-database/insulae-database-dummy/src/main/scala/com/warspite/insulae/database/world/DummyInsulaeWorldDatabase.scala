package com.warspite.insulae.database.world

import org.scala_tools.time.Imports._

class DummyInsulaeWorldDatabase extends WorldDatabase {
	def getRealmById(id: Int): Realm = {
	  new Realm(id, "name!", DateTime.now, DateTime.now + 3.months);
	}

	def getRealmAll(): Array[Realm] = {
	  Array[Realm](new Realm(1, "startingRealm", DateTime.now, DateTime.now + 3.months), new Realm(2, "futureRealm", DateTime.now + 4.months, DateTime.now + 7.months));
	}

	def getRaceById(id: Int): Race = {
	  new Race(id, "name!", "description!");
	}
	
	def getRaceAll(): Array[Race] = {
	  Array[Race](new Race(1, "firstRace", "firstDescription"), new Race(2, "secondRace", "secondDescription"));
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
}
