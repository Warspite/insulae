package com.warspite.insulae.servlets.industry;

import com.warspite.common.servlets._
import sessions._
import com.warspite.insulae.database._
import com.warspite.insulae.mechanisms.geography._
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
import com.warspite.insulae.mechanisms.Authorizer

object BuildingServlet {
  val PARAM_ID = "id";
  val PARAM_RESERVEDACTIONPOINTS = "reservedActionPoints";
}

class BuildingServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper, pathFinder: PathFinder, val authorizer: Authorizer) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (params.contains("locationId")) {
        db.industry.getBuildingByLocationId(params.getInt("locationId")).asMap();
      } else if (params.contains(BuildingServlet.PARAM_ID)) {
        db.industry.getBuildingById(params.getInt(BuildingServlet.PARAM_ID)).asMap();
      } else if (params.contains("areaId")) {
        Map[String, Any]("buildings" -> db.industry.getBuildingByAreaId(params.getInt("areaId")));
      } else {
        throw new MissingParameterException(false, BuildingServlet.PARAM_ID, "locationId", "areaId");
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
      if (!params.contains(BuildingServlet.PARAM_ID))
        throw new MissingParameterException(true, BuildingServlet.PARAM_ID);

      val session = auth(request);
      val building = db.industry.getBuildingById(params.getInt(BuildingServlet.PARAM_ID));
      authorizer.authBuilding(session, building);

      db.industry.deleteBuildingById(building.id);
      Map[String, Any]();
    } catch {
      case e: ClientReadableException => throw e;
      case e: BuildingIdDoesNotExistException => throw new ClientReadableException(e, "The building you tried to destroy does not eixst.");
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  override def post(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (!params.contains(BuildingServlet.PARAM_ID) || !params.contains(BuildingServlet.PARAM_RESERVEDACTIONPOINTS))
        throw new MissingParameterException(true, BuildingServlet.PARAM_ID, BuildingServlet.PARAM_RESERVEDACTIONPOINTS);

      val session = auth(request);
      val b = db.industry.getBuildingById(params.getInt(BuildingServlet.PARAM_ID));
      authorizer.authBuilding(session, b);
      val bType = db.industry.getBuildingTypeById(b.buildingTypeId);
      val reservedActionPoints = scala.math.min(bType.maximumActionPoints, scala.math.max(0, params.getInt(BuildingServlet.PARAM_RESERVEDACTIONPOINTS)));

      db.industry.setBuildingReservedActionPoints(b.id, reservedActionPoints);
      Map[String, Any]();
    } catch {
      case e: ClientReadableException => throw e;
      case e: BuildingIdDoesNotExistException => throw new ClientReadableException(e, "The building you tried to destroy does not eixst.");
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }
}
