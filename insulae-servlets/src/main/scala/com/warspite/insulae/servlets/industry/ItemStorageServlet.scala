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

class ItemStorageServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    val session = auth(request);
    try {
      if (params.contains("buildingId")) {
        if(db.world.getAvatarById(db.industry.getBuildingById(params.getInt("buildingId")).avatarId).accountId != session.id)
          throw new AuthorizationFailureException(session);
        
        Map[String, Any]("itemStorages" -> db.industry.getItemStorageByBuildingId(params.getInt("buildingId")));
      } else if (params.contains("areaId") && params.contains("avatarId")) {
        if(db.world.getAvatarById(params.getInt("avatarId")).accountId != session.id)
          throw new AuthorizationFailureException(session);
        
        Map[String, Any]("itemStorages" -> db.industry.getItemStorageByAreaIdAndAvatarId(params.getInt("areaId"), params.getInt("avatarId")));
      } else {
        throw new ClientReadableException("Bad servlet parameters received. Need either buildingId, or areaId and avatarId.", "Your request are lacking required parameters. Either supply 'buildingId', or 'areaId' and 'avatarId'.");
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }
}
