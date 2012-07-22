INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (1, (SELECT id FROM ItemType WHERE canonicalName='grain'), 5);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (1, (SELECT id FROM ItemType WHERE canonicalName='vegetables'), 12);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (1, (SELECT id FROM ItemType WHERE canonicalName='lumber'), 8);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (1, (SELECT id FROM ItemType WHERE canonicalName='firewood'), 3);

INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (5, (SELECT id FROM ItemType WHERE canonicalName='bread'), 4);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (5, (SELECT id FROM ItemType WHERE canonicalName='vegetables'), 2);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (5, (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1);

INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (8, (SELECT id FROM ItemType WHERE canonicalName='grain'), 25);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (8, (SELECT id FROM ItemType WHERE canonicalName='flour'), 25);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (8, (SELECT id FROM ItemType WHERE canonicalName='woodenPlanks'), 4);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (8, (SELECT id FROM ItemType WHERE canonicalName='lumber'), 6);
INSERT INTO ItemStorage(buildingId, itemTypeId, amount) VALUES (8, (SELECT id FROM ItemType WHERE canonicalName='firewood'), 13);
