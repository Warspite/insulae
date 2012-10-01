INSERT INTO StartingLocation (raceId, locationId) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), 4);
INSERT INTO StartingLocation (raceId, locationId) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), 14);

INSERT INTO AreaType(name, canonicalName, description, startingAreaOfRaceId) VALUES ('Sample volcanic', 'sampleVolcanic', 'A sample of a volcanic area type.', 0);

UPDATE Area SET areaTypeId=(SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic') WHERE id=2;
