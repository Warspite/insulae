package com.warspite.insulae.mechanisms.geography

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.account.AccountDatabase
import com.warspite.common.database.DataRecord
import org.mockito.Matchers._
import com.warspite.insulae.database.account.Account
import org.mockito.ArgumentCaptor
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.LocationType
import com.warspite.insulae.database.geography.TransportationType
import com.warspite.insulae.database.geography.TransportationCost
import com.warspite.insulae.database.geography.LocationNeighbor
import com.warspite.insulae.database.geography.Resource
import com.warspite.insulae.database.world.WorldDatabase
import com.warspite.common.servlets.json.Parser
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.geography.AreaType

@RunWith(classOf[JUnitRunner])
class AreaTemplateCreatorSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var db: InsulaeDatabase = null;
  var geoDb: GeographyDatabase = null;
  var worldDb: WorldDatabase = null;

  var atc: AreaTemplateCreator = null;

  val validJson = "{\"areaType\": \"sampleDesert\",\"startingLocations\": [{\"race\": \"balwerian\",\"x\": 1,\"y\": 1},{\"race\": \"balwerian\",\"x\": 5,\"y\": 8}  ],\"roads\": [{\"x\": 2,\"y\": 1},{\"x\": 3,\"y\": 2},{\"x\": 4, \"y\": 3},{\"x\": 4,\"y\": 4},{\"x\": 4,\"y\": 5 },{\"x\": 4,\"y\": 6},{\"x\": 4,\"y\": 7}],\"incomingPortals\": [{\"x\": 0,\"y\": 5},{\"x\": 5,\"y\": 0}],\"outgoingPortals\": [{\"areaType\": \"sampleDesert\",\"x\": 2,\"y\": 4},{\"areaType\": \"sampleVolcanic\",\"x\": 7,\"y\": 7}]}";
  val validData = DataRecord(Parser.parse(validJson).asInstanceOf[Map[String, AnyRef]]);

  override def beforeEach() {
    db = mock[InsulaeDatabase];
    geoDb = mock[GeographyDatabase];
    worldDb = mock[WorldDatabase];

    when(db.geography).thenReturn(geoDb);
    when(db.world).thenReturn(worldDb);

    atc = new AreaTemplateCreator(db);
  }

  "AreaTemplateCreator" should "extract roads from data record" in {
    atc.getRoads(validData) should equal(Array((4, 7), (4, 6), (4, 5), (4, 4), (4, 3), (3, 2), (2, 1)));
  }

  it should "extract startingLocations from data record" in {
    when(worldDb.getRaceAll()).thenReturn(Array(new Race(1, "name", "description", "balwerian", 0)));

    val startingLocations = atc.getStartingLocations(validData);
    startingLocations.length should equal(2);
    startingLocations(0)._3.canonicalName should equal("balwerian");
    startingLocations(0)._1 should equal(5);
    startingLocations(0)._2 should equal(8);
    startingLocations(1)._3.canonicalName should equal("balwerian");
    startingLocations(1)._1 should equal(1);
    startingLocations(1)._2 should equal(1);
  }

  it should "throw appropriate exception if unrecognized race canonical name is parsed " in {
    when(worldDb.getRaceAll()).thenReturn(Array(new Race(1, "name", "description", "someWeirdRace", 0)));

    intercept[AreaTemplateCreationException] {
      atc.getStartingLocations(validData);
    }
  }

  it should "extract incomingPortals from data record" in {
    atc.getIncomingPortals(validData) should equal(Array((5, 0), (0, 5)));
  }

  it should "extract outgoingPortals from data record" in {
    when(geoDb.getAreaTypeAll()).thenReturn(Array(new AreaType(1, "name", "description", "sampleDesert", 0), new AreaType(2, "name", "description", "sampleVolcanic", 0)));

    val outgoingPortals = atc.getOutgoingPortals(validData);
    outgoingPortals.length should equal(2);
    outgoingPortals(0)._3.canonicalName should equal("sampleVolcanic");
    outgoingPortals(0)._1 should equal(7);
    outgoingPortals(0)._2 should equal(7);
    outgoingPortals(1)._3.canonicalName should equal("sampleDesert");
    outgoingPortals(1)._1 should equal(2);
    outgoingPortals(1)._2 should equal(4);
  }

  it should "throw appropriate exception if an outgoingPortal object is missing any attribute" in {
    when(geoDb.getAreaTypeAll()).thenReturn(Array(new AreaType(1, "name", "description", "sampleDesert", 0), new AreaType(2, "name", "description", "sampleVolcanic", 0)));
    val json = "{\"outgoingPortals\": [{\"areaType\": \"sampleDesert\",\"y\": 4}]}";
    val data = DataRecord(Parser.parse(json).asInstanceOf[Map[String, AnyRef]]);
    intercept[AreaTemplateCreationException] {
      atc.getOutgoingPortals(data);
    }
  }
}