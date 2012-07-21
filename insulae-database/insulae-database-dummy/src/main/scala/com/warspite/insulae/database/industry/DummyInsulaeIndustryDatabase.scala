package com.warspite.insulae.database.industry

import org.scala_tools.time.Imports._

class DummyInsulaeIndustryDatabase extends IndustryDatabase {
  def getBuildingTypeById(id: Int): BuildingType = {
    new BuildingType(id, "Some name", "Some description", "someCanonicalName", 1, 1, 10, 1.0, 0, 24);
  }

  def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType] = {
    Array[BuildingType](new BuildingType(1, "Some name", "Some description", "someCanonicalName", raceId, 1, 10, 1.0, 0, 24), new BuildingType(2, "Some other name", "Some other description", "someOtherCanonicalName", raceId, 2, 0, 1.5, 20, 48));
  }

  def getBuildingTypeAll(): Array[BuildingType] = {
    Array[BuildingType](new BuildingType(1, "Some name", "Some description", "someCanonicalName", 1, 1, 10, 1.0, 0, 24), new BuildingType(2, "Some other name", "Some other description", "someOtherCanonicalName", 2, 2, 0, 1.5, 20, 48));
  }

  def getBuildingByAreaId(areaId: Int): Array[Building] = {
    Array[Building](new Building(1, 1, 1, 1, 10, 5), new Building(2, 2, 2, 1, 0, 0));
  }
}
