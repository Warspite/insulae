package com.warspite.insulae.database.industry
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import scala.collection.mutable.Queue

class MySqlInsulaeIndustryDatabase(connection: Connection) extends MySqlQueryer(connection) with IndustryDatabase {
  def getBuildingTypeById(id: Int): BuildingType = {
    val r = query(BuildingType.fields, "FROM BuildingType WHERE id = " + id);
    return BuildingType(r.next(true).getOrElse(throw new BuildingTypeIdDoesNotExistException(id)));
  }

  def getBuildingTypeByRaceId(raceId: Int): Array[BuildingType] = {
    val r = query(BuildingType.fields, "FROM BuildingType WHERE raceId = " + raceId);
    return r.buildArray[BuildingType](BuildingType.apply);
  }

  def getBuildingTypeAll(): Array[BuildingType] = {
    val r = query(BuildingType.fields, "FROM BuildingType");
    return r.buildArray[BuildingType](BuildingType.apply);
  }

  def getBuildingByAreaId(areaId: Int): Array[Building] = {
    val r = query(Building.fields, "FROM Building, Location WHERE Building.locationId = Location.id AND Location.areaId = " + areaId, "Building");
    return r.buildArray[Building](Building.apply);
  }

}
