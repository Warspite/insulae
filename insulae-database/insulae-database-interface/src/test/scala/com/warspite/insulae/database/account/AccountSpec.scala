package com.warspite.insulae.database.account

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.common.database.DataRecord
import com.warspite.common.database.IncompleteDataRecordException

@RunWith(classOf[JUnitRunner])
class AccountSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var dr: DataRecord = null;

  override def beforeEach() {
    dr = new DataRecord();
    dr.put("id", 5);
    dr.put("email", "a");
    dr.put("passwordHash", "b");
    dr.put("callSign", "c");
    dr.put("givenName", "d");
    dr.put("surname", "e");
  }

  "Account" should "create an instance from a data record" in {
    val a = Account(dr);
    a.id should equal(5);
    a.email should equal("a");
    a.passwordHash should equal("b");
    a.callSign should equal("c");
    a.givenName should equal("d");
    a.surname should equal("e");
  }

  it should "throw exception when attempting to create with incomplete data record" in {
    dr.remove("callSign");
    intercept[IncompleteDataRecordException] {
      Account(dr);
    }
  }
}