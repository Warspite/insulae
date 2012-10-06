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
import com.warspite.insulae.database.industry.BuildingAtLocationIdAlreadyExistsException
import com.warspite.insulae.mechanisms.Authorizer

object BuildingActionAutomationServlet {
  val PARAM_BUILDINGID = "buildingId";
  val PARAM_ACTIONID = "actionId";
}

class BuildingActionAutomationServlet(val db: InsulaeDatabase, sessionKeeper: SessionKeeper, val authorizer: Authorizer) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def post(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      val session = auth(request);
      if (params.contains(BuildingActionAutomationServlet.PARAM_BUILDINGID) && params.contains(BuildingActionAutomationServlet.PARAM_ACTIONID)) {
        val b = db.industry.getBuildingById(params.getInt(BuildingActionAutomationServlet.PARAM_BUILDINGID));
        authorizer.authBuilding(session, b);

        val aId = params.getInt(BuildingActionAutomationServlet.PARAM_ACTIONID);
        
        if(aId != 0) {
          val capableActions = db.industry.getActionByBuildingTypeId(b.buildingTypeId);
          val matchingActions = capableActions.filter(a => a.id == aId);
          if( matchingActions.size != 1)
            throw new ClientReadableException("Invalid " + BuildingActionAutomationServlet.PARAM_ACTIONID + " given for automating " + b + ". There are " + matchingActions.size + " actions with that actionId of which the building is capable, but there must be exactly 1.", "Bummer, the building you selected isn't capable of performing that action!");
          
          val a = matchingActions.head;
          if(a.requiresLocationId)
            throw new ClientReadableException("Attempted to automate " + a + " in " + b + ", but it requires a location target.", a.name + " requires a location target. Regrettably, only actions that do not require location targets can be automated.");
        }
        
        db.industry.setBuildingActionAutomation(b.id, aId);
        Map[String,Any]();
      } else {
        throw new MissingParameterException(true, BuildingActionAutomationServlet.PARAM_ACTIONID, BuildingActionAutomationServlet.PARAM_BUILDINGID);
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }
}
