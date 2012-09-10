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