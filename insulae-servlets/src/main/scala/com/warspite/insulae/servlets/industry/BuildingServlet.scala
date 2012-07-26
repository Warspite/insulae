package com.warspite.insulae.servlets.industry;

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
import com.warspite.insulae.database.world.AvatarIdDoesNotExistException
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.world.Avatar
import com.warspite.insulae.database.geography.LocationIdDoesNotExistException
import com.warspite.insulae.database.industry.BuildingTypeIdDoesNotExistException
import com.warspite.insulae.database.industry.BuildingAtLocationIdAlreadyExistsException
import com.warspite.insulae.database.industry.BuildingIdDoesNotExistException

class BuildingServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (params.contains("locationId")) {
        db.industry.getBuildingByLocationId(params.getInt("locationId")).asMap();
      } else if (params.contains("id")) {
        db.industry.getBuildingById(params.getInt("id")).asMap();
      } else if (params.contains("areaId")) {
        Map[String, Any]("buildings" -> db.industry.getBuildingByAreaId(params.getInt("areaId")));
      } else {
        throw new MissingParameterException(false, "id", "locationId", "areaId");
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: BuildingIdDoesNotExistException => throw new ClientReadableException(e, "It would seem that building doesn't exist."); 
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  override def delete(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if(!params.contains("id"))
        throw new MissingParameterException(true, "id");

      val session = auth(request);
      val building = db.industry.getBuildingById(params.getInt("id"));
      val avatar = db.world.getAvatarById(building.avatarId);
    
      if(avatar.accountId != session.id)
        throw new AuthorizationFailureException(session);
      
      db.industry.deleteBuildingById(building.id);
      Map[String, Any]();
    } catch {
      case e: ClientReadableException => throw e;
      case e: BuildingIdDoesNotExistException => throw new ClientReadableException(e, "The building you tried to destroy does not eixst."); 
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  override def put(req: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      val session = auth(req);
      var industryHubBuildingId: Int = 0;
      if (params.contains("industryHubBuildingId"))
        industryHubBuildingId = params.getInt("industryHubBuildingId");

      val newBuilding = new Building(0, params.getInt("locationId"), params.getInt("buildingTypeId"), params.getInt("avatarId"), 0, 0, industryHubBuildingId);
      val avatar = db.world.getAvatarById(newBuilding.avatarId);

      checkIfAvatarBelongsToSession(avatar, session);
      checkIfBuildingTypeBelongsToSameRaceAsAvatar(newBuilding.buildingTypeId, avatar);
      checkIfIndustryHubIsValid(newBuilding.industryHubBuildingId, newBuilding, avatar);

      return db.industry.putBuilding(newBuilding).asMap(true, false);
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompleteDataRecordException => throw new MissingParameterException(true, "locationId", "buildingTypeId", "avatarId", "industryHubBuildingId");
      case e: AvatarIdDoesNotExistException => throw new InvalidServletInputException("Invalid avatarId received.");
      case e: BuildingTypeIdDoesNotExistException => throw new ClientReadableException(e, "The building type you attempted to construct does not exist!"); 
      case e: BuildingAtLocationIdAlreadyExistsException => throw new ClientReadableException(e, "There is already a building at that location!"); 
      case e: BuildingIdDoesNotExistException => throw new ClientReadableException(e, "Couldn't find the designated industry hub."); 
      case e: DatabaseException => throw new ClientReadableException(e, "There's some unexpected trouble with the database, so I couldn't perform that action just now...");
    }
  }

  def checkIfAvatarBelongsToSession(avatar: Avatar, session: Session) {
    if (avatar.accountId != session.id)
      throw new AuthorizationFailureException(session);
  }

  def checkIfBuildingTypeBelongsToSameRaceAsAvatar(buildingTypeId: Int, avatar: Avatar) {
    if (db.industry.getBuildingTypeById(buildingTypeId).raceId != avatar.raceId)
      throw new InvalidServletInputException("Building type " + buildingTypeId + " is not available to race " + avatar.raceId + ", to which avatar " + avatar.id + " belongs.");
  }

  def checkIfIndustryHubIsValid(industryHubBuildingId: Int, newBuilding: Building, avatar: Avatar) {
    val buildingType = db.industry.getBuildingTypeById(newBuilding.buildingTypeId);
    if (buildingType.industryHubRange == 0) {
      val industryHub = db.industry.getBuildingById(industryHubBuildingId);
      if (industryHub.avatarId != avatar.id)
        throw new InvalidServletInputException("Industrial hub " + industryHubBuildingId + " is not owned by avatar " + avatar.id + ".");

      if (db.geography.getLocationById(industryHub.locationId).areaId != db.geography.getLocationById(newBuilding.locationId).areaId)
        throw new InvalidServletInputException("Industrial hub " + industryHubBuildingId + " is not in the same area as that of attempted building.");

      val industryHubType = db.industry.getBuildingTypeById(industryHub.buildingTypeId);
      if (industryHubType.industryHubRange == 0)
        throw new InvalidServletInputException("Building designated as industry hub (" + newBuilding.industryHubBuildingId + ") is in fact not an industry hub.");
    }
  }
}
