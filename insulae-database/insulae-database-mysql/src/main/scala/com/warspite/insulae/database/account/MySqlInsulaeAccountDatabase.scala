package com.warspite.insulae.database.account
import java.sql.Connection
import com.warspite.insulae.database.MySqlQueryer

class MySqlInsulaeAccountDatabase(connection: Connection) extends MySqlQueryer(connection) with AccountDatabase {
	def getAccountById(id: Int): Account = {
	  val t = query(List("id", "email", "passwordHash", "callSign", "givenName", "surname"), "FROM Account WHERE id=" + id, 1);
	  return Account(t.row(0));
	}

	def getAccountByEmail(email: String): Account = {
	  val t = query(List("id", "email", "passwordHash", "callSign", "givenName", "surname"), "FROM Account WHERE email='" + email + "'", 1);
	  return Account(t.row(0));
	}
	
	def putAccount(a: Account) {
	  
	}
}
