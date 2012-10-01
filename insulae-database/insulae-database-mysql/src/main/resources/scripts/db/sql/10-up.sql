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
