package com.warspite.insulae.servlets.world

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.common.servlets.sessions.SessionKeeper
import com.warspite.insulae.database.InsulaeDatabase
import javax.servlet.http.HttpServletRequest
import com.warspite.common.database.DataRecord
import com.warspite.common.servlets.ClientReadableException
import org.mockito.Matchers._
import org.mockito.ArgumentCaptor
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.world.WorldDatabase
import com.warspite.common.servlets.MissingParameterException
import com.warspite.common.servlets.sessions.Session
import com.warspite.insulae.database.world.Avatar
import com.warspite.insulae.mechanisms.geography.AreaCreator
import com.warspite.insulae.mechanisms.geography.Surveyor
import com.warspite.insulae.database.geography.StartingLocation
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.industry.IndustryDatabase
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.StartingBuilding
import com.warspite.insulae.database.geography.LocationCoordinatesDoNotExistException
import com.warspite.insulae.database.industry.BuildingAtLocationIdDoesNotExistException
import com.warspite.insulae.database.geography.LocationCoordinatesDoNotExistException
import com.warspite.insulae.mechanisms.geography.PathFinder

@RunWith(classOf[JUnitRunner])
class AvatarServletSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var worldDb: WorldDatabase = null;
  var indDb: IndustryDatabase = null;
  var geoDb: GeographyDatabase = null;
  var httpReq: HttpServletRequest = null;
  var as: AvatarServlet = null;
  var surveyor: Surveyor = null;
  var areaCreator: AreaCreator = null;
  var pf: PathFinder = null;

  val race = new Race(1, "raceName", "raceDesc", "race", 1);
  val locNW = new Location(1, 1, 1, 1, 1, false);
  val locNC = new Location(2, 1, 1, 2, 1, false);
  val locNE = new Location(3, 1, 1, 3, 1, false);
  val locCW = new Location(4, 2, 1, 1, 2, false);
  val locCC = new Location(5, 2, 1, 2, 2, false);
  val locCE = new Location(6, 2, 1, 3, 2, false);
  val locSW = new Location(7, 3, 1, 1, 3, false);
  val locSC = new Location(8, 3, 1, 2, 3, false);
  val locSE = new Location(9, 3, 1, 3, 3, false);

  override def beforeEach() {
    sk = mock[SessionKeeper];
    db = mock[InsulaeDatabase];
    surveyor = mock[Surveyor];
    areaCreator = mock[AreaCreator];
    pf = mock[PathFinder];
    worldDb = mock[WorldDatabase];
    geoDb = mock[GeographyDatabase];
    indDb = mock[IndustryDatabase];
    httpReq = mock[HttpServletRequest];
    as = new AvatarServlet(db, sk, areaCreator, surveyor, pf);

    when(db.world).thenReturn(worldDb);
    when(db.geography).thenReturn(geoDb);
    when(db.industry).thenReturn(indDb);
    when(sk.get(any[Int], any[Int])).thenReturn(new Session(1, sk));
    when(httpReq.getHeader(any[String])).thenReturn("{\"id\": 1, \"key\": 2}");

    when(geoDb.getLocationByCoordinates(locNW.areaId, locNW.coordinatesX, locNW.coordinatesY)).thenReturn(locNW);
    when(geoDb.getLocationByCoordinates(locNC.areaId, locNC.coordinatesX, locNC.coordinatesY)).thenReturn(locNC);
    when(geoDb.getLocationByCoordinates(locNE.areaId, locNE.coordinatesX, locNE.coordinatesY)).thenReturn(locNE);
    when(geoDb.getLocationByCoordinates(locCW.areaId, locCW.coordinatesX, locCW.coordinatesY)).thenReturn(locCW);
    when(geoDb.getLocationByCoordinates(locCC.areaId, locCC.coordinatesX, locCC.coordinatesY)).thenReturn(locCC);
    when(geoDb.getLocationByCoordinates(locCE.areaId, locCE.coordinatesX, locCE.coordinatesY)).thenReturn(locCE);
    when(geoDb.getLocationByCoordinates(locSW.areaId, locSW.coordinatesX, locSW.coordinatesY)).thenReturn(locSW);
    when(geoDb.getLocationByCoordinates(locSC.areaId, locSC.coordinatesX, locSC.coordinatesY)).thenReturn(locSC);
    when(geoDb.getLocationByCoordinates(locSE.areaId, locSE.coordinatesX, locSE.coordinatesY)).thenReturn(locSE);

    when(indDb.getStartingBuildingByRaceId(race.id)).thenReturn(Array(new StartingBuilding(race.id, 1, 0, 0), new StartingBuilding(race.id, 2, 1, 0)));
  }

  "AvatarServlet" should "throw appropriate exception if required parameters are missing when putting avatar" in {
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "sexId" -> 1)))
    }
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "name" -> "1")))
    }
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "name" -> "1", "sexId" -> 1)))
    }
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("name" -> "1", "raceId" -> 1, "sexId" -> 1)))
    }
  }

  it should "throw appropriate exception if name is too short" in {
    intercept[ClientReadableException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "sexId" -> 1, "name" -> "123")))
    }
  }

  it should "throw appropriate exception if account already has avatar in that realm" in {
    when(worldDb.getAvatarByAccountId(any[Int])).thenReturn(Array[Avatar](new Avatar(1, 1, 2, 1, 1, "someName"), new Avatar(1, 1, 1, 1, 1, "someOtherName")))
    intercept[ClientReadableException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "sexId" -> 1, "name" -> "1234")))
    }
  }

  it should "determine a starting location as ok if surveyor finds no buildings and required starting building locations exist and are unoccupied" in {
    when(surveyor.findBuildingsWithinRange(locCC, race.minimumStartingLocationClearRadius)).thenReturn(Array[Building]());
    when(indDb.getBuildingByLocationId(any[Int])).thenThrow(new BuildingAtLocationIdDoesNotExistException(0));
    as.isStartingLocationOk(locCC, race) should equal(true);
    verify(surveyor).findBuildingsWithinRange(locCC, race.minimumStartingLocationClearRadius);
    verify(geoDb).getLocationByCoordinates(locCC.areaId, locCC.coordinatesX, locCC.coordinatesY);
    verify(geoDb).getLocationByCoordinates(locCE.areaId, locCE.coordinatesX, locCE.coordinatesY);
    verify(indDb).getBuildingByLocationId(locCC.id);
    verify(indDb).getBuildingByLocationId(locCE.id);
  }

  it should "determine a starting location as not ok if surveyor finds buildings within range" in {
    when(surveyor.findBuildingsWithinRange(locCC, race.minimumStartingLocationClearRadius)).thenReturn(Array[Building](new Building(1, 1, 1, 1, 0.0, 0, 0, 0, 0)));
    when(indDb.getBuildingByLocationId(any[Int])).thenThrow(new BuildingAtLocationIdDoesNotExistException(0));
    as.isStartingLocationOk(locCC, race) should equal(false);
  }

  it should "determine a starting location as not ok if a building exists on required location" in {
    when(surveyor.findBuildingsWithinRange(locCC, race.minimumStartingLocationClearRadius)).thenReturn(Array[Building]());
    when(indDb.getBuildingByLocationId(locCC.id)).thenThrow(new BuildingAtLocationIdDoesNotExistException(0));
    when(indDb.getBuildingByLocationId(locCE.id)).thenReturn(new Building(1, 1, 1, 1, 0.0, 0, 0, 0, 0));
    as.isStartingLocationOk(locCC, race) should equal(false);
  }

  it should "determine a starting location as not ok if a required location does not exist" in {
    when(geoDb.getLocationByCoordinates(any[Int], any[Int], any[Int])).thenThrow(new LocationCoordinatesDoNotExistException(0, 0, 0));
    when(surveyor.findBuildingsWithinRange(locCC, race.minimumStartingLocationClearRadius)).thenReturn(Array[Building]());
    when(indDb.getBuildingByLocationId(any[Int])).thenThrow(new BuildingAtLocationIdDoesNotExistException(0));
    as.isStartingLocationOk(locCC, race) should equal(false);
  }
}