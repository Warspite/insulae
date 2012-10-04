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
    val startingArea = new Area(0, selectAreaName(template), areaCoordinates._1, areaCoordinates._2, realm.id, template.areaTypeId);
    
    throw new RuntimeException("Unimplemented!");
  }

  def selectStartingAreaTemplate(race: Race): AreaTemplate = {
    val potentialTemplates = db.geography.getAreaTemplateByStartingAreaOfRaceId(race.id);
    
    if(potentialTemplates.isEmpty)
      throw new NoStartingAreaTemplatesFoundException(race);
    
    return potentialTemplates(AreaCreator.rand.nextInt(potentialTemplates.length));
  }

  def selectAreaName(template: AreaTemplate): String = {
    return "New area name";
  }

  def selectAreaCoordinates(template: AreaTemplate): (Int, Int) = {
    return (0, 0);
  }
}
