INSERT INTO StartingLocation (raceId, locationId) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), 4);
INSERT INTO StartingLocation (raceId, locationId) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), 14);

INSERT INTO AreaType(name, canonicalName, description, startingAreaOfRaceId) VALUES ('Sample volcanic', 'sampleVolcanic', 'A sample of a volcanic area type.', (SELECT id FROM Race WHERE canonicalName='balwerian'));
INSERT INTO AreaType(name, canonicalName, description, startingAreaOfRaceId) VALUES ('Sample desert', 'sampleDesert', 'A sample of a desert area type.', (SELECT id FROM Race WHERE canonicalName='balwerian'));

UPDATE Area SET areaTypeId=(SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic') WHERE id=2;

INSERT INTO AreaTemplate(areaTypeId) VALUES ((SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'));
SET @volcanicTemplateId = LAST_INSERT_ID();
INSERT INTO AreaTemplate(areaTypeId) VALUES ((SELECT id FROM AreaType WHERE canonicalName='sampleDesert'));
SET @desertTemplateId = LAST_INSERT_ID();

INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@volcanicTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 0, 0, FALSE, 0, (SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'), TRUE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@volcanicTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 0, 1, FALSE, (SELECT id FROM Race WHERE canonicalName='balwerian'), 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@volcanicTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 1, 0, TRUE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@volcanicTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 1, 1, TRUE, 0, 0, TRUE);
	
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 0, 0, FALSE, 0, (SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'), FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 0, 1, FALSE, (SELECT id FROM Race WHERE canonicalName='balwerian'), 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 1, 0, FALSE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='plains'), 1, 1, FALSE, 0, (SELECT id FROM AreaType WHERE canonicalName='sampleDesert'), FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 2, 0, FALSE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 2, 1, FALSE, 0, 0, TRUE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='rockyHills'), 3, 0, FALSE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='plains'), 3, 1, FALSE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='plains'), 4, 0, FALSE, 0, 0, TRUE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='plains'), 4, 1, FALSE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='plains'), 5, 0, FALSE, 0, 0, FALSE);
INSERT INTO LocationTemplate(areaTemplateId, locationTypeId, coordinatesX, coordinatesY, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible) VALUES (@desertTemplateId, (SELECT id FROM LocationType WHERE canonicalName='plains'), 5, 1, FALSE, (SELECT id FROM Race WHERE canonicalName='balwerian'), 0, FALSE);
	
INSERT INTO AreaName (name, areaTypeId) VALUES ("Some volcanic island", (SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("Some other volcanic island", (SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("Super volcano", (SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("Volcano Riviera", (SELECT id FROM AreaType WHERE canonicalName='sampleVolcanic'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("Some desert island", (SELECT id FROM AreaType WHERE canonicalName='sampleDesert'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("The Dry Place", (SELECT id FROM AreaType WHERE canonicalName='sampleDesert'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("The Very Dry Place", (SELECT id FROM AreaType WHERE canonicalName='sampleDesert'));
INSERT INTO AreaName (name, areaTypeId) VALUES ("Parchemonium", (SELECT id FROM AreaType WHERE canonicalName='sampleDesert'));