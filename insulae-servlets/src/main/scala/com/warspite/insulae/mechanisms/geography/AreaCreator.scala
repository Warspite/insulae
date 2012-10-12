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
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.geography.Area
import scala.util.Random
import com.warspite.insulae.database.geography.AreaTemplate
import com.warspite.insulae.database.world.Realm
import com.warspite.insulae.database.geography.StartingLocation
import com.warspite.insulae.database.geography.LocationNeighbor
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.insulae.database.geography.AreaName
import com.warspite.insulae.database.geography.ResourceOccurrence
import com.warspite.insulae.database.geography.Resource

object AreaCreator {
  val rand = new Random(System.currentTimeMillis());
}

class AreaCreator(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def createStartingArea(race: Race, realm: Realm): Area = {
    logger.debug("Creating starting area for " + race);
    val template = selectStartingAreaTemplate(race);
    logger.debug("Selected starting area template " + template.id + ", of type " + template.areaTypeId);

    val areaCoordinates = selectAreaCoordinates(template);
    val startingArea = db.geography.putArea(new Area(0, selectAreaName(template), areaCoordinates._1, areaCoordinates._2, realm.id, template.areaTypeId));
    val locations = createLocations(startingArea, template);
    createNeighborRelations(locations);
    spawnResourcesInArea(startingArea, locations);
    
    return startingArea;
  }

  def createLocations(area: Area, areaTemplate: AreaTemplate): Array[Location] = {
    logger.debug("Populating " + area + " with locations.");
    for (t <- db.geography.getLocationTemplateByAreaTemplateId(areaTemplate.id)) {
      val l = db.geography.putLocation(new Location(0, t.locationTypeId, area.id, t.coordinatesX, t.coordinatesY, t.road, t.incomingPortalPossible));

      if (t.startingLocationOfRaceId != 0)
        db.geography.putStartingLocation(new StartingLocation(t.startingLocationOfRaceId, l.id));

      if (t.portalToAreaTypeId != 0)
        createOutgoingPortal(area, l, t.portalToAreaTypeId);
    }

    logger.debug("Done populating " + area + " with locations.");
    return db.geography.getLocationByAreaId(area.id);
  }

  def createOutgoingPortal(area: Area, l: Location, targetAreaTypeId: Int) {
    val potentialDestinations = db.geography.getLocationByPotentialPortalEndpoint(targetAreaTypeId, area.realmId, area.id);
    if (potentialDestinations.isEmpty)
      return ;

    val destination = potentialDestinations(AreaCreator.rand.nextInt(potentialDestinations.length));
    db.geography.setIncomingPortalPossible(destination.id, false);
    db.geography.putLocationNeighbor(Array(new LocationNeighbor(l.id, destination.id), new LocationNeighbor(destination.id, l.id)));
  }

  def selectStartingAreaTemplate(race: Race): AreaTemplate = {
    val potentialTemplates = db.geography.getAreaTemplateByStartingAreaOfRaceId(race.id);

    if (potentialTemplates.isEmpty)
      throw new NoStartingAreaTemplatesFoundException(race);

    return potentialTemplates(AreaCreator.rand.nextInt(potentialTemplates.length));
  }

  def selectAreaName(template: AreaTemplate): String = {
    val potentialNames = db.geography.getAreaNameByAreaTypeId(template.areaTypeId);
    if (potentialNames.isEmpty) {
      logger.info("Coulnd't find any area name for " + template);
      return "Unnamed area";
    }

    return potentialNames(AreaCreator.rand.nextInt(potentialNames.length)).name;
  }

  def selectAreaCoordinates(template: AreaTemplate): (Int, Int) = {
    return (AreaCreator.rand.nextInt(100), AreaCreator.rand.nextInt(100));
  }

  def buildLocationsMap(locations: Array[Location]): Map[(Int, Int), Location] = {
    var m = Map[(Int, Int), Location]();

    for (l <- locations)
      m += (l.coordinatesX, l.coordinatesY) -> l;

    return m;
  }

  def createNeighborRelations(locations: Array[Location]) {
    val map = buildLocationsMap(locations);
    var q = new Queue[LocationNeighbor]();

    for (l <- locations) {
      for (x <- -1 until 2) {
        for (y <- -1 until 2) {
          if (x != 0 || y != 0) {
            map.get((l.coordinatesX + x, l.coordinatesY + y)) match {
              case Some(n) => q += new LocationNeighbor(l.id, n.id);
              case None => None;
            }
          }
        }
      }
    }

    db.geography.putLocationNeighbor(q.toArray);
  }

  def spawnResourcesInArea(area: Area, locations: Array[Location]) {
    val resourceOccurrences = db.geography.getResourceOccurrenceByAreaTypeId(area.areaTypeId).foldLeft(Map[Int, List[ResourceOccurrence]]()) { (m, ro) =>
      val key = ro.locationTypeId;
      m + (key -> (ro :: m.getOrElse(key, Nil)))
    }

    for (l <- locations)
      spawnResourcesInLocation(l, resourceOccurrences.getOrElse(l.locationTypeId, Nil));
  }

  def spawnResourcesInLocation(location: Location, resourceOccurrences: List[ResourceOccurrence]) {
    for (ro <- resourceOccurrences)
      if (AreaCreator.rand.nextFloat() < ro.occurrence) {
        val r = new Resource(ro.resourceTypeId, location.id)
        db.geography.putResource(r);
        logger.debug("Placed " + r + " at " + location + " (" + ro.occurrence + " occurrence).");
      }
  }
}
