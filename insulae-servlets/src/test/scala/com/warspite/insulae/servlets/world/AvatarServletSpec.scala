package com.warspite.insulae.servlets.world

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
import javax.servlet.http.HttpServletRequest
import com.warspite.common.database.DataRecord
import com.warspite.common.servlets.ClientReadableException
import org.mockito.Matchers._
import org.mockito.ArgumentCaptor
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.world.WorldDatabase
import com.warspite.common.servlets.MissingParameterException
import com.warspite.common.servlets.sessions.Session
import com.warspite.insulae.database.world.Avatar

@RunWith(classOf[JUnitRunner])
class AvatarServletSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var worldDb: WorldDatabase = null;
  var httpReq: HttpServletRequest = null;
  var as: AvatarServlet = null;

  override def beforeEach() {
    sk = mock[SessionKeeper];
    db = mock[InsulaeDatabase];
    worldDb = mock[WorldDatabase];
    httpReq = mock[HttpServletRequest];
    as = new AvatarServlet(db, sk);

    when(db.world).thenReturn(worldDb);
    when(sk.get(any[Int], any[Int])).thenReturn(new Session(1, sk));
    when(httpReq.getHeader(any[String])).thenReturn("{\"id\": 1, \"key\": 2}");
  }

  "AvatarServlet" should "throw appropriate exception if required parameters are missing when putting avatar" in {
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "sexId" -> 1)))
    }
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "name" -> "1")))
    }
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "name" -> "1", "sexId" -> 1)))
    }
    intercept[MissingParameterException] {
      as.put(httpReq, DataRecord(Map[String, Any]("name" -> "1", "raceId" -> 1, "sexId" -> 1)))
    }
  }

  it should "throw appropriate exception if name is too short" in {
    intercept[ClientReadableException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "sexId" -> 1, "name" -> "123")))
    }
  }

  it should "throw appropriate exception if account already has avatar in that realm" in {
    when(worldDb.getAvatarByAccountId(any[Int])).thenReturn(Array[Avatar](new Avatar(1, 1, 2, 1, 1, "someName"), new Avatar(1, 1, 1, 1, 1, "someOtherName")))
    intercept[ClientReadableException] {
      as.put(httpReq, DataRecord(Map[String, Any]("realmId" -> 1, "raceId" -> 1, "sexId" -> 1, "name" -> "1234")))
    }
  }
}