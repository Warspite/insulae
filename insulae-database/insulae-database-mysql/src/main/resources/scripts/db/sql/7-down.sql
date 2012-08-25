ALTER TABLE Avatar ADD UNIQUE (name);

ALTER TABLE BuildingType ADD COLUMN constructionActionPointCost INT NOT NULL DEFAULT 0;

UPDATE BuildingType SET constructionActionPointCost = 4 WHERE canonicalName = 'vegetableFarm';
UPDATE BuildingType SET constructionActionPointCost = 4 WHERE canonicalName = 'grainFarm';
UPDATE BuildingType SET constructionActionPointCost = 3 WHERE canonicalName = 'woodCutter';
UPDATE BuildingType SET constructionActionPointCost = 10 WHERE canonicalName = 'sawmill';
UPDATE BuildingType SET constructionActionPointCost = 8 WHERE canonicalName = 'windmill';
UPDATE BuildingType SET constructionActionPointCost = 6 WHERE canonicalName = 'commonersHouse';
UPDATE BuildingType SET constructionActionPointCost = 10 WHERE canonicalName = 'bakery';
UPDATE BuildingType SET constructionActionPointCost = 24 WHERE canonicalName = 'villageSquare';
UPDATE BuildingType SET constructionActionPointCost = 48 WHERE canonicalName = 'townSquare';

ALTER TABLE Building DROP hubDistanceCost;

DROP TABLE IF EXISTS ActionByBuildingType;
DROP TABLE IF EXISTS ActionItemCost;
DROP TABLE IF EXISTS Action;
