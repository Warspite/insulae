package com.warspite.insulae.database.geography

import org.scala_tools.time.Imports._

class DummyInsulaeGeographyDatabase extends GeographyDatabase {
	def getAreaById(id: Int): Area = {
	  new Area(id, "name!", "description", 1, 1, 1);
	}

	def getAreaByRealmId(realmId: Int): Array[Area] = {
	  Array[Area](new Area(1, "name!", "description", 1, 1, realmId), new Area(2, "other name!", "other description", 1, 2, realmId));
	}
}
