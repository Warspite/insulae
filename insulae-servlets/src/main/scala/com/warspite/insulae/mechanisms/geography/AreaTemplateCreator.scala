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

class AreaTemplateCreator(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def createTemplate(realmCanonicalName: String, jsonFile: File): AreaTemplate = {
    val json = scala.io.Source.fromFile(jsonFile).mkString;
    logger.debug(json);

    val data = DataRecord(Parser.parse(json).asInstanceOf[Map[String, AnyRef]]);
    
    val realm = db.world.getRealmByCanonicalName(realmCanonicalName);
    val areaType = db.geography.getAreaTypeByCanonicalName(data.getString("areaType"));
    val roads = getRoads(data);
    val startingLocations = getStartingLocations(data);
    val incomingPortals = getIncomingPortals(data);
    val outgoingPortals = getOutgoingPortals(data);
    
    return null;
  }
  
  def getRoads(data: DataRecord): Array[(Int, Int)] = {
    var roads = List[(Int, Int)]();
    for(r <- data.get[Any]("roads").asInstanceOf[List[Map[String, Double]]]) {
      val x = r.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found road object without 'x' coordinates."));
      val y = r.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found road object without 'y' coordinates."));
      roads = (x.toInt, y.toInt) :: roads;
    }
    
    roads.toArray;
  }

  def getStartingLocations(data: DataRecord): Array[(Int, Int, Race)] = {
    val races = DescriptiveType.canonicalNameMapify(db.world.getRaceAll());
    
    var startingLocations = List[(Int, Int, Race)]();
    for(s <- data.get[Any]("startingLocations").asInstanceOf[List[Map[String, Any]]]) {
      val raceName = s.getOrElse("race", throw new AreaTemplateCreationException("Malformed area template: found startingLocation object without 'race' attribute.")).asInstanceOf[String];
      val race = races.getOrElse(raceName, throw new AreaTemplateCreationException("Malformed area template: found 'race' attribute with unrecognized value '" + raceName + "'."));
      val x = s.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found startingLocation object without 'x' coordinates.")).asInstanceOf[Double];
      val y = s.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found startingLocation object without 'y' coordinates.")).asInstanceOf[Double];
      startingLocations = (x.toInt, y.toInt, race) :: startingLocations;
    }
    
    startingLocations.toArray;
  }

  def getIncomingPortals(data: DataRecord): Array[(Int, Int)] = {
    var portals = List[(Int, Int)]();
    for(p <- data.get[Any]("incomingPortals").asInstanceOf[List[Map[String, Double]]]) {
      val x = p.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found incomingPortal object without 'x' coordinates."));
      val y = p.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found incomingPortal object without 'y' coordinates."));
      portals = (x.toInt, y.toInt) :: portals;
    }
    
    portals.toArray;
  }

  def getOutgoingPortals(data: DataRecord): Array[(Int, Int, AreaType)] = {
    val areaTypes = DescriptiveType.canonicalNameMapify(db.geography.getAreaTypeAll());
    
    var portals = List[(Int, Int, AreaType)]();
    for(s <- data.get[Any]("outgoingPortals").asInstanceOf[List[Map[String, Any]]]) {
      val areaTypeName = s.getOrElse("areaType", throw new AreaTemplateCreationException("Malformed area template: found outgoingPortal object without 'areaType' attribute.")).asInstanceOf[String];
      val areaType = areaTypes.getOrElse(areaTypeName, throw new AreaTemplateCreationException("Malformed area template: found 'areaType' attribute with unrecognized value '" + areaTypeName + "'."));
      val x = s.getOrElse("x", throw new AreaTemplateCreationException("Malformed area template: found outgoingPortal object without 'x' coordinates.")).asInstanceOf[Double];
      val y = s.getOrElse("y", throw new AreaTemplateCreationException("Malformed area template: found outgoingPortal object without 'y' coordinates.")).asInstanceOf[Double];
      portals = (x.toInt, y.toInt, areaType) :: portals;
    }
    
    portals.toArray;
  }
}
