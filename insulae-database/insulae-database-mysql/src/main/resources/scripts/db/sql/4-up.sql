CREATE TABLE Area(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    description text NOT NULL,
    coordinatesX int NOT NULL,
    coordinatesY int NOT NULL,
    realmId int NOT NULL
);

ALTER TABLE Area ADD CONSTRAINT fkAreaRealmId FOREIGN KEY (realmId) REFERENCES Realm (id);

CREATE TABLE LocationType(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    description text NOT NULL
);

INSERT INTO LocationType (name, description) VALUES ('Plains', 'The grassy plains are home to several species of large mammals, including wild horses.');
INSERT INTO LocationType (name, description) VALUES ('Forest', 'Forests can be difficult traverse for mounted units.');
INSERT INTO LocationType (name, description) VALUES ('Grassy hills', 'Grassy hills serve well as grazing grounds for nomadic herds.');
INSERT INTO LocationType (name, description) VALUES ('Rocky hills', 'Traveling through rocky hills can be treacherous and requires great care.');

CREATE TABLE TransportationType(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL
);

INSERT INTO TransportationType (name) VALUES ('Afoot');
INSERT INTO TransportationType (name) VALUES ('Winged');
INSERT INTO TransportationType (name) VALUES ('Mounted');
INSERT INTO TransportationType (name) VALUES ('Wheeled');


CREATE TABLE TransportationCost(
    locationTypeId int NOT NULL,
    transportationTypeId int NOT NULL,
    costWithoutRoad int NOT NULL,
    costWithRoad int NOT NULL
);

ALTER TABLE TransportationCost ADD CONSTRAINT fkTransportationCostLocationTypeId FOREIGN KEY (locationTypeId) REFERENCES LocationType (id);
ALTER TABLE TransportationCost ADD CONSTRAINT fkTransportationCostTransportationTypeTypeId FOREIGN KEY (transportationTypeId) REFERENCES TransportationType (id);
ALTER TABLE TransportationCost ADD UNIQUE ikTransportationCostLocationTypeTransportationType (locationTypeId, transportationTypeId);

INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM TransportationType WHERE name='Afoot'), 6, 4);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM TransportationType WHERE name='Afoot'), 10, 4);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Grassy hills'), (SELECT id FROM TransportationType WHERE name='Afoot'), 8, 5);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Rocky hills'), (SELECT id FROM TransportationType WHERE name='Afoot'), 10, 5);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM TransportationType WHERE name='Winged'), 2, 2);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM TransportationType WHERE name='Winged'), 2, 2);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Grassy hills'), (SELECT id FROM TransportationType WHERE name='Winged'), 2, 2);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Rocky hills'), (SELECT id FROM TransportationType WHERE name='Winged'), 2, 2);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM TransportationType WHERE name='Mounted'), 4, 3);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM TransportationType WHERE name='Mounted'), 14, 4);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Grassy hills'), (SELECT id FROM TransportationType WHERE name='Mounted'), 6, 4);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Rocky hills'), (SELECT id FROM TransportationType WHERE name='Mounted'), 16, 4);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM TransportationType WHERE name='Wheeled'), 8, 3);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM TransportationType WHERE name='Wheeled'), 24, 4);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Grassy hills'), (SELECT id FROM TransportationType WHERE name='Wheeled'), 10, 5);
INSERT INTO TransportationCost (locationTypeId, transportationTypeId, costWithoutRoad, costWithRoad) VALUES ((SELECT id FROM LocationType WHERE name='Rocky hills'), (SELECT id FROM TransportationType WHERE name='Wheeled'), 20, 5);

CREATE TABLE Location(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    locationTypeId int NOT NULL,
    areaId int NOT NULL,
    coordinatesX int NOT NULL,
    coordinatesY int NOT NULL
);

ALTER TABLE Location ADD CONSTRAINT fkLocationLocationTypeId FOREIGN KEY (locationTypeId) REFERENCES LocationType (id);

CREATE TABLE LocationNeighbor(
    locationId int NOT NULL,
    neighborLocationId int NOT NULL
);

ALTER TABLE LocationNeighbor ADD CONSTRAINT fkLocationNeighborLocationId FOREIGN KEY (locationId) REFERENCES Location (id);
ALTER TABLE LocationNeighbor ADD CONSTRAINT fkLocationNeighborNeighborLocationId FOREIGN KEY (neighborLocationId) REFERENCES Location (id);
ALTER TABLE LocationNeighbor ADD UNIQUE ikLocationNeighborLocationIdNeighborLocationId (locationId, neighborLocationId);
