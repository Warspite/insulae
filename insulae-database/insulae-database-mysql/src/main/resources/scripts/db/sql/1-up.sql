CREATE TABLE SchemaVersion(
	SchemaVersion int NOT NULL
);
DELETE FROM SchemaVersion;
INSERT INTO SchemaVersion (SchemaVersion) VALUES (0);

CREATE TABLE Account(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(20) NOT NULL,
    passwordHash varchar(20) NOT NULL,
    callSign varchar(20) NOT NULL,
    givenName varchar(20) NOT NULL,
    surname varchar(20) NOT NULL
);
