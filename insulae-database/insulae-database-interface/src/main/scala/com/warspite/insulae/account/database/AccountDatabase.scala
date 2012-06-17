package com.warspite.insulae.account.database

trait AccountDatabase {
	def getAccountById(id: Int): Account;
	def getAccountByEmail(email: String): Account;
	def putAccount(a: Account);
}