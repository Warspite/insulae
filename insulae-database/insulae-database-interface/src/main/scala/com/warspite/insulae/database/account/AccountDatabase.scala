package com.warspite.insulae.database.account

trait AccountDatabase {
	def getAccountById(id: Int): Account;
	def getAccountByEmail(email: String): Account;
	def getAccountByCallSign(callSign: String): Account;
	def putAccount(a: Account): Account;
}