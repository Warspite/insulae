package com.warspite.insulae.mechanisms.geography

import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.TransportationType
import scala.collection.mutable.Queue
import scala.collection.mutable.PriorityQueue
import scala.math._
import scala.util.control.Breaks._
import scala.collection.mutable.Stack
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.BuildingAtLocationIdDoesNotExistException
import scala.collection.mutable.{ Map => MMap }

object Surveyor {
  val TARGET_NOT_WITHIN_RANGE = -1;
}

class Surveyor(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def findRange(startingLocationId: Int, targetLocationId: Int, maxRange: Int, spentSteps: Int = 0): Int = {
    if (startingLocationId == targetLocationId)
      return spentSteps;

    if (spentSteps < maxRange)
      for (n <- db.geography.getLocationNeighborByLocationId(startingLocationId)) {
        val range = findRange(n.neighborLocationId, targetLocationId, maxRange, spentSteps + 1);
        if (range != Surveyor.TARGET_NOT_WITHIN_RANGE)
          return range;
      }

    return Surveyor.TARGET_NOT_WITHIN_RANGE;
  }

  def countLocationTypesWithinRange(origin: Location, range: Int): Map[Int, Int] = {
    var map = Map[Int, Int]();
    for (l <- findLocationsWithinRange(origin.id, range).values) {
      if (!map.contains(l.locationTypeId))
        map += l.locationTypeId -> 1;
      else
        map += l.locationTypeId -> (map(l.locationTypeId) + 1);
    }

    map;
  }

  def countResourcesWithinRange(origin: Location, range: Int, map: MMap[Int, Int] = MMap[Int, Int]()): MMap[Int, Int] = {
    for (r <- db.geography.getResourceByLocationId(origin.id)) {
      if (!map.contains(r.resourceTypeId))
        map += r.resourceTypeId -> 1;
      else
        map += r.resourceTypeId -> (map(r.resourceTypeId) + 1);
    }

    if (range > 0) {
      for (n <- db.geography.getLocationNeighborByLocationId(origin.id)) {
        countResourcesWithinRange(db.geography.getLocationById(n.neighborLocationId), range - 1, map);
      }
    }

    map;
  }

  def findLocationsWithinRange(originId: Int, range: Int, locations: MMap[Int, Location] = MMap[Int, Location]()): MMap[Int, Location] = {
    if (locations.contains(originId))
      return locations;

    locations += originId -> db.geography.getLocationById(originId);

    if (range > 0)
      for (n <- db.geography.getLocationNeighborByLocationId(originId))
        findLocationsWithinRange(n.neighborLocationId, range - 1, locations);

    return locations;
  }

  def findBuildingsWithinRange(origin: Location, range: Int): Array[Building] = {
    db.industry.getBuildingByMultipleLocationId(findLocationsWithinRange(origin.id, range).keys.toArray);
  }
}
