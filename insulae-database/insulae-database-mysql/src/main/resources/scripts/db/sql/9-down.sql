ALTER TABLE Building DROP COLUMN automatedActionId;

DROP TABLE IF EXISTS ItemHoardingOrder;

DROP TABLE IF EXISTS ResourceRequiredNearActionTargetLocation;
DROP TABLE IF EXISTS LocationTypeRequiredNearActionTargetLocation;

DROP TABLE IF EXISTS Resource;
DROP TABLE IF EXISTS ResourceType;

DROP TABLE IF EXISTS TroubleReport;
DROP TABLE IF EXISTS TroubleReportType;
