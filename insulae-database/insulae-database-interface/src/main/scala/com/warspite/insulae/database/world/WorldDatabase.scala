package com.warspite.insulae.database.world

trait WorldDatabase {
	def getRealmById(id: Int): Realm;
	def getRealmAll(): Array[Realm];

	def getRaceById(id: Int): Race;
	def getRaceByRealmId(realmId: Int): Array[Race];
	def getRaceAll(): Array[Race];
}