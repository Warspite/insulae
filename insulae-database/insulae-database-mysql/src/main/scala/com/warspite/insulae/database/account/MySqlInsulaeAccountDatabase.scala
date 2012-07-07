package com.warspite.insulae.database.account
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord

class MySqlInsulaeAccountDatabase(connection: Connection) extends MySqlQueryer(connection) with AccountDatabase {
	def getAccountById(id: Int): Account = {
	  val r = query(Account.fields, "FROM Account WHERE id=" + id);
	  return Account(r.next(true).getOrElse(throw new ExpectedRecordNotFoundException("There is no account with id " + id + ".")));
	}

	def getAccountByEmail(email: String): Account = {
	  val r = query(Account.fields, "FROM Account WHERE email='" + email + "'");
	  return Account(r.next(true).getOrElse(throw new ExpectedRecordNotFoundException("There is no account with email '" + email + "'.")));
	}
	
	def putAccount(a: Account) {
	  
	}
}
