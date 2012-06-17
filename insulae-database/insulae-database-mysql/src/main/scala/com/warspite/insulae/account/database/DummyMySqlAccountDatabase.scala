package com.warspite.insulae.account.database

class DummyMySqlAccountDatabase extends AccountDatabase {
	def getAccountById(id: Int): Account = {
	  new Account(id, "email!", "hashelihash", "Scud", "Daniel", "Franzen");
	}

	def getAccountByEmail(email: String): Account = {
	  new Account(1, email, "hashelihash", "Scud", "Daniel", "Franzen");
	}
	
	def putAccount(a: Account) {
	  
	}
}
