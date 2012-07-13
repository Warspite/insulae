package com.warspite.insulae.test.account

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.insulae.servlets.account.AccountServlet
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.account.AccountDatabase
import com.warspite.common.servlets.sessions.SessionKeeper
import com.warspite.common.servlets.sessions.Session
import javax.servlet.http.HttpServletRequest
import com.warspite.common.database.DataRecord
import com.warspite.insulae.database.account.Account
import com.warspite.common.servlets.ClientReadableException

@RunWith(classOf[JUnitRunner])
class AccountApiSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var accountDb: AccountDatabase = null;
  var as: AccountServlet = null;
  var httpReq: HttpServletRequest = null;
  var accountMatchingUsedSession: Account = null;
  var accountNotMatchingSession: Account = null;
  var sessionUsed: Session = null;

  override def beforeEach() {
    sk = new SessionKeeper();
    db = mock[InsulaeDatabase];
    accountDb = mock[AccountDatabase];
    httpReq = mock[HttpServletRequest];
    as = new AccountServlet(db, sk);

    sessionUsed = sk.put(100);
    accountMatchingUsedSession = new Account(sessionUsed.id, "mail!", "hash!", "call!", "given!", "sur!");
    accountNotMatchingSession = new Account(sessionUsed.id + 1, "mail.", "hash.", "call.", "given.", "sur.");
    when(httpReq.getHeader("auth")).thenReturn("{\"id\": " + sessionUsed.id + ", \"key\": " + sessionUsed.key + "}");
    when(db.account).thenReturn(accountDb);
  }

  "AccountApi" should "allow retrieve from database an existing account matching the session used" in {
    when(accountDb.getAccountById(accountMatchingUsedSession.id)).thenReturn(accountMatchingUsedSession);

    val res = as.get(httpReq, DataRecord(Map("id" -> accountMatchingUsedSession.id)));
    res.get("id").get should equal(accountMatchingUsedSession.id);
    res.get("callSign").get should equal(accountMatchingUsedSession.callSign);
  }

  it should "throw appropriate exception if used session does not match the account" in {
    when(accountDb.getAccountById(accountNotMatchingSession.id)).thenReturn(accountNotMatchingSession);

    intercept[ClientReadableException] {
      val res = as.get(httpReq, DataRecord(Map("id" -> accountNotMatchingSession.id)));
    }
  }
}