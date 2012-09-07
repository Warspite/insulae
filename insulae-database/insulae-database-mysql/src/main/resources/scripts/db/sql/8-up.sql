CREATE TABLE ActionItemOutput (
	actionId int NOT NULL,
	itemTypeId int NOT NULL,
    amount int NOT NULL
);

INSERT INTO Action (name, description, canonicalName, actionPointCost, constructedBuildingTypeId, requiresLocationId, maximumRange) VALUES ('Cut lumber', 'Cut lumber into wooden planks.', 'cutLumber', 3, NULL, FALSE, 0);

INSERT INTO ActionItemOutput (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutWood'), (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1);
INSERT INTO ActionItemOutput (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutLumber'), (SELECT id FROM ItemType WHERE canonicalName='woodenPlanks'), 1);

INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutLumber'), (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1);

INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='sawmill'), (SELECT id FROM Action WHERE canonicalName='cutLumber'));
