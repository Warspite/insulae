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
