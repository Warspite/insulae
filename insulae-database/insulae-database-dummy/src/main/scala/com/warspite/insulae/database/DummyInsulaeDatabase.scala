package com.warspite.insulae.database
import com.warspite.insulae.database.account.DummyInsulaeAccountDatabase
import java.util.Properties

class DummyInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
	val accountDb = new DummyInsulaeAccountDatabase;
	
	def account = accountDb;
}
