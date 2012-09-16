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

object ResourceRequiredNearActionTargetLocationServlet {
  val PARAM_ACTIONID = "actionId";
  val OUTPUT_COLLECTION_NAME = "resourcesRequiredNearActionTargetLocation"
}

class ResourceRequiredNearActionTargetLocationServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (params.contains(ResourceRequiredNearActionTargetLocationServlet.PARAM_ACTIONID)) {
        Map[String, Any](ResourceRequiredNearActionTargetLocationServlet.OUTPUT_COLLECTION_NAME -> db.industry.getResourcesRequiredNearActionTargetLocationByActionId(params.getInt(ResourceRequiredNearActionTargetLocationServlet.PARAM_ACTIONID)));
      } else {
        Map[String, Any](ResourceRequiredNearActionTargetLocationServlet.OUTPUT_COLLECTION_NAME -> db.industry.getResourcesRequiredNearActionTargetLocationAll());
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }
}
