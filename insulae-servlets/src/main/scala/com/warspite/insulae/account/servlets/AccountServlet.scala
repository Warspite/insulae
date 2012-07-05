package com.warspite.insulae.account.servlets;

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
import com.warspite.common.database.UnexpectedNumberOfRowsException

class AccountServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends JsonServlet(sessionKeeper) {
  val ACCOUNT_ID_PARAMETER_NAME = "accountId";
  val ACCOUNT_EMAIL_PARAMETER_NAME = "accountEmail";

  override def get(request: HttpServletRequest): String = {
    val session = authenticateRequest(request);
    val accountId = getIntParameter(ACCOUNT_ID_PARAMETER_NAME, request, false);
    val accountEmail = getStringParameter(ACCOUNT_EMAIL_PARAMETER_NAME, request, false);

    if (accountId == null && accountEmail == null)
      throw new ClientReadableException("Malformed request received. Either " + ACCOUNT_ID_PARAMETER_NAME + " or " + ACCOUNT_EMAIL_PARAMETER_NAME + " required.", "Couldn't find any " + ACCOUNT_ID_PARAMETER_NAME + " or " + ACCOUNT_EMAIL_PARAMETER_NAME + " parameter in your request!");

    if (accountId != null && accountEmail != null)
      throw new ClientReadableException("Malformed request received. Both " + ACCOUNT_ID_PARAMETER_NAME + " and " + ACCOUNT_EMAIL_PARAMETER_NAME + " present, but only one allowed.", "Found both " + ACCOUNT_ID_PARAMETER_NAME + " and " + ACCOUNT_EMAIL_PARAMETER_NAME + " parameters in your request! Now I'm all confused, I can only handle one at a time :(");

    try {
      var account: Account = null;

      if (accountId != null)
        account = db.account.getAccountById(accountId);
      else
        account = db.account.getAccountByEmail(accountEmail);

      if (session.id != account.id)
        throw new ClientReadableException("Unauthorized access attempt to account " + account.id + " by session " + session.id + ".", "It seems you tried to get an account you don't have access to! That's not very good :(");

      return jsonify(Map("id" -> account.id, "email" -> account.email, "givenName" -> account.givenName, "surname" -> account.surname, "callSign" -> account.callSign)); 
    } catch {
      case e: UnexpectedNumberOfRowsException => throw new ClientReadableException(e.getMessage(), "Sorry! Couldn't find the requested account.");
      case e: AccountException => throw new ClientReadableException(e.getMessage(), "Sorry! Couldn't find the requested account.");
    }
  }
}
