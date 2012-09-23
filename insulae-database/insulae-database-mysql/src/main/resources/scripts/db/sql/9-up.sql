CREATE TABLE TroubleReportType (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(30) NOT NULL
);

CREATE TABLE TroubleReport (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	troubleReportTypeId int NOT NULL,
	slogan varchar(50) NOT NULL,
	content text NOT NULL,
	creationTime timestamp NOT NULL 
);

ALTER TABLE TroubleReport ADD CONSTRAINT fkTroubleReportTroubleReportTypeId
FOREIGN KEY (troubleReportTypeId) REFERENCES TroubleReportType (id);

INSERT INTO TroubleReportType (name) VALUES ('GUI Improvement');
INSERT INTO TroubleReportType (name) VALUES ('GUI Error');
INSERT INTO TroubleReportType (name) VALUES ('Game Mechanics Improvement');
INSERT INTO TroubleReportType (name) VALUES ('Game Mechanics Error');
INSERT INTO TroubleReportType (name) VALUES ('Other');

CREATE TABLE ResourceType (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(40) NOT NULL,
	canonicalName varchar(30) NOT NULL UNIQUE,
	description text NOT NULL
);

CREATE TABLE Resource (
    resourceTypeId int NOT NULL,
    locationId int NOT NULL
);

ALTER TABLE Resource ADD CONSTRAINT fkResourceResourceTypeId FOREIGN KEY (resourceTypeId) REFERENCES ResourceType (id);
ALTER TABLE Resource ADD UNIQUE ikResourceResourceTypeIdLocationId (resourceTypeId, locationId);

INSERT INTO ResourceType(name, canonicalName, description) VALUES ('Gems', 'gems', 'Valuable gems are plentiful in this area.');
INSERT INTO ResourceType(name, canonicalName, description) VALUES ('Gold', 'gold', 'Rich gold veins have been discovered in this area.');
INSERT INTO ResourceType(name, canonicalName, description) VALUES ('Iron', 'iron', 'Rich iron veins have been discovered in this area.');

CREATE TABLE LocationTypeRequiredNearActionTargetLocation (
    actionId int NOT NULL,
    locationTypeId int NOT NULL,
    number int NOT NULL,
    maximumRange int NOT NULL
);

ALTER TABLE LocationTypeRequiredNearActionTargetLocation ADD CONSTRAINT fkLocationTypeRequiredNearActionTargetLocationActionId FOREIGN KEY (actionId) REFERENCES Action (id);
ALTER TABLE LocationTypeRequiredNearActionTargetLocation ADD CONSTRAINT fkLocationTypeRequiredNearActionTargetLocationLocationTypeId FOREIGN KEY (locationTypeId) REFERENCES LocationType (id);
ALTER TABLE LocationTypeRequiredNearActionTargetLocation ADD UNIQUE ikLocationTypeRequiredNearActionTargetLocationAIdLocTId (actionId, locationTypeId);

INSERT INTO LocationTypeRequiredNearActionTargetLocation (actionId, locationTypeId, number, maximumRange) VALUES ((SELECT id FROM Action WHERE canonicalName='constructWoodcutter'), (SELECT id FROM LocationType WHERE canonicalName='forest'), 1, 0);

CREATE TABLE ResourceRequiredNearActionTargetLocation (
    actionId int NOT NULL,
    resourceTypeId int NOT NULL,
    number int NOT NULL,
    maximumRange int NOT NULL
);

ALTER TABLE ResourceRequiredNearActionTargetLocation ADD CONSTRAINT fkResourceRequiredNearActionTargetLocationActionId FOREIGN KEY (actionId) REFERENCES Action (id);
ALTER TABLE ResourceRequiredNearActionTargetLocation ADD CONSTRAINT fkResourceRequiredNearActionTargetLocationResourceTypeId FOREIGN KEY (resourceTypeId) REFERENCES ResourceType (id);
ALTER TABLE ResourceRequiredNearActionTargetLocation ADD UNIQUE ikResourceRequiredNearActionTargetLocationAIdResTId (actionId, resourceTypeId);

CREATE TABLE ItemHoardingOrder(
    buildingId int NOT NULL,
    itemTypeId int NOT NULL,
    amount int NOT NULL,
    priority int NOT NULL
);

ALTER TABLE ItemHoardingOrder ADD CONSTRAINT fkItemHoardingOrderBuildingId FOREIGN KEY (buildingId) REFERENCES Building (id);
ALTER TABLE ItemHoardingOrder ADD CONSTRAINT fkItemHoardingOrderItemTypeId FOREIGN KEY (itemTypeId) REFERENCES ItemType (id);
ALTER TABLE ItemHoardingOrder ADD UNIQUE ikItemHoardingOrderBuildingItemType (buildingId, itemTypeId);

ALTER TABLE Building ADD COLUMN automatedActionId int DEFAULT 0;
