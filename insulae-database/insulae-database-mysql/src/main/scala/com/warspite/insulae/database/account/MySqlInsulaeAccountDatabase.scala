package com.warspite.insulae.database.account
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import com.warspite.insulae.database.account._

class MySqlInsulaeAccountDatabase(connection: Connection) extends MySqlQueryer(connection) with AccountDatabase {
	def getAccountById(id: Int): Account = {
	  val r = query(Account.fields, "FROM Account WHERE id=" + id);
	  return Account(r.next(true).getOrElse(throw new AccountIdDoesNotExistException(id)));
	}

	def getAccountByEmail(email: String): Account = {
	  val r = query(Account.fields, "FROM Account WHERE email='" + StringEscaper.escape(email) + "'");
	  return Account(r.next(true).getOrElse(throw new AccountEmailDoesNotExistException(email)));
	}
	
	def getAccountByCallSign(callSign: String): Account = {
	  val r = query(Account.fields, "FROM Account WHERE callSign='" + StringEscaper.escape(callSign) + "'");
	  return Account(r.next(true).getOrElse(throw new AccountCallSignDoesNotExistException(callSign)));
	}
	
	def putAccount(a: Account): Account = {
	  try {
	    val existingAccount = getAccountByEmail(a.email);
	    throw new AccountEmailAlreadyExistsException(a.email);
	  }
	  catch {
	    case e: AccountEmailDoesNotExistException => None
	  }
	  
	  try {
	    val existingAccount = getAccountByCallSign(a.callSign);
	    throw new AccountCallSignAlreadyExistsException(a.callSign);
	  }
	  catch {
	    case e: AccountCallSignDoesNotExistException => None
	  }
	  
	  insert("Account", a.sensitive(true).transient(true).asMap());
	  
	  return getAccountByEmail(a.email);
	}
}
