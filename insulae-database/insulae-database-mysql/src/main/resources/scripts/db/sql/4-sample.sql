INSERT INTO Area(name, description, coordinatesX, coordinatesY, realmId) VALUES ('some area', 'A very non-descript area', 1, 1, 1);
INSERT INTO Area(name, description, coordinatesX, coordinatesY, realmId) VALUES ('some other area', 'A very particular area', 5, 3, 1);
INSERT INTO Area(name, description, coordinatesX, coordinatesY, realmId) VALUES ('an area', 'A private area.', 1, 1, 2);


INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM Area WHERE name='some area'), 1, 1);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM Area WHERE name='some area'), 1, 2);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM Area WHERE name='some area'), 1, 3);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Plains'), (SELECT id FROM Area WHERE name='some area'), 2, 1);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM Area WHERE name='some area'), 2, 2);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Forest'), (SELECT id FROM Area WHERE name='some area'), 2, 3);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Rocky hills'), (SELECT id FROM Area WHERE name='some area'), 3, 1);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Grassy hills'), (SELECT id FROM Area WHERE name='some area'), 3, 2);
INSERT INTO Location(locationTypeId, areaId, coordinatesX, coordinatesY) VALUES ((SELECT id FROM LocationType WHERE name='Grassy hills'), (SELECT id FROM Area WHERE name='some area'), 3, 3);

INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (1, 2);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (2, 1);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (1, 4);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (4, 1);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (2, 3);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (3, 2);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (2, 5);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (5, 2);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (3, 6);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (6, 3);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (4, 7);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (7, 4);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (5, 8);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (8, 5);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (6, 9);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (9, 6);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (7, 8);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (8, 7);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (8, 9);
INSERT INTO LocationNeighbor(locationId, neighborLocationId) VALUES (9, 8);
