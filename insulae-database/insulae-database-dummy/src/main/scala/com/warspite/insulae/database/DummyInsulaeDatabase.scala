package com.warspite.insulae.database
import com.warspite.insulae.database.account.DummyInsulaeAccountDatabase
import com.warspite.insulae.database.world.DummyInsulaeWorldDatabase
import java.util.Properties

class DummyInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
	val accountDb = new DummyInsulaeAccountDatabase;
	val worldDb = new DummyInsulaeWorldDatabase;
	
	def account = accountDb;
	def world = worldDb;
	
	def connect() {
	}	
}
