ALTER TABLE Location DROP COLUMN incomingPortalPossible;
ALTER TABLE Location DROP INDEX ikLocationAreaIdCoordinates;
ALTER TABLE Area ADD COLUMN description text;
UPDATE Area, AreaType SET Area.description=AreaType.description WHERE AreaType.id = Area.areaTypeId;

DROP TABLE IF EXISTS LocationTemplate;
DROP TABLE IF EXISTS AreaTemplate;

ALTER TABLE Area DROP FOREIGN KEY fkAreaAreaTypeId;
ALTER TABLE Area DROP COLUMN areaTypeId;

DROP TABLE IF EXISTS AreaType;

ALTER TABLE Race DROP COLUMN minimumStartingLocationClearRadius;

DROP TABLE IF EXISTS StartingLocation;
DROP TABLE IF EXISTS StartingBuilding;
