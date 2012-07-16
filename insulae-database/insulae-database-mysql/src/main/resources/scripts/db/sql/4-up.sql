CREATE TABLE Area(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    description text NOT NULL,
    coordinatesX int NOT NULL,
    coordinatesY int NOT NULL,
    realmId int NOT NULL
);

ALTER TABLE Area ADD CONSTRAINT fkAreaRealmId
FOREIGN KEY (realmId) REFERENCES Realm (id);
