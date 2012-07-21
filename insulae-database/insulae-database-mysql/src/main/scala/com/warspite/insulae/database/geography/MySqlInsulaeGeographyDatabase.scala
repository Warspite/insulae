package com.warspite.insulae.database.geography
import java.sql.Connection
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.sql.MySqlQueryer
import com.warspite.common.database.DataRecord
import com.warspite.common.database.sql.StringEscaper
import scala.collection.mutable.Queue

class MySqlInsulaeGeographyDatabase(connection: Connection) extends MySqlQueryer(connection) with GeographyDatabase {
	def getAreaById(id: Int): Area = {
	  val r = query(Area.fields, "FROM Area WHERE id = " + id);
	  return Area(r.next(true).getOrElse(throw new AreaIdDoesNotExistException(id)));
	}
	
	def getAreaByRealmId(realmId: Int): Array[Area] = {
	  val r = query(Area.fields, "FROM Area WHERE realmId = " + realmId);
	  return r.buildArray[Area](Area.apply);
	}

	def getLocationTypeAll(): Array[LocationType] = {
	  val r = query(LocationType.fields, "FROM LocationType");
	  return r.buildArray[LocationType](LocationType.apply);
	}

	def getLocationByAreaId(areaId: Int): Array[Location] = {
	  val r = query(Location.fields, "FROM Location WHERE areaId = " + areaId);
	  return r.buildArray[Location](Location.apply);
	}

	def getTransportationTypeAll(): Array[TransportationType] = {
	  val r = query(TransportationType.fields, "FROM TransportationType");
	  return r.buildArray[TransportationType](TransportationType.apply);
	}

	def getTransportationCostAll(): Array[TransportationCost] = {
	  val r = query(TransportationCost.fields, "FROM TransportationCost");
	  return r.buildArray[TransportationCost](TransportationCost.apply);
	}

	def getLocationNeighborByAreaId(areaId: Int): Array[LocationNeighbor] = {
	  val r = query(LocationNeighbor.fields, "FROM LocationNeighbor, Location WHERE Location.areaId = " + areaId + " AND LocationNeighbor.locationId = Location.id", "LocationNeighbor");
	  return r.buildArray[LocationNeighbor](LocationNeighbor.apply);
	}
	
}
