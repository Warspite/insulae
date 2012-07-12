package com.warspite.insulae.database.account

class DummyInsulaeAccountDatabase extends AccountDatabase {
	def getAccountById(id: Int): Account = {
	  new Account(id, "email!", "hashelihash", "Scud", "Daniel", "Franzen");
	}

	def getAccountByEmail(email: String): Account = {
	  new Account(1, email, "hashelihash", "Scud", "Daniel", "Franzen");
	}
	
	def putAccount(a: Account): Account = {
	  a;
	}
}
