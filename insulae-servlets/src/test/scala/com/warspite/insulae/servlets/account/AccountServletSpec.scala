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

@RunWith(classOf[JUnitRunner])
class AccountServletSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var accountDb: AccountDatabase = null;
  var httpReq: HttpServletRequest = null;
  var as: AccountServlet = null;
  val returnedAccountFromDb = new Account(37, "a", "b", "c", "d", "e");

  override def beforeEach() {
    sk = new SessionKeeper();
    db = mock[InsulaeDatabase];
    accountDb = mock[AccountDatabase];
    httpReq = mock[HttpServletRequest];
    as = new AccountServlet(db, sk);

    when(db.account).thenReturn(accountDb);

    when(accountDb.putAccount(any[Account])).thenReturn(returnedAccountFromDb);
  }

  "AccountServlet" should "throw appropriate exception if required parameters are missing when putting account" in {
    intercept[ClientReadableException] {
      as.put(httpReq, DataRecord(Map[String, Any]("password" -> "hejhejhej", "callSign" -> "someCallSign", "givenName" -> "someGivenName")))
    }
  }

  it should "return account returned from database if successful" in {
    val result = as.put(httpReq, DataRecord(Map[String, Any]("password" -> "hejhejhej", "email" -> "a@b.c", "callSign" -> "someCallSign", "givenName" -> "someGivenName", "surname" -> "someSurname")))
    result.get("email").get should equal(returnedAccountFromDb.email);
    result.get("surname").get should equal(returnedAccountFromDb.surname);
    result.get("id").get should equal(returnedAccountFromDb.id);
  }

  it should "attempt to put a received account into the database" in {
    as.put(httpReq, DataRecord(Map[String, Any]("password" -> "hejhejhej", "email" -> "a@b.c", "callSign" -> "someCallSign", "givenName" -> "someGivenName", "surname" -> "someSurname")))

    val arg = ArgumentCaptor.forClass(classOf[Account]);
    verify(accountDb).putAccount(arg.capture())
    arg.getValue().callSign should equal("someCallSign")
    arg.getValue().passwordHash should equal(PasswordHasher.hash("hejhejhej"));
  }

  it should "throw appropriate exception if database operation fails" in {
    when(accountDb.putAccount(any[Account])).thenThrow(new DatabaseException("danger!"));
    intercept[ClientReadableException] {
      as.put(httpReq, DataRecord(Map[String, Any]("password" -> "hejhejhejhej", "email" -> "a@b.c", "callSign" -> "someCallSign", "givenName" -> "someGivenName", "surname" -> "someSurname")))
    }
  }
}