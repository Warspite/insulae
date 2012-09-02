CREATE TABLE BuildingType (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    description text NOT NULL,
    canonicalName varchar(20) NOT NULL,
    raceId int NOT NULL,
    transportationTypeId int NOT NULL,
    maximumActionPoints int NOT NULL,
    actionPointRegenerationRate float NOT NULL,
    industryHubRange int NOT NULL,
    constructionActionPointCost int NOT NULL
);

ALTER TABLE BuildingType ADD CONSTRAINT fkBuildingTypeRaceId FOREIGN KEY (raceId) REFERENCES Race (id);
ALTER TABLE BuildingType ADD CONSTRAINT fkBuildingTypeTransportationTypeId FOREIGN KEY (transportationTypeId) REFERENCES TransportationType (id);

INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Vegetable farm', 'The vegetable farm produces healthy vegetables full of nutrients.', 'vegetableFarm', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Foot'), 24, 1.0, 0, 4);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Grain farm', 'The grain farm produces grains that can then be turned into e.g. flour and bread - important base foods in your society!', 'grainFarm', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Foot'), 24, 1.0, 0, 4);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Woodcutter', 'The woodcutter cuts down trees, producing lumber.', 'woodcutter', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Wheel'), 24, 1.0, 0, 3);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Sawmill', 'The sawmill cuts lumber into planks', 'sawmill', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Wheel'), 24, 1.0, 0, 10);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Windmill', 'The windmill grounds grains into flour', 'windmill', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Foot'), 24, 1.0, 0, 8);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Commoner\'s house', 'This is where commoners live, eat, sleep, and produce more little commoners!', 'commonersHouse', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Foot'), 48, 1.0, 0, 6);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Bakery', 'In the bakery, the baker combines flour, water and love to produce the healthiest bread imaginable.', 'bakery', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Foot'), 24, 1.0, 0, 10);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Village square', 'The village square acts as a very simple industrial hub.', 'villageSquare', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Wheel'), 48, 1.0, 2, 24);
INSERT INTO BuildingType (name, description, canonicalName, raceId, transportationTypeId, maximumActionPoints, actionPointRegenerationRate, industryHubRange, constructionActionPointCost) VALUES ('Town square', 'The town square is slightly larger than the village square. It has more carriers on hand and an extended range.', 'townSquare', (SELECT id FROM Race WHERE name='Balwerian'), (SELECT id FROM TransportationType WHERE name='Wheel'), 64, 1.8, 3, 48);

CREATE TABLE Building(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    locationId int NOT NULL,
    buildingTypeId int NOT NULL,
    avatarId int NOT NULL,
    actionPoints float NOT NULL,
    reservedActionPoints int NOT NULL,
    industryHubBuildingId int NOT NULL
);

ALTER TABLE Building ADD CONSTRAINT fkBuildingLocationId FOREIGN KEY (locationId) REFERENCES Location (id);
ALTER TABLE Building ADD CONSTRAINT fkBuildingBuildingTypeId FOREIGN KEY (buildingTypeId) REFERENCES BuildingType (id);
ALTER TABLE Building ADD CONSTRAINT fkBuildingAvatarId FOREIGN KEY (avatarId) REFERENCES Avatar (id);
