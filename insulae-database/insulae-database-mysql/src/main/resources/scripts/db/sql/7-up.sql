ALTER TABLE Avatar DROP INDEX name;
ALTER TABLE BuildingType DROP constructionActionPointCost;

CREATE TABLE Action (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(40) NOT NULL,
    description text NOT NULL,
    canonicalName varchar(40) NOT NULL,
    actionPointCost int NOT NULL,
    constructedBuildingTypeId int
);

INSERT INTO Action (name, description, canonicalName, actionPointCost, constructedBuildingTypeId) VALUES ('Build village square', 'Construct a new Village Square at a suitable location!', 'buildVillageSquare', 48, (SELECT id FROM BuildingType WHERE canonicalName='villageSquare'));
INSERT INTO Action (name, description, canonicalName, actionPointCost, constructedBuildingTypeId) VALUES ('Upgrade to town square', 'Upgrade your village square to a much fancier town ditto.', 'upgradeVillageSquareToTownSquare', 32, NULL);
INSERT INTO Action (name, description, canonicalName, actionPointCost, constructedBuildingTypeId) VALUES ('Chop wood', 'Harvest trees, producing lumber and firewood.', 'cutWood', 10, NULL);

CREATE TABLE ActionByBuildingType (
	buildingTypeId int NOT NULL,
	actionId int NOT NULL
);

ALTER TABLE ActionByBuildingType ADD CONSTRAINT fkActionByBuildingTypeBuildingTypeId FOREIGN KEY (buildingTypeId) REFERENCES BuildingType (id);
ALTER TABLE ActionByBuildingType ADD CONSTRAINT fkActionByBuildingTypeActionId FOREIGN KEY (actionId) REFERENCES Action (id);

INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='townSquare'), (SELECT id FROM Action WHERE canonicalName='buildVillageSquare'));
INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='villageSquare'), (SELECT id FROM Action WHERE canonicalName='upgradeVillageSquareToTownSquare'));
INSERT INTO ActionByBuildingType (buildingTypeId, actionId) VALUES ((SELECT id FROM BuildingType WHERE canonicalName='woodcutter'), (SELECT id FROM Action WHERE canonicalName='cutWood'));

CREATE TABLE ActionItemCost (
	actionId int NOT NULL,
	itemTypeId int NOT NULL,
    amount int NOT NULL
);

ALTER TABLE ActionItemCost ADD CONSTRAINT fkActionItemCostActionId FOREIGN KEY (actionId) REFERENCES Action (id);
ALTER TABLE ActionItemCost ADD CONSTRAINT fkActionItemCostItemTypeId FOREIGN KEY (itemTypeId) REFERENCES ItemType (id);

INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='buildVillageSquare'), (SELECT id FROM ItemType WHERE canonicalName='woodenPlanks'), 50);
INSERT INTO ActionItemCost (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='upgradeVillageSquareToTownSquare'), (SELECT id FROM ItemType WHERE canonicalName='woodenPlanks'), 35);

ALTER TABLE Building ADD COLUMN hubDistanceCost INT NOT NULL DEFAULT 0;
