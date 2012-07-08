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
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.DataRecord
import com.warspite.common.database.IncompleteDataRecordException
import com.warspite.common.database.IncompatibleTypeInDataRecordException

class AccountServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  val ACCOUNT_ID_PARAMETER_NAME = "accountId";
  val ACCOUNT_EMAIL_PARAMETER_NAME = "accountEmail";

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
        throw new ClientReadableException("Unauthorized access attempt to account " + account.id + " by session " + session.id + ".", "It seems you tried to get an account you don't have access to! That's not very good :(");

      return Map("id" -> account.id, "email" -> account.email, "givenName" -> account.givenName, "surname" -> account.surname, "callSign" -> account.callSign);
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested account.");
      case e: AccountException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested account.");
    }
  }
}
