package com.warspite.insulae.servlets.world;

import com.warspite.common.servlets._
import sessions._
import com.warspite.insulae.database._
import javax.servlet.http.HttpServlet
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.account.Account
import com.warspite.insulae.database.account.AccountException
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.DataRecord
import com.warspite.common.database.IncompleteDataRecordException
import com.warspite.common.database.IncompatibleTypeInDataRecordException
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.account.AccountEmailAlreadyExistsException
import com.warspite.insulae.database.account.AccountCallSignAlreadyExistsException
import com.warspite.insulae.database.world.Avatar
import com.warspite.insulae.database.world.AvatarNameAlreadyExistsException
import com.warspite.insulae.database.world.AvatarDataInconsistentException
import com.warspite.insulae.mechanisms.geography.AreaCreator
import com.warspite.insulae.mechanisms.geography.Surveyor
import scala.util.Random
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.StartingLocation
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.geography.LocationCoordinatesDoNotExistException
import com.warspite.insulae.database.industry.BuildingAtLocationIdDoesNotExistException
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.BuildingType
import com.warspite.insulae.mechanisms.geography.PathFinder

object AvatarServlet {
  val rand = new Random(System.currentTimeMillis());
}

class AvatarServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper, areaCreator: AreaCreator, surveyor: Surveyor, pathFinder: PathFinder) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (params.contains("id")) {
        db.world.getAvatarById(params.getInt("id")).asMap();
      } else if (params.contains("realmId")) {
        Map[String, Any]("avatars" -> db.world.getAvatarByRealmId(params.getInt("realmId")));
      } else if (params.contains("accountId")) {
        val session = auth(request);
        val requestedAccountId = params.getInt("accountId");
        if (session.id != requestedAccountId)
          throw new AuthorizationFailureException(session);

        Map[String, Any]("avatars" -> db.world.getAvatarByAccountId(requestedAccountId));
      } else {
        throw new MissingParameterException(false, "id", "realmId", "accountId");
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested race.");
    }
  }

  override def put(req: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      val session = auth(req);
      var newAvatar = new Avatar(0, session.id, params.getInt("realmId"), params.getInt("raceId"), params.getInt("sexId"), params.getString("name"));

      InputChecker.checkLength(newAvatar.name, "name", 4, 16);

      checkIfAccountAlreadyHasAvatarInRealm(session, newAvatar);


      val startingLocation = findStartingLocation(newAvatar, db.world.getRaceById(newAvatar.raceId));
      newAvatar = db.world.putAvatar(newAvatar);
      createStartingBuildings(newAvatar, db.geography.getLocationById(startingLocation.locationId));

      return newAvatar.asMap(true, false);
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompleteDataRecordException => throw new MissingParameterException(true, "realmId", "raceId", "sexId", "name");
      case e: NoStartingLocationFoundException => throw new ClientReadableException(e, "Couldn't find any starting location for your avatar! This isn't supposed to happen and we'll get straight to work analyzing it, but maybe you could try another race?");
      case e: AvatarNameAlreadyExistsException => throw new ClientReadableException(e, "The avatar name you entered is already taken in that realm!");
      case e: AvatarDataInconsistentException => throw new ClientReadableException(e, "The avatar data you entered has internal inconsistencies!");
      case e: DatabaseException => throw new ClientReadableException(e, "There's some unexpected trouble with the database, so I couldn't perform that action just now...");
    }
  }

  def checkIfAccountAlreadyHasAvatarInRealm(session: Session, newAvatar: Avatar) {
    db.world.getAvatarByAccountId(session.id).foreach(a => {
      if (a.realmId == newAvatar.realmId)
        throw new ClientReadableException("Session " + session.id + " attempted to put a new avatar into realm " + newAvatar.realmId + ", although it already contains one tied to that account.", "Sorry, you're only allowed one avatar per realm!");
    });
  }

  def createStartingBuildings(avatar: Avatar, location: Location) {
    var nonHubStartingBuildings = Map[Building, BuildingType]();
    var startingHub: Building = null;
    for (startingBuilding <- db.industry.getStartingBuildingByRaceId(avatar.raceId)) {
      val bType = db.industry.getBuildingTypeById(startingBuilding.buildingTypeId);
      
      val b = new Building(
        id = 0,
        locationId = db.geography.getLocationByCoordinates(location.areaId, location.coordinatesX + startingBuilding.deltaX, location.coordinatesY + startingBuilding.deltaY).id,
        buildingTypeId = bType.id,
        avatarId = avatar.id,
        actionPoints = bType.maximumActionPoints,
        reservedActionPoints = 0,
        industryHubBuildingId = 0,
        hubDistanceCost = 0,
        automatedActionId = 0);

      if (bType.isIndustryHub) {
        if (startingHub != null)
          throw new MultipleStartingHubsException(avatar.raceId);

        startingHub = b;
      } else {
        nonHubStartingBuildings += b -> bType;
      }
    }

    if (startingHub == null)
      throw new NoStartingHubsException(avatar.raceId);

    val createdStartingHub = db.industry.putBuilding(startingHub);
    
    for((b, bType) <- nonHubStartingBuildings) {
      b.industryHubBuildingId = createdStartingHub.id;
      b.hubDistanceCost = pathFinder.findPath(bType.transportationTypeId, b.locationId, startingHub.locationId).cost();
      db.industry.putBuilding(b);
    }
  }

  def isStartingLocationOk(location: Location, race: Race): Boolean = {
    if (!surveyor.findBuildingsWithinRange(location, race.minimumStartingLocationClearRadius).isEmpty)
      return false;

    for (startingBuilding <- db.industry.getStartingBuildingByRaceId(race.id))
      if (!canCoordinatesBeBuiltOn(location.areaId, location.coordinatesX + startingBuilding.deltaX, location.coordinatesY + startingBuilding.deltaY))
        return false;

    return true;
  }

  def canCoordinatesBeBuiltOn(areaId: Int, x: Int, y: Int): Boolean = {
    try {
      val location = db.geography.getLocationByCoordinates(areaId, x, y);
      db.industry.getBuildingByLocationId(location.id)
      return false;
    } catch {
      case e: LocationCoordinatesDoNotExistException => return false;
      case e: BuildingAtLocationIdDoesNotExistException => return true;
    }
  }

  def findStartingLocation(avatar: Avatar, race: Race, createAreaAndTryAgainIfNoneFound: Boolean = true): StartingLocation = {
    var startingLocations = db.geography.getStartingLocationByRaceIdAndRealmId(avatar.raceId, avatar.realmId);

    while (!startingLocations.isEmpty) {
      var potentialStartingLocation = startingLocations(AvatarServlet.rand.nextInt(startingLocations.length));

      db.geography.deleteStartingLocationByLocationIdAndRaceId(potentialStartingLocation.locationId, potentialStartingLocation.raceId);

      if (isStartingLocationOk(db.geography.getLocationById(potentialStartingLocation.locationId), race))
        return potentialStartingLocation;

      startingLocations = db.geography.getStartingLocationByRaceIdAndRealmId(avatar.raceId, avatar.realmId);
    }

    if(!createAreaAndTryAgainIfNoneFound)
      throw new NoStartingLocationFoundException(avatar, race);
    
    areaCreator.createStartingArea(race);
    return findStartingLocation(avatar, race, false);
  }
}
