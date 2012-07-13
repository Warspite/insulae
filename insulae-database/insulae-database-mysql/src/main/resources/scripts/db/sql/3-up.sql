CREATE TABLE Avatar(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    accountId int NOT NULL,
    realmId int NOT NULL,
    raceId int NOT NULL,
    sexId int NOT NULL,
    name varchar(20) NOT NULL
);

ALTER TABLE Avatar ADD CONSTRAINT fkAvatarAccountId
FOREIGN KEY (accountId) REFERENCES Account (Id);

ALTER TABLE Avatar ADD CONSTRAINT fkAvatarRealmId
FOREIGN KEY (realmId) REFERENCES Realm (Id);

ALTER TABLE Avatar ADD CONSTRAINT fkAvatarRaceId
FOREIGN KEY (raceId) REFERENCES Race (Id);

ALTER TABLE Avatar ADD CONSTRAINT fkAvatarSexId
FOREIGN KEY (sexId) REFERENCES Sex (Id);
