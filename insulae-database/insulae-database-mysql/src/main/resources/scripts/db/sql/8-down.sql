DELETE FROM ActionByBuildingType WHERE buildingTypeId = (SELECT id FROM BuildingType WHERE canonicalName='sawmill') AND actionId = (SELECT id FROM Action WHERE canonicalName='cutLumber');
DELETE FROM ActionByBuildingType WHERE buildingTypeId = (SELECT id FROM BuildingType WHERE canonicalName='villageSquare') AND actionId = (SELECT id FROM Action WHERE canonicalName='constructWoodcutter');
DELETE FROM ActionByBuildingType WHERE buildingTypeId = (SELECT id FROM BuildingType WHERE canonicalName='townSquare') AND actionId = (SELECT id FROM Action WHERE canonicalName='constructWoodcutter');

DELETE FROM ActionItemCost WHERE actionId = (SELECT id FROM Action WHERE canonicalName='cutLumber');
DELETE FROM ActionItemCost WHERE actionId = (SELECT id FROM Action WHERE canonicalName='constructWoodcutter');

DROP TABLE IF EXISTS ActionItemOutput;

DELETE FROM Action WHERE canonicalName='cutLumber';
DELETE FROM Action WHERE canonicalName='constructWoodcutter';
