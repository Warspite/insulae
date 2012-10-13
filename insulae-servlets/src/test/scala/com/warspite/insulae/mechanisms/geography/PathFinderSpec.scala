package com.warspite.insulae.mechanisms.geography

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.account.AccountDatabase
import com.warspite.common.database.DataRecord
import org.mockito.Matchers._
import com.warspite.insulae.database.account.Account
import org.mockito.ArgumentCaptor
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.LocationType
import com.warspite.insulae.database.geography.TransportationType
import com.warspite.insulae.database.geography.TransportationCost
import com.warspite.insulae.database.geography.LocationNeighbor
import com.warspite.insulae.database.geography.Resource

@RunWith(classOf[JUnitRunner])
class PathFinderSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var db: InsulaeDatabase = null;
  var geoDb: GeographyDatabase = null;
  var pf: PathFinder = null;

  val locNW = new Location(1, 1, 1, 1, 1, false, false);
  val locNC = new Location(2, 1, 1, 2, 1, false, false);
  val locNE = new Location(3, 1, 1, 3, 1, false, false);
  val locCW = new Location(4, 2, 1, 1, 2, false, false);
  val locCC = new Location(5, 2, 1, 2, 2, false, false);
  val locCE = new Location(6, 2, 1, 3, 2, false, false);
  val locSW = new Location(7, 3, 1, 1, 3, false, false);
  val locSC = new Location(8, 3, 1, 2, 3, false, false);
  val locSE = new Location(9, 3, 1, 3, 3, false, false);
  val locDisconnected = new Location(10, 1, 1, 4, 3, false, false);
  val locOtherArea = new Location(11, 1, 2, 2, 2, false, false);

  val locType1 = new LocationType(1, "locType1", "locType1", "locType1", "ffffff");
  val locType2 = new LocationType(2, "locType2", "locType2", "locType2", "ffffff");
  val locType3 = new LocationType(3, "locType3", "locType3", "locType3", "ffffff");

  val transType1 = new TransportationType(1, "transType1");
  val transType2 = new TransportationType(2, "transType2");

  val transCost11 = new TransportationCost(locType1.id, transType1.id, 1, 1);
  val transCost12 = new TransportationCost(locType1.id, transType2.id, 5, 5);
  val transCost21 = new TransportationCost(locType2.id, transType1.id, 2, 2);
  val transCost22 = new TransportationCost(locType2.id, transType2.id, 10, 10);
  val transCost31 = new TransportationCost(locType3.id, transType1.id, 3, 3);
  val transCost32 = new TransportationCost(locType3.id, transType2.id, 15, 15);
  
  override def beforeEach() {
    db = mock[InsulaeDatabase];
    geoDb = mock[GeographyDatabase];
    pf = new PathFinder(db, 2);

    when(db.geography).thenReturn(geoDb);

    when(geoDb.getLocationById(locNW.id)).thenReturn(locNW);
    when(geoDb.getLocationById(locNC.id)).thenReturn(locNC);
    when(geoDb.getLocationById(locNE.id)).thenReturn(locNE);
    when(geoDb.getLocationById(locCW.id)).thenReturn(locCW);
    when(geoDb.getLocationById(locCC.id)).thenReturn(locCC);
    when(geoDb.getLocationById(locCE.id)).thenReturn(locCE);
    when(geoDb.getLocationById(locSW.id)).thenReturn(locSW);
    when(geoDb.getLocationById(locSC.id)).thenReturn(locSC);
    when(geoDb.getLocationById(locSE.id)).thenReturn(locSE);
    when(geoDb.getLocationById(locDisconnected.id)).thenReturn(locDisconnected);
    when(geoDb.getLocationById(locOtherArea.id)).thenReturn(locOtherArea);

    when(geoDb.getLocationTypeById(locType1.id)).thenReturn(locType1);
    when(geoDb.getLocationTypeById(locType2.id)).thenReturn(locType2);
    when(geoDb.getLocationTypeById(locType3.id)).thenReturn(locType3);

    when(geoDb.getTransportationCostByLocationTypeIdAndTransportationTypeId(locType1.id, transType1.id)).thenReturn(transCost11);
    when(geoDb.getTransportationCostByLocationTypeIdAndTransportationTypeId(locType1.id, transType2.id)).thenReturn(transCost12);
    when(geoDb.getTransportationCostByLocationTypeIdAndTransportationTypeId(locType2.id, transType1.id)).thenReturn(transCost21);
    when(geoDb.getTransportationCostByLocationTypeIdAndTransportationTypeId(locType2.id, transType2.id)).thenReturn(transCost22);
    when(geoDb.getTransportationCostByLocationTypeIdAndTransportationTypeId(locType3.id, transType1.id)).thenReturn(transCost31);
    when(geoDb.getTransportationCostByLocationTypeIdAndTransportationTypeId(locType3.id, transType2.id)).thenReturn(transCost32);

    when(geoDb.getLocationNeighborByLocationId(locNW.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locNW.id, locNC.id), new LocationNeighbor(locNW.id, locCW.id)));
    when(geoDb.getLocationNeighborByLocationId(locNC.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locNC.id, locNW.id), new LocationNeighbor(locNC.id, locNE.id), new LocationNeighbor(locNC.id, locCC.id)));
    when(geoDb.getLocationNeighborByLocationId(locNE.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locNE.id, locNC.id), new LocationNeighbor(locNE.id, locSE.id)));
    when(geoDb.getLocationNeighborByLocationId(locCW.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locCW.id, locNW.id), new LocationNeighbor(locCW.id, locSW.id), new LocationNeighbor(locCW.id, locCC.id)));
    when(geoDb.getLocationNeighborByLocationId(locCC.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locCC.id, locNC.id), new LocationNeighbor(locCC.id, locSC.id), new LocationNeighbor(locCC.id, locCW.id), new LocationNeighbor(locCC.id, locCE.id)));
    when(geoDb.getLocationNeighborByLocationId(locCE.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locCE.id, locCC.id), new LocationNeighbor(locCE.id, locNE.id), new LocationNeighbor(locCE.id, locSE.id)));
    when(geoDb.getLocationNeighborByLocationId(locSW.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locSW.id, locCW.id), new LocationNeighbor(locSW.id, locSC.id), new LocationNeighbor(locSC.id, locOtherArea.id)));
    when(geoDb.getLocationNeighborByLocationId(locSC.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locSC.id, locSW.id), new LocationNeighbor(locSC.id, locCC.id), new LocationNeighbor(locSC.id, locSE.id)));
    when(geoDb.getLocationNeighborByLocationId(locSE.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locSE.id, locSC.id), new LocationNeighbor(locSE.id, locNE.id), new LocationNeighbor(locSE.id, locOtherArea.id)));
    when(geoDb.getLocationNeighborByLocationId(locOtherArea.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locOtherArea.id, locSW.id), new LocationNeighbor(locOtherArea.id, locSE.id)));
    
    when(geoDb.getResourceByLocationId(any[Int])).thenReturn(Array[Resource]());
  }

  "PathFinder" should "calculate correct cost to neighbor location with transportation type 1" in {
    var p = pf.findPath(transType1, locNC, locNW);
    p.movementStack.size should equal(1);
    p.cost() should equal(1);
  }

  it should "calculate correct cost to neighbor location with transportation type 2" in {
    var p = pf.findPath(transType2, locNC, locNW);
    p.movementStack.size should equal(1);
    p.cost() should equal(5);
  }

  it should "calculate correct cost to location two steps away with transportation type 1" in {
    var p = pf.findPath(transType1, locNW, locCC);
    p.movementStack.size should equal(2);
    p.cost() should equal(3);
  }

  it should "throw appropriate exception if there is no path" in {
    intercept[NoPathFoundException] {
      var p = pf.findPath(transType1, locCC, locDisconnected);
    }
  }

  it should "return an empty path if starting point and destination are the same" in {
    var p = pf.findPath(transType1, locNE, locNE);
    p.movementStack.size should equal(0);
  }

  it should "plot path through other area if cheaper" in {
    var p = pf.findPath(transType2, locSW, locSE);
    p.movementStack.size should equal(2);
    p.cost() should equal(24);
  }

  it should "not plot path through other area if more expensive" in {
    var p = pf.findPath(transType1, locSW, locSE);
    p.movementStack.size should equal(2);
    p.cost() should equal(6);
  }
}