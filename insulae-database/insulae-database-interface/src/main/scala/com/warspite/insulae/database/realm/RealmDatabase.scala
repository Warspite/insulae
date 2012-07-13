package com.warspite.insulae.database.realm

trait RealmDatabase {
	def getRealmById(id: Int): Realm;
	def getRealmAll(): Array[Realm];
}