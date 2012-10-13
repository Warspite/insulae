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
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.industry.Building
import com.warspite.insulae.database.industry.IndustryDatabase
import scala.collection.mutable.{ Map => MMap }


@RunWith(classOf[JUnitRunner])
class SurveyorSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var db: InsulaeDatabase = null;
  var geoDb: GeographyDatabase = null;
  var s: Surveyor = null;

  val locNW = new Location(1, 1, 1, 1, 1, false, false);
  val locNC = new Location(2, 1, 1, 2, 1, false, false);
  val locNE = new Location(3, 1, 1, 3, 1, false, false);
  val locCW = new Location(4, 2, 1, 1, 2, false, false);
  val locCC = new Location(5, 2, 1, 2, 2, false, false);
  val locCE = new Location(6, 2, 1, 3, 2, false, false);
  val locSW = new Location(7, 3, 1, 1, 3, false, false);
  val locSC = new Location(8, 3, 1, 2, 3, false, false);
  val locSE = new Location(9, 3, 1, 3, 3, false, false);

  val locType1 = new LocationType(1, "locType1", "locType1", "locType1", "ffffff");
  val locType2 = new LocationType(2, "locType2", "locType2", "locType2", "ffffff");
  val locType3 = new LocationType(3, "locType3", "locType3", "locType3", "ffffff");
  
  val resourceCW = new Resource(1, locCW.id);
  val resourceSW = new Resource(2, locSW.id);

  override def beforeEach() {
    db = mock[InsulaeDatabase];
    geoDb = mock[GeographyDatabase];
    s = new Surveyor(db);

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

    when(geoDb.getLocationByAreaId(locCC.areaId)).thenReturn(Array(locNW, locNC, locNE, locCW, locCC, locCE, locSW, locSC, locSE));

    when(geoDb.getLocationTypeById(locType1.id)).thenReturn(locType1);
    when(geoDb.getLocationTypeById(locType2.id)).thenReturn(locType2);
    when(geoDb.getLocationTypeById(locType3.id)).thenReturn(locType3);

    when(geoDb.getLocationNeighborByLocationId(locNW.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locNW.id, locNC.id), new LocationNeighbor(locNW.id, locCW.id)));
    when(geoDb.getLocationNeighborByLocationId(locNC.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locNC.id, locNW.id), new LocationNeighbor(locNC.id, locNE.id), new LocationNeighbor(locNC.id, locCC.id)));
    when(geoDb.getLocationNeighborByLocationId(locNE.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locNE.id, locNC.id), new LocationNeighbor(locNE.id, locSE.id)));
    when(geoDb.getLocationNeighborByLocationId(locCW.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locCW.id, locNW.id), new LocationNeighbor(locCW.id, locSW.id), new LocationNeighbor(locCW.id, locCC.id)));
    when(geoDb.getLocationNeighborByLocationId(locCC.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locCC.id, locNC.id), new LocationNeighbor(locCC.id, locSC.id), new LocationNeighbor(locCC.id, locCW.id), new LocationNeighbor(locCC.id, locCE.id)));
    when(geoDb.getLocationNeighborByLocationId(locCE.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locCE.id, locCC.id), new LocationNeighbor(locCE.id, locNE.id), new LocationNeighbor(locCE.id, locSE.id)));
    when(geoDb.getLocationNeighborByLocationId(locSW.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locSW.id, locCW.id), new LocationNeighbor(locSW.id, locSC.id)));
    when(geoDb.getLocationNeighborByLocationId(locSC.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locSC.id, locSW.id), new LocationNeighbor(locSC.id, locCC.id), new LocationNeighbor(locSC.id, locSE.id)));
    when(geoDb.getLocationNeighborByLocationId(locSE.id)).thenReturn(Array[LocationNeighbor](new LocationNeighbor(locSE.id, locSC.id), new LocationNeighbor(locSE.id, locNE.id)));

    when(geoDb.getResourceByLocationId(any[Int])).thenReturn(Array[Resource]());
    when(geoDb.getResourceByLocationId(resourceCW.locationId)).thenReturn(Array(resourceCW));
    when(geoDb.getResourceByLocationId(resourceSW.locationId)).thenReturn(Array(resourceSW));
  }

  "Surveyor" should "accurately count location types within range 0" in {
    var types = s.countLocationTypesWithinRange(locCC, 0);
    types(locCC.locationTypeId) should equal(1);
  }

  it should "accurately count location types within range 1" in {
    var types = s.countLocationTypesWithinRange(locNW, 1);
    types(locNW.locationTypeId) should equal(2); //locNC has same type, so there should be two
    types(locCW.locationTypeId) should equal(1);
  }

  it should "accurately count resources within range 0" in {
    var r = s.countResourcesWithinRange(locCW, 0);
    r(resourceCW.resourceTypeId) should equal(1);
  }

  it should "accurately count resources within range 1" in {
    var r = s.countResourcesWithinRange(locSC, 1);
    r.contains(resourceCW.resourceTypeId) should equal(false);
    r(resourceSW.resourceTypeId) should equal(1);
  }

  it should "find a correct list of locations with range 0" in {
    var l = s.findLocationsWithinRange(locNW.id, 0);
    l.size should equal(1);
    l.contains(locNW.id) should equal(true);
  }

  it should "find a correct list of locations with range 1" in {
    var l = s.findLocationsWithinRange(locNW.id, 1);
    l.size should equal(3);
    l.contains(locNW.id) should equal(true);
    l.contains(locNC.id) should equal(true);
    l.contains(locCW.id) should equal(true);
  }
}