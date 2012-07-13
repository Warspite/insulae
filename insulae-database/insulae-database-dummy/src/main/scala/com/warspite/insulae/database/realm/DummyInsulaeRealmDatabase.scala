package com.warspite.insulae.database.realm

import org.scala_tools.time.Imports._

class DummyInsulaeRealmDatabase extends RealmDatabase {
	def getRealmById(id: Int): Realm = {
	  new Realm(id, "name!", DateTime.now, DateTime.now + 3.months);
	}

	def getRealmAll(): Array[Realm] = {
	  Array[Realm](new Realm(1, "startingRealm", DateTime.now, DateTime.now + 3.months), new Realm(2, "futureRealm", DateTime.now + 4.months, DateTime.now + 7.months));
	}
}
