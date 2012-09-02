CREATE TABLE ActionItemOutput (
	actionId int NOT NULL,
	itemTypeId int NOT NULL,
    amount int NOT NULL
);

INSERT INTO ActionItemOutput (actionId, itemTypeId, amount) VALUES ((SELECT id FROM Action WHERE canonicalName='cutWood'), (SELECT id FROM ItemType WHERE canonicalName='lumber'), 1);
