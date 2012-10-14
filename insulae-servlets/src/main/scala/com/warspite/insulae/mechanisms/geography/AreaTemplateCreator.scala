package com.warspite.insulae.mechanisms.geography
import com.warspite.insulae.database.InsulaeDatabase
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.geography.AreaTemplate
import java.io.File
import com.warspite.common.database.DataRecord
import com.warspite.common.servlets.json.Parser
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.geography.AreaType
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.DescriptiveType
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import com.warspite.insulae.database.geography.LocationType
import com.warspite.insulae.database.geography.LocationTemplate
import java.awt.Color

class AreaTemplateCreator(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def createTemplate(realmCanonicalName: String, jsonFile: File, pngFile: File): AreaTemplate = {
    logger.info("Starting creation of area template in " + realmCanonicalName + " from " + jsonFile);
    val png = ImageIO.read(pngFile);
    val json = scala.io.Source.fromFile(jsonFile).mkString;
    logger.debug("Reading area creation template: " + json);

    val data = DataRecord(Parser.parse(json).asInstanceOf[Map[String, AnyRef]]);

    val realm = db.world.getRealmByCanonicalName(realmCanonicalName);
    val areaType = db.geography.getAreaTypeByCanonicalName(data.getString("areaType"));
    val roads = getRoads(data);
    val startingLocations = getStartingLocations(data);
    val incomingPortals = getIncomingPortals(data);
    val outgoingPortals = getOutgoingPortals(data);
    val areaTemplate = db.geography.putAreaTemplate(new AreaTemplate(0, areaType.id));

    try { 
      db.geography.putLocationTemplate(createLocationTemplates(png, areaTemplate, roads, startingLocations, incomingPortals, outgoingPortals));
    }
    catch {
      case x => {
        db.geography.deleteAreaTemplateById(areaTemplate.id);
        throw x;
      }
    }

    logger.info("Done creating " + areaTemplate);
    return areaTemplate;
  }

  def getRoads(data: DataRecord): Array[(Int, Int)] = {
    var roads = List[(Int, Int)]();
    for (r <- data.get[Any]("roads").asInstanceOf[List[Map[String, Double]]]) {
      val x = r.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found road object without 'x' coordinates."));
      val y = r.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found road object without 'y' coordinates."));
      roads = (x.toInt, y.toInt) :: roads;
    }

    roads.toArray;
  }

  def getStartingLocations(data: DataRecord): Map[(Int, Int), Int] = {
    val races = DescriptiveType.canonicalNameMapify(db.world.getRaceAll());

    var startingLocations = Map[(Int, Int), Int]();
    for (s <- data.get[Any]("startingLocations").asInstanceOf[List[Map[String, Any]]]) {
      val raceName = s.getOrElse("race", throw new AreaTemplateCreationException("Malformed area template: found startingLocation object without 'race' attribute.")).asInstanceOf[String];
      val race = races.getOrElse(raceName, throw new AreaTemplateCreationException("Malformed area template: found 'race' attribute with unrecognized value '" + raceName + "'."));
      val x = s.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found startingLocation object without 'x' coordinates.")).asInstanceOf[Double];
      val y = s.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found startingLocation object without 'y' coordinates.")).asInstanceOf[Double];
      startingLocations += (x.toInt, y.toInt) -> race.id;
    }

    return startingLocations
  }

  def getIncomingPortals(data: DataRecord): Array[(Int, Int)] = {
    var portals = List[(Int, Int)]();
    for (p <- data.get[Any]("incomingPortals").asInstanceOf[List[Map[String, Double]]]) {
      val x = p.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found incomingPortal object without 'x' coordinates."));
      val y = p.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found incomingPortal object without 'y' coordinates."));
      portals = (x.toInt, y.toInt) :: portals;
    }

    portals.toArray;
  }

  def getOutgoingPortals(data: DataRecord): Map[(Int, Int), Int] = {
    val areaTypes = DescriptiveType.canonicalNameMapify(db.geography.getAreaTypeAll());

    var portals = Map[(Int, Int), Int]();
    for (s <- data.get[Any]("outgoingPortals").asInstanceOf[List[Map[String, Any]]]) {
      val areaTypeName = s.getOrElse("areaType", throw new AreaTemplateCreationException("Malformed area template: found outgoingPortal object without 'areaType' attribute.")).asInstanceOf[String];
      val areaType = areaTypes.getOrElse(areaTypeName, throw new AreaTemplateCreationException("Malformed area template: found 'areaType' attribute with unrecognized value '" + areaTypeName + "'."));
      val x = s.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found outgoingPortal object without 'x' coordinates.")).asInstanceOf[Double];
      val y = s.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found outgoingPortal object without 'y' coordinates.")).asInstanceOf[Double];
      portals += (x.toInt, y.toInt) -> areaType.id;
    }

    return portals;
  }

  def createLocationTemplates(img: BufferedImage, areaTemplate: AreaTemplate, roads: Array[(Int, Int)], startingLocations: Map[(Int, Int), Int], incomingPortals: Array[(Int, Int)], outgoingPortals: Map[(Int, Int), Int]): Array[LocationTemplate] = {
    val locationTypesByColor = db.geography.getLocationTypeAll().foldLeft(Map[String, LocationType]()) { (m, lt) => m + (lt.color -> lt); }

    var locationTemplates = List[LocationTemplate]();

    for (y <- 0 until img.getHeight()) {
      for (x <- 0 until img.getWidth()) {
        val htmlColor = getHtmlColor(img.getRGB(x, y));
        val locationType = locationTypesByColor.getOrElse(htmlColor, throw new AreaTemplateCreationException("Malformed area image: found unrecognized color '" + htmlColor + "'."));
        val road = roads.contains((x, y));
        val incomingPortalPossible = incomingPortals.contains((x, y));
        val portalToAreaTypeId = outgoingPortals.getOrElse((x, y), 0);
        val startingLocationOfRaceId = startingLocations.getOrElse((x, y), 0);
        val t = new LocationTemplate(areaTemplate.id, locationType.id, x, y, road, startingLocationOfRaceId, portalToAreaTypeId, incomingPortalPossible);
        locationTemplates = t :: locationTemplates;
        logger.debug("Created " + t);
      }
    }

    return locationTemplates.toArray;
  }

  def getHtmlColor(in: Int): String = {
    val hexString = new Color(in).getRGB().toHexString;
    return hexString.substring(2, hexString.length());
  }
}
