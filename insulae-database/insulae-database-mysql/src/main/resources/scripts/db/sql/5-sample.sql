INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (3, (SELECT id FROM BuildingType WHERE canonicalName='woodCutter'), (SELECT id FROM Avatar WHERE name='avatar1'), 10, 0);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (1, (SELECT id FROM BuildingType WHERE canonicalName='grainFarm'), (SELECT id FROM Avatar WHERE name='avatar1'), 0, 0);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (2, (SELECT id FROM BuildingType WHERE canonicalName='vegetableFarm'), (SELECT id FROM Avatar WHERE name='avatar1'), 3, 5);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (8, (SELECT id FROM BuildingType WHERE canonicalName='villageSquare'), (SELECT id FROM Avatar WHERE name='avatar1'), 17, 10);

INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (10, (SELECT id FROM BuildingType WHERE canonicalName='woodCutter'), (SELECT id FROM Avatar WHERE name='avatar2'), 10, 0);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (12, (SELECT id FROM BuildingType WHERE canonicalName='grainFarm'), (SELECT id FROM Avatar WHERE name='avatar2'), 10, 0);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (13, (SELECT id FROM BuildingType WHERE canonicalName='townSquare'), (SELECT id FROM Avatar WHERE name='avatar3'), 10, 0);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (14, (SELECT id FROM BuildingType WHERE canonicalName='sawmill'), (SELECT id FROM Avatar WHERE name='avatar3'), 10, 0);
INSERT INTO Building(locationId, buildingTypeId, avatarId, actionPoints, reservedActionPoints) VALUES (15, (SELECT id FROM BuildingType WHERE canonicalName='windmill'), (SELECT id FROM Avatar WHERE name='avatar2'), 10, 0);

