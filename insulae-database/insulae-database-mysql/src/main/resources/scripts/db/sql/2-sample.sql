INSERT INTO Realm(name, startDate, endDate) VALUES ('sampleOldRealm', '2011-01-01', '2011-06-30');
SET @sampleOldRealmId = LAST_INSERT_ID();
INSERT INTO Realm(name, startDate, endDate) VALUES ('sampleActiveRealm', '2012-06-01', '2050-06-30');
SET @sampleActiveRealmId = LAST_INSERT_ID();

INSERT INTO RaceByRealm (realmId, raceId) VALUES (@sampleOldRealmId, (SELECT id FROM Race WHERE name='Balwerian'));
INSERT INTO RaceByRealm (realmId, raceId) VALUES (@sampleActiveRealmId, (SELECT id FROM Race WHERE name='Balwerian'));
INSERT INTO RaceByRealm (realmId, raceId) VALUES (@sampleActiveRealmId, (SELECT id FROM Race WHERE name='Eilendel'));
