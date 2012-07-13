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

class AvatarServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      if (params.contains("id")) {
        db.world.getAvatarById(params.getInt("id")).asMap();
      } else if (params.contains("realmId")) {
        Map[String, Any]("avatars" -> db.world.getAvatarByRealmId(params.getInt("realmId")));
      } else if (params.contains("accountId")) {
        val session = auth(request);
        val requestedAccountId = params.getInt("accountId");
        if(session.id != requestedAccountId)
          throw new AuthorizationFailureException(session);
        
        Map[String, Any]("avatars" -> db.world.getAvatarByAccountId(requestedAccountId));
      } else {
        throw new ClientReadableException("Missing parameters from servlet request.", "I need either 'id', 'realmId' or 'accountId' to handle this request!");
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested race.");
    }
  }
}
