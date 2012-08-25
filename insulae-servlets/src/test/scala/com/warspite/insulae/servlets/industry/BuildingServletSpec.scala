package com.warspite.insulae.servlets.industry

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
import com.warspite.insulae.database.world.Avatar
import com.warspite.common.servlets.MissingParameterException
import com.warspite.common.servlets.sessions.Session
import com.warspite.insulae.database.world.Avatar
import com.warspite.insulae.database.industry.IndustryDatabase
import com.warspite.common.servlets.AuthorizationFailureException
import com.warspite.insulae.database.geography.LocationIdDoesNotExistException
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.industry.BuildingType
import com.warspite.common.servlets.InvalidServletInputException
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.mechanisms.geography._

@RunWith(classOf[JUnitRunner])
class BuildingServletSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var pf: PathFinder = null;
  var worldDb: WorldDatabase = null;
  var geographyDb: GeographyDatabase = null;
  var industryDb: IndustryDatabase = null;
  var bs: BuildingServlet = null;
  var session: Session = null;
  var avatar: Avatar = null;

  override def beforeEach() {
    sk = mock[SessionKeeper];
    db = mock[InsulaeDatabase];
    pf = mock[PathFinder];
    worldDb = mock[WorldDatabase];
    geographyDb = mock[GeographyDatabase];
    industryDb = mock[IndustryDatabase];
    bs = new BuildingServlet(db, sk, pf);
    session = new Session(43, sk);
    avatar = new Avatar(1, session.id, 1, 1, 1, "hej");

    when(db.world).thenReturn(worldDb);
    when(db.industry).thenReturn(industryDb);
    when(db.geography).thenReturn(geographyDb);
    when(pf.findPath(any[Int], any[Int], any[Int])).thenReturn(new Path());
  }

  "BuildingServlet" should "throw appropriate exception if avatar does not belong to session" in {
    intercept[AuthorizationFailureException] {
      avatar.accountId = session.id + 1;
      bs.checkIfAvatarBelongsToSession(avatar, session);
    }
  }

  it should "not throw any exception if a given avatar belongs to a given session" in {
    bs.checkIfAvatarBelongsToSession(avatar, session);
  }

  it should "throw appropriate exception if a given buildingTypeId does not belong to the same race as a given avatar" in {
    when(industryDb.getBuildingTypeById(1)).thenReturn(new BuildingType(1, "a", "b", "c", avatar.raceId + 1, 1, 10, 1.0, 0));
    intercept[InvalidServletInputException] {
      bs.checkIfBuildingTypeBelongsToSameRaceAsAvatar(1, avatar);
    }
  }

  it should "not throw any exception if a given avatar belongs to the same race as a given building type" in {
    when(industryDb.getBuildingTypeById(1)).thenReturn(new BuildingType(1, "a", "b", "c", avatar.raceId, 1, 10, 1.0, 0));
    bs.checkIfBuildingTypeBelongsToSameRaceAsAvatar(1, avatar);
  }

  it should "throw appropriate exception if a given industryHubBuildingId does not belong to the avatar" in {
    when(industryDb.getBuildingTypeById(1)).thenReturn(new BuildingType(1, "", "", "", 1, 1, 10, 1.0, 0));
    intercept[InvalidServletInputException] {
      bs.checkIfIndustryHubIsValid(new Building(1, 1, 1, avatar.id + 1, 0, 0, 0, 0), new Building(2, 1, 1, avatar.id, 0, 0, 0, 0), avatar);
    }
  }

  it should "throw appropriate exception if a given industryHubBuildingId is not in the same area as the new building" in {
    val areaId = 101;
    val locationIdOfNewBuilding = 105;
    val locationIdOfIndustryHub = 72;
    when(industryDb.getBuildingTypeById(1)).thenReturn(new BuildingType(1, "", "", "", 1, 1, 10, 1.0, 0));
    when(geographyDb.getLocationById(locationIdOfNewBuilding)).thenReturn(new Location(1, 1, areaId, 0, 0));
    when(geographyDb.getLocationById(locationIdOfIndustryHub)).thenReturn(new Location(2, 1, areaId + 1, 2, 0));
    intercept[InvalidServletInputException] {
      bs.checkIfIndustryHubIsValid(new Building(1, locationIdOfIndustryHub, 1, avatar.id, 0, 0, 0, 0), new Building(2, locationIdOfNewBuilding, 1, avatar.id, 0, 0, 0, 0), avatar);
    }
  }

  it should "not throw any exception if a given industryHubBuildingId is valid" in {
    val areaId = 101;
    val locationIdOfNewBuilding = 105;
    val locationIdOfIndustryHub = 72;
    val industryHubType = 2;
    when(industryDb.getBuildingTypeById(1)).thenReturn(new BuildingType(1, "", "", "", 1, 1, 10, 1.0, 0));
    when(industryDb.getBuildingTypeById(industryHubType)).thenReturn(new BuildingType(1, "", "", "", 1, 1, 10, 1.0, 10));
    when(geographyDb.getLocationById(locationIdOfNewBuilding)).thenReturn(new Location(1, 1, areaId, 0, 0));
    when(geographyDb.getLocationById(locationIdOfIndustryHub)).thenReturn(new Location(2, 1, areaId, 2, 0));
    bs.checkIfIndustryHubIsValid(new Building(1, locationIdOfIndustryHub, industryHubType, avatar.id, 0, 0, 0, 0), new Building(2, locationIdOfNewBuilding, 1, avatar.id, 0, 0, 0, 0), avatar);
  }
}