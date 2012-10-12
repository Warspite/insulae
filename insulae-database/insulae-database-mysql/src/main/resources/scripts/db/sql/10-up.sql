CREATE TABLE StartingBuilding (
    raceId int NOT NULL,
	buildingTypeId int NOT NULL,
	deltaX int NOT NULL,
	deltaY int NOT NULL
);

ALTER TABLE StartingBuilding ADD CONSTRAINT fkStartingBuildingRaceId FOREIGN KEY (raceId) REFERENCES Race (id);
ALTER TABLE StartingBuilding ADD CONSTRAINT fkStartingBuildingBuildingTypeId FOREIGN KEY (buildingTypeId) REFERENCES BuildingType (id);

INSERT INTO StartingBuilding (raceId, buildingTypeId, deltaX, deltaY) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), (SELECT id FROM BuildingType WHERE canonicalName='villageSquare'), 0, 0);
INSERT INTO StartingBuilding (raceId, buildingTypeId, deltaX, deltaY) VALUES ((SELECT id FROM Race WHERE canonicalName='balwerian'), (SELECT id FROM BuildingType WHERE canonicalName='woodcutter'), -1, 0);

CREATE TABLE StartingLocation (
    raceId int NOT NULL,
	locationId int NOT NULL
);

ALTER TABLE StartingLocation ADD CONSTRAINT fkStartingLocationRaceId FOREIGN KEY (raceId) REFERENCES Race (id);
ALTER TABLE StartingLocation ADD CONSTRAINT fkStartingLocationLocationId FOREIGN KEY (locationId) REFERENCES Location (id);

ALTER TABLE Race ADD COLUMN minimumStartingLocationClearRadius int DEFAULT 5;

ALTER TABLE Realm ADD COLUMN canonicalName varchar(30) NOT NULL DEFAULT "unset";
UPDATE Realm SET canonicalName = CONCAT("realm", id);
ALTER TABLE Realm ADD UNIQUE(canonicalName);

ALTER TABLE LocationType ADD COLUMN color varchar(6) NOT NULL DEFAULT "unset";
UPDATE LocationType SET color='bfba28' WHERE canonicalName='plains';
UPDATE LocationType SET color='10581f' WHERE canonicalName='forest';
UPDATE LocationType SET color='72bd82' WHERE canonicalName='grassyHills';
UPDATE LocationType SET color='bdb572' WHERE canonicalName='rockyHills';
ALTER TABLE LocationType ADD UNIQUE(color);

CREATE TABLE AreaType (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(40) NOT NULL,
	canonicalName varchar(30) NOT NULL UNIQUE,
	description text NOT NULL,
	startingAreaOfRaceId int NOT NULL DEFAULT 0
);

INSERT INTO AreaType(name, canonicalName, description, startingAreaOfRaceId) VALUES ('Generic', 'generic', 'Generic area type.', 0);

ALTER TABLE Area ADD COLUMN areaTypeId int NOT NULL DEFAULT 1;
ALTER TABLE Area ADD CONSTRAINT fkAreaAreaTypeId FOREIGN KEY (areaTypeId) REFERENCES AreaType (id);

CREATE TABLE AreaTemplate (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	areaTypeId int NOT NULL
);

ALTER TABLE AreaTemplate ADD CONSTRAINT fkAreaTemplateAreaTypeId FOREIGN KEY (areaTypeId) REFERENCES AreaType (id);

CREATE TABLE LocationTemplate (
	areaTemplateId int NOT NULL,
	locationTypeId int NOT NULL,
	coordinatesX int NOT NULL,
	coordinatesY int NOT NULL,
	road boolean NOT NULL,
	startingLocationOfRaceId int NOT NULL,
	portalToAreaTypeId int NOT NULL,
	incomingPortalPossible boolean NOT NULL
);

ALTER TABLE LocationTemplate ADD CONSTRAINT fkLocationTemplateAreaTemplateId FOREIGN KEY (areaTemplateId) REFERENCES AreaTemplate (id);
ALTER TABLE LocationTemplate ADD CONSTRAINT fkLocationTemplateLocationTypeId FOREIGN KEY (locationTypeId) REFERENCES LocationType (id);
ALTER TABLE LocationTemplate ADD UNIQUE ikLocationTemplateAreaTemplateIdCoordinates (areaTemplateId, coordinatesX, coordinatesY);

ALTER TABLE Area DROP COLUMN description;
ALTER TABLE Area ADD UNIQUE ikAreaRealmIdCoordinates (realmId, coordinatesX, coordinatesY);
ALTER TABLE Location ADD UNIQUE ikLocationAreaIdCoordinates (areaId, coordinatesX, coordinatesY);
ALTER TABLE Location ADD COLUMN incomingPortalPossible boolean NOT NULL DEFAULT FALSE;

CREATE TABLE AreaName (
	name varchar(30) NOT NULL,
	areaTypeId int NOT NULL
);

ALTER TABLE AreaName ADD CONSTRAINT fAreaNameAreaTypeId FOREIGN KEY (areaTypeId) REFERENCES AreaType (id);

CREATE TABLE ResourceOccurrence (
	areaTypeId int NOT NULL,
	locationTypeId int NOT NULL,
	resourceTypeId int NOT NULL,
	occurrence float NOT NULL
);

ALTER TABLE ResourceOccurrence ADD CONSTRAINT fResourceOccurrenceAreaTypeId FOREIGN KEY (areaTypeId) REFERENCES AreaType (id);
ALTER TABLE ResourceOccurrence ADD CONSTRAINT fResourceOccurrenceLocationTypeId FOREIGN KEY (locationTypeId) REFERENCES LocationType (id);
ALTER TABLE ResourceOccurrence ADD CONSTRAINT fResourceOccurrenceResourceTypeId FOREIGN KEY (resourceTypeId) REFERENCES ResourceType (id);
