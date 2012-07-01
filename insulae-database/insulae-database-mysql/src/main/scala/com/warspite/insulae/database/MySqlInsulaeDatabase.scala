package com.warspite.insulae.database
import com.warspite.insulae.database.account.MySqlInsulaeAccountDatabase
import java.util.Properties

class MySqlInsulaeDatabase(props: Properties) extends InsulaeDatabase(props) {
	val accountDb = new MySqlInsulaeAccountDatabase;
	
	def account = accountDb;
}
