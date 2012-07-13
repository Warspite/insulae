package com.warspite.insulae.servlets.account

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.common.servlets.sessions.SessionKeeper
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.account.AccountDatabase
import javax.servlet.http.HttpServletRequest
import com.warspite.common.database.DataRecord
import com.warspite.common.servlets.ClientReadableException
import org.mockito.Matchers._
import com.warspite.insulae.database.account.Account
import org.mockito.ArgumentCaptor
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.account.AccountEmailDoesNotExistException
import com.warspite.common.servlets.sessions.Session

@RunWith(classOf[JUnitRunner])
class LoginServletSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var accountDb: AccountDatabase = null;
  var httpReq: HttpServletRequest = null;
  var ls: LoginServlet = null;
  val password = "dobedobedo";
  val accountReturnedFromDb = new Account(37, "someFancyEmail", PasswordHasher.hash(password), "c", "d", "e");

  override def beforeEach() {
    sk = mock[SessionKeeper];
    db = mock[InsulaeDatabase];
    accountDb = mock[AccountDatabase];
    httpReq = mock[HttpServletRequest];
    ls = new LoginServlet(db, sk);

    when(sk.put(any[Int])).thenReturn(new Session(accountReturnedFromDb.id, sk));
    when(db.account).thenReturn(accountDb);
    when(accountDb.getAccountByEmail(any[String])).thenReturn(accountReturnedFromDb);
  }

  "LoginServlet" should "throw appropriate exception if required parameters are missing" in {
    intercept[ClientReadableException] {
      ls.post(httpReq, DataRecord(Map("email" -> "doesntmatter")));
    }
    intercept[ClientReadableException] {
      ls.post(httpReq, DataRecord(Map("password" -> "somePass")));
    }
  }

  it should "throw appropriate exception if account is missing" in {
    when(accountDb.getAccountByEmail(any[String])).thenThrow(new AccountEmailDoesNotExistException(""));
    intercept[ClientReadableException] {
      ls.post(httpReq, DataRecord(Map("email" -> "doesntmatter", "password" -> "somePass")));
    }
  }

  it should "throw appropriate exception if password does not match" in {
    intercept[ClientReadableException] {
      ls.post(httpReq, DataRecord(Map("email" -> "doesntmatter", "password" -> "somePass")));
    }
  }

  it should "return a session containing correct id if account exists and password matches" in {
    val result = ls.post(httpReq, DataRecord(Map("email" -> "doesntmatter", "password" -> password)));
    result.get("id").get should equal(accountReturnedFromDb.id);
  }

  it should "put a new session in the session keeper" in {
    val result = ls.post(httpReq, DataRecord(Map("email" -> "doesntmatter", "password" -> password)));
    verify(sk).put(accountReturnedFromDb.id);
  }
}