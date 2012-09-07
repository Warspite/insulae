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
import com.warspite.insulae.mechanisms.industry.ActionPerformer
import com.warspite.insulae.mechanisms.industry.DepositFailedException
import com.warspite.insulae.mechanisms.industry.ItemTransactionException
import com.warspite.insulae.mechanisms.industry.InsufficientItemStorageForWithdrawalException

object ActionServlet {
  val PARAM_ID = "id";
  val PARAM_ACTIONID = "actionId";
  val PARAM_TARGETLOCATIONID = "targetLocationId";
  val PARAM_BUILDINGTYPEID = "buildingTypeId";
}

class ActionServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper, val actionPerformer: ActionPerformer) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (params.contains(ActionServlet.PARAM_ID)) {
        db.industry.getActionById(params.getInt(ActionServlet.PARAM_ID)).asMap();
      } else if (params.contains(ActionServlet.PARAM_BUILDINGTYPEID)) {
        Map[String, Any]("actions" -> db.industry.getActionByBuildingTypeId(params.getInt(ActionServlet.PARAM_BUILDINGTYPEID)));
      } else {
        Map[String, Any]("actions" -> db.industry.getActionAll());
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  override def post(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (!params.contains(ActionServlet.PARAM_ACTIONID))
        throw new MissingParameterException(true, ActionServlet.PARAM_ACTIONID);

      val session = auth(request);
      var agent = determineActionAgent(params, session);
      val action = db.industry.getActionById(params.getInt(ActionServlet.PARAM_ACTIONID));
      var targetLocationId = ActionPerformer.UNSET_TARGET_LOCATION_ID;
      if(params.contains(ActionServlet.PARAM_TARGETLOCATIONID))
        targetLocationId = params.getInt(ActionServlet.PARAM_TARGETLOCATIONID);
      
      actionPerformer.perform(action, agent, targetLocationId);

      Map[String, Any]();
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: InsufficientItemStorageForWithdrawalException => throw new ClientReadableException(e, "There isn't enough " + db.industry.getItemTypeById(e.withdrawal.itemTypeId).name + " available to perform this action (" + e.withdrawal.amount + " required).");
      case e: ItemTransactionException => throw new ClientReadableException(e, "Sorry, an economic transaction involved in performing your action failed.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  def determineActionAgent(params: DataRecord, session: Session): Object = {
    if (params.contains("agentBuildingId")) {
      val agent = db.industry.getBuildingById(params.getInt("agentBuildingId"));
      val controllingAvatar = db.world.getAvatarById(agent.avatarId);
      if (controllingAvatar.accountId != session.id)
        throw new AuthorizationFailureException(session);

      return agent;
    }

    throw new MissingActionAgentParameterException();
  }
}
