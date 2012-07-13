package com.warspite.insulae.servlets.account;

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

object AccountServlet {
  val MINIMUM_AVATAR_CALL_SIGN_LENGTH = 4;
}

class AccountServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      val session = auth(request);
      var account: Account = null;
      if (params.contains("id"))
        account = db.account.getAccountById(params.getInt("id"));
      else if (params.contains("email"))
        account = db.account.getAccountByEmail(params.getString("email"));
      else
        throw new ClientReadableException("Malformed request received. Either id or email required.", "Couldn't find any id or email parameter in your request!");

      if (session.id != account.id)
        throw new AuthorizationFailureException(session);

      return account.asMap(true, false)
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested account.");
      case e: AccountException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested account.");
    }
  }

  override def put(req: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      val a = new Account(0, params.getString("email"), PasswordHasher.hash(params.getString("password")), params.getString("callSign"), params.getString("givenName"), params.getString("surname"))

      if(a.callSign.length() < AccountServlet.MINIMUM_AVATAR_CALL_SIGN_LENGTH)
        throw new ClientReadableException("Too short call sign entered.", "Your account's call sign must be at least " + AccountServlet.MINIMUM_AVATAR_CALL_SIGN_LENGTH + " characters long!");
      
      
      return db.account.putAccount(a).asMap(true, false);
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompleteDataRecordException => throw new MissingParameterException(true, "email", "password", "callSign", "givenName", "surname");
      case e: AccountEmailAlreadyExistsException  => throw new ClientReadableException(e, "The email you entered is already tied to an account!");
      case e: AccountCallSignAlreadyExistsException  => throw new ClientReadableException(e, "Your call sign is already taken!");
      case e: DatabaseException => throw new ClientReadableException(e, "There's some unexpected trouble with the database, so I couldn't perform that action just now...");
    }
  }
}
