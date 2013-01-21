package com.warspite.insulae.servlets.geography

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
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.geography.Location

@RunWith(classOf[JUnitRunner])
class LocationServletSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var sk: SessionKeeper = null;
  var db: InsulaeDatabase = null;
  var ls: LocationServlet = null;

  override def beforeEach() {
    sk = new SessionKeeper();
    db = mock[InsulaeDatabase];
    ls = new LocationServlet(db, sk);
  }

  "LocationServlet" should "jsonify 10k locations rather quickly" in {
    var locations = List[Location]();
    var prep = System.currentTimeMillis();
    for (i <- 0 until 10000)
      locations ::= new Location(0, 1, 1, 0, 0, true, false);
    
    var start = System.currentTimeMillis();
    ls.jsonify(Map("locations" -> locations.toArray[Location]));
    var elapsed: Int = (System.currentTimeMillis() - start).toInt;
    elapsed should (be <= (750))
  }
}