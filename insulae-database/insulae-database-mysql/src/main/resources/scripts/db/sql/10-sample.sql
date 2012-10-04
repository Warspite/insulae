INSERT INTO StartingLocation (raceId, locationId) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), 4);
INSERT INTO StartingLocation (raceId, locationId) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), 14);

INSERT INTO AreaType(name, canonicalName, description, startingAreaOfRaceId) VALUES ('Sample volcanic', 'sampleVolcanic', 'A sample of a volcanic area type.', (SELECT id FROM Race WHERE canonicalName='balwerian'));
INSERT INTO AreaType(name, canonicalName, description, startingAreaOfRaceId) VALUES ('Sample desert', 'sampleDesert', 'A sample of a desert area type.', (SELECT id FROM Race WHERE canonicalName='balwerian'));

UPDATE Area SET areaTypeId=(SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic') WHERE id=2;

INSERT INTO AreaTemplate(areaTypeId) VALUES ((SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'));
INSERT INTO AreaTemplate(areaTypeId) VALUES ((SELECT id FROM AreaType WHERE canonicalName='sampleDesert'));