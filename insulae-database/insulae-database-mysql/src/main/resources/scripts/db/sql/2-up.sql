CREATE TABLE Realm(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    startDate datetime NOT NULL,
    endDate datetime NOT NULL
);





CREATE TABLE Race(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    description text NOT NULL,
    canonicalName varchar(20) NOT NULL
);

INSERT INTO Race (name, description, canonicalName) VALUES ('Balwerian', 'The Balwerians are vegetarian imperialists!', 'balwerian');
INSERT INTO Race (name, description, canonicalName) VALUES ('Eilendel', 'The Eilendel are a friendly bunch of people.', 'eilendel');



CREATE TABLE Sex(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    raceId int NOT NULL,
    name varchar(20) NOT NULL,
    title varchar(20) NOT NULL,
    description text NOT NULL
);

ALTER TABLE Sex ADD CONSTRAINT fkSexRaceId
FOREIGN KEY (raceId) REFERENCES Race (id);

INSERT INTO Sex (raceId, name, title, description) VALUES ((SELECT id FROM Race WHERE name='Balwerian'), 'Male', 'Master', 'The male Balwerian is an ugly sight to behold.');
INSERT INTO Sex (raceId, name, title, description) VALUES ((SELECT id FROM Race WHERE name='Balwerian'), 'Female', 'Mistress', 'The female Balwerian often cuts her hair short.');
INSERT INTO Sex (raceId, name, title, description) VALUES ((SELECT id FROM Race WHERE name='Eilendel'), 'Male', 'Lord', 'The male Eilendel look kind of feminine.');
INSERT INTO Sex (raceId, name, title, description) VALUES ((SELECT id FROM Race WHERE name='Eilendel'), 'Female', 'Lady', 'The female Eilendel are very tall!.');




CREATE TABLE RaceByRealm(
    realmId int NOT NULL,
    raceId int NOT NULL
);

ALTER TABLE RaceByRealm ADD CONSTRAINT fkRaceByRealmRaceId
FOREIGN KEY (raceId) REFERENCES Race (id);

ALTER TABLE RaceByRealm ADD CONSTRAINT fkRaceByRealmRealmId
FOREIGN KEY (realmId) REFERENCES Realm (id);
