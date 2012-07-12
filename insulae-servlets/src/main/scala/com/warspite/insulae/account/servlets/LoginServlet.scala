package com.warspite.insulae.account.servlets;

import com.warspite.common.servlets._
import sessions._
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.common.database.DataRecord
import com.warspite.insulae.account.PasswordHasher
import com.warspite.common.database.IncompleteDataRecordException
import com.warspite.insulae.database.account.AccountEmailDoesNotExistException

class LoginServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def post(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {

      val email = params.getString("email");
      val passwordHash = PasswordHasher.hash(params.getString("password"));
      val account = db.account.getAccountByEmail(email);
      
      if(account.passwordHash != passwordHash)
    	  throw new ClientReadableException("Failed login attempt to email '" + email + "'.", "You didn't supply a correct password!");
      
      val s = sessionKeeper.put(account.id);
      return Map("id" -> s.id, "key" -> s.key); 
      
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompleteDataRecordException => throw new ClientReadableException(e, "The login information was incomplete. Please supply both 'email' and 'password'.");
      case e: AccountEmailDoesNotExistException => throw new ClientReadableException(e, "Sorry, there's no account tied to the email you supplied.");
    }
  }
}
