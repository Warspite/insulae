package com.warspite.insulae.database.account

class MySqlInsulaeAccountDatabase extends AccountDatabase {
	def getAccountById(id: Int): Account = {
	  new Account(id, "email!", "hashelihash", "MySql-Scud", "Daniel", "Franzen");
	}

	def getAccountByEmail(email: String): Account = {
	  new Account(1, email, "hashelihash", "MySql", "Daniel", "Franzen");
	}
	
	def putAccount(a: Account) {
	  
	}
}
