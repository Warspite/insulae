DELETE FROM ActionByBuildingType WHERE buildingTypeId = (SELECT id FROM BuildingType WHERE canonicalName='sawmill') AND actionId = (SELECT id FROM Action WHERE canonicalName='cutLumber');

DELETE FROM ActionItemCost WHERE actionId = (SELECT id FROM Action WHERE canonicalName='cutLumber');

DROP TABLE IF EXISTS ActionItemOutput;

DELETE FROM Action WHERE canonicalName='cutLumber';
