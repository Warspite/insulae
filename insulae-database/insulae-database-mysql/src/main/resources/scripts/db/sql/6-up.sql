CREATE TABLE ItemType (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL,
    canonicalName varchar(20) NOT NULL
);

INSERT INTO ItemType (name, canonicalName) VALUES ('Grain', 'grain');
INSERT INTO ItemType (name, canonicalName) VALUES ('Vegetables', 'vegetables');
INSERT INTO ItemType (name, canonicalName) VALUES ('Lumber', 'lumber');
INSERT INTO ItemType (name, canonicalName) VALUES ('Firewood', 'firewood');
INSERT INTO ItemType (name, canonicalName) VALUES ('Wooden planks', 'woodenPlanks');
INSERT INTO ItemType (name, canonicalName) VALUES ('Flour', 'flour');
INSERT INTO ItemType (name, canonicalName) VALUES ('Bread', 'bread');

CREATE TABLE ItemStorage(
    buildingId int NOT NULL,
    itemTypeId int NOT NULL,
    amount int NOT NULL
);

ALTER TABLE ItemStorage ADD CONSTRAINT fkItemStorageBuildingId FOREIGN KEY (buildingId) REFERENCES Building (id);
ALTER TABLE ItemStorage ADD CONSTRAINT fkItemStorageItemTypeId FOREIGN KEY (itemTypeId) REFERENCES ItemType (id);
ALTER TABLE ItemStorage ADD UNIQUE ikItemStorageBuildingItemType (buildingId, itemTypeId);
