INSERT INTO Avatar(accountId, realmId, raceId, sexId, name) VALUES (1, (SELECT id FROM Realm WHERE name='sampleOldRealm'), (SELECT id FROM Race WHERE name='Balwerian'), 1, 'avatar1');
INSERT INTO Avatar(accountId, realmId, raceId, sexId, name) VALUES (1, (SELECT id FROM Realm WHERE name='sampleActiveRealm'), (SELECT id FROM Race WHERE name='Eilendel'), 3, 'avatar2');
INSERT INTO Avatar(accountId, realmId, raceId, sexId, name) VALUES (2, (SELECT id FROM Realm WHERE name='sampleActiveRealm'), (SELECT id FROM Race WHERE name='Balwerian'), 2, 'avatar3');
