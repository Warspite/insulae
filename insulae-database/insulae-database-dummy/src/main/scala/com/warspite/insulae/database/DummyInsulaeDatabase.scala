package com.warspite.insulae.database
import com.warspite.insulae.database.account.DummyInsulaeAccountDatabase
import com.warspite.insulae.database.realm.DummyInsulaeRealmDatabase
import java.util.Properties

class DummyInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
	val accountDb = new DummyInsulaeAccountDatabase;
	val realmDb = new DummyInsulaeRealmDatabase;
	
	def account = accountDb;
	def realm = realmDb;
	
	def connect() {
	}	
}
