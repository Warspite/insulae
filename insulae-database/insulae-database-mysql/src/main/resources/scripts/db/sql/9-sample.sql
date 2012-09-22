INSERT INTO Resource (locationId, resourceTypeId) VALUES (12, (SELECT id FROM ResourceType WHERE canonicalName='iron'));
INSERT INTO Resource (locationId, resourceTypeId) VALUES (14, (SELECT id FROM ResourceType WHERE canonicalName='gold'));
INSERT INTO Resource (locationId, resourceTypeId) VALUES (14, (SELECT id FROM ResourceType WHERE canonicalName='gems'));
INSERT INTO Resource (locationId, resourceTypeId) VALUES (15, (SELECT id FROM ResourceType WHERE canonicalName='gems'));

INSERT INTO ItemHoardingOrder (buildingId, itemTypeId, amount, priority) VALUES (8, (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1, 1);