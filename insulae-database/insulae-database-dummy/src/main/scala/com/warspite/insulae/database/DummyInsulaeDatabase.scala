package com.warspite.insulae.database
import com.warspite.insulae.database.account.DummyInsulaeAccountDatabase
import com.warspite.insulae.database.world.DummyInsulaeWorldDatabase
import com.warspite.insulae.database.geography.DummyInsulaeGeographyDatabase
import java.util.Properties

class DummyInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
	val accountDb = new DummyInsulaeAccountDatabase;
	val worldDb = new DummyInsulaeWorldDatabase;
	val geographyDb = new DummyInsulaeGeographyDatabase;
	
	def account = accountDb;
	def world = worldDb;
	def geography = geographyDb;
	
	def connect() {
	}	
}
