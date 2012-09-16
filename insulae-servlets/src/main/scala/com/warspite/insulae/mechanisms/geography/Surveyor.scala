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

  def countLocationTypesWithinRange(origin: Location, range: Int, map: scala.collection.mutable.Map[Int, Int] = scala.collection.mutable.Map[Int, Int]()): scala.collection.mutable.Map[Int, Int] = {
    if (!map.contains(origin.locationTypeId))
      map += origin.locationTypeId -> 1;
    else
      map += origin.locationTypeId -> (map(origin.locationTypeId) + 1);

    if (range > 0) {
      for (n <- db.geography.getLocationNeighborByLocationId(origin.id)) {
        countLocationTypesWithinRange(db.geography.getLocationById(n.neighborLocationId), range - 1, map);
      }
    }

    map;
  }

  def countResourcesWithinRange(origin: Location, range: Int, map: scala.collection.mutable.Map[Int, Int] = scala.collection.mutable.Map[Int, Int]()): scala.collection.mutable.Map[Int, Int] = {
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
}
