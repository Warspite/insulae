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

DELIMITER |
CREATE FUNCTION changeNumberOfItemsInStorage (buildingId INT, itemTypeId INT, amount INT)
RETURNS TINYINT(1)
BEGIN
    DECLARE itemExistsInStorage TINYINT(1);
    DECLARE existingAmount INT;
    DECLARE resultingAmount INT;
    DECLARE success TINYINT(1);
    
    SELECT ItemStorage.amount FROM ItemStorage WHERE ItemStorage.buildingId = buildingId AND ItemStorage.itemTypeId = itemTypeId INTO existingAmount;

    IF ISNULL(existingAmount) THEN
        SET existingAmount = 0;
        SET itemExistsInStorage = 0;
    ELSE
        SET itemExistsInStorage = 1;
    END IF;
    
    SET resultingAmount = existingAmount + amount;
    
    SET success = 1;
    IF resultingAmount < 0 THEN SET success = 0;
    ELSEIF resultingAmount = 0 THEN DELETE FROM ItemStorage WHERE ItemStorage.buildingId = buildingId AND ItemStorage.itemTypeId = itemTypeId;
    ELSEIF itemExistsInStorage = 0 THEN INSERT INTO ItemStorage (buildingId, itemTypeId, amount) VALUES (buildingId, itemTypeId, resultingAmount);
    ELSE UPDATE ItemStorage SET ItemStorage.amount = resultingAmount WHERE ItemStorage.buildingId = buildingId AND ItemStorage.itemTypeId = itemTypeId;
    END IF;
    
    RETURN success;
END|
DELIMITER ;