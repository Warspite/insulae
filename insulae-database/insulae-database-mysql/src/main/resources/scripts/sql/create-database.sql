DROP DATABASE IF EXISTS insulae;
CREATE DATABASE insulae;
USE insulae;

CREATE TABLE Version(
	Version varchar(20) NOT NULL
);
INSERT INTO Version (Version) VALUES ('@insulae_version@');

CREATE TABLE Account(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(20) NOT NULL,
    passwordHash varchar(20) NOT NULL,
    callSign varchar(20) NOT NULL,
    givenName varchar(20) NOT NULL,
    surname varchar(20) NOT NULL
);
