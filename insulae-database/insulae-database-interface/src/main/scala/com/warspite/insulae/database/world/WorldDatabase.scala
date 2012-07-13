package com.warspite.insulae.database.world

trait WorldDatabase {
	def getRealmById(id: Int): Realm;
	def getRealmAll(): Array[Realm];

	def getRaceById(id: Int): Race;
	def getRaceByRealmId(realmId: Int): Array[Race];
	def getRaceAll(): Array[Race];

	def getSexById(id: Int): Sex;
	def getSexByRaceId(raceId: Int): Array[Sex];

	def getAvatarById(id: Int): Avatar;
	def getAvatarByNameAndRealm(name: String, realmId: Int): Avatar;
	def getAvatarByAccountId(accountId: Int): Array[Avatar];
	def getAvatarByRealmId(realmId: Int): Array[Avatar];
	def putAvatar(a: Avatar): Avatar;
}