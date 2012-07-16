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
      val newAvatar = new Avatar(0, session.id, params.getInt("realmId"), params.getInt("raceId"), params.getInt("sexId"), params.getString("name"));
      
      InputChecker.checkLength(newAvatar.name, "name", 4, 16);
      
      checkIfAccountAlreadyHasAvatarInReal(session, newAvatar);
      
      return db.world.putAvatar(newAvatar).asMap(true, false);
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompleteDataRecordException => throw new MissingParameterException(true, "realmId", "raceId", "sexId", "name");
       case e: AvatarNameAlreadyExistsException  => throw new ClientReadableException(e, "The avatar name you entered is already taken in that realm!");
      case e: AvatarDataInconsistentException  => throw new ClientReadableException(e, "The avatar data you entered has internal inconsistencies!");
      case e: DatabaseException => throw new ClientReadableException(e, "There's some unexpected trouble with the database, so I couldn't perform that action just now...");
    }
  }
  
  def checkIfAccountAlreadyHasAvatarInReal(session: Session, newAvatar: Avatar) {
      db.world.getAvatarByAccountId(session.id).foreach(a => {
        if(a.realmId == newAvatar.realmId)
          throw new ClientReadableException("Session " + session.id + " attempted to put a new avatar into realm " + newAvatar.realmId + ", although it already contains one tied to that account.", "Sorry, you're only allowed one avatar per realm!");
      });
  }
}
