CREATE TABLE ActionItemOutput (
	actionId int NOT NULL,
	itemTypeId int NOT NULL,
    amount int NOT NULL
);

INSERT INTO Action (name, description, canonicalName, actionPointCost, constructedBuildingTypeId, requiresLocationId, maximumRange, upgradesToBuildingTypeId) VALUES ('Cut lumber', 'Cut lumber into wooden planks.', 'cutLumber', 3, NULL, FALSE, -1, NULL);
INSERT INTO Action (name, description, canonicalName, actionPointCost, constructedBuildingTypeId, requiresLocationId, maximumRange, upgradesToBuildingTypeId) VALUES ('Construct woodcutter\'s cottage', 'Construct a woodcutter\'s cottage at a nearby location.', 'constructWoodcutter', 12, (SELECT id FROM BuildingType WHERE canonicalName='woodcutter'), TRUE, -1, NULL);

INSERT INTO ActionItemOutput (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutWood'), (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1);
INSERT INTO ActionItemOutput (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutLumber'), (SELECT id FROM ItemType WHERE canonicalName='woodenPlanks'), 1);

INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutLumber'), (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1);
INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='constructWoodcutter'), (SELECT id FROM ItemType WHERE canonicalName='lumber'), 5);
INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='constructWoodcutter'), (SELECT id FROM ItemType WHERE canonicalName='grain'), 2);
INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='constructWoodcutter'), (SELECT id FROM ItemType WHERE canonicalName='vegetables'), 2);
INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='constructWoodcutter'), (SELECT id FROM ItemType WHERE canonicalName='woodenPlanks'), 3);

INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='sawmill'), (SELECT id FROM Action WHERE canonicalName='cutLumber'));
INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='villageSquare'), (SELECT id FROM Action WHERE canonicalName='constructWoodcutter'));
INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='townSquare'), (SELECT id FROM Action WHERE canonicalName='constructWoodcutter'));
