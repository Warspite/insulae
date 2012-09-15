package com.warspite.insulae.mechanisms.industry

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => the, any }
import com.warspite.insulae.database.InsulaeDatabase
import org.mockito.Matchers._
import org.mockito.ArgumentCaptor
import com.warspite.insulae.mechanisms.geography.PathFinder
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.industry.IndustryDatabase
import com.warspite.insulae.database.industry.Action
import com.warspite.insulae.database.industry.LocationTypeRequiredNearActionTargetLocation
import com.warspite.insulae.database.geography.Location
import scala.collection.mutable.{Map => MMap}

@RunWith(classOf[JUnitRunner])
class ActionVerifierSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var db: InsulaeDatabase = null;
  var geoDb: GeographyDatabase = null;
  var indDb: IndustryDatabase = null;
  var pf: PathFinder = null;
  var av: ActionVerifier = null;

  var targetedLocation = new Location(1, 1, 1, 1, 1, false);
  var targetedAction = new Action(1, "someName", "someDescription", "someAction", 5, 0, true, 2, 0);
  var req1 = new LocationTypeRequiredNearActionTargetLocation(targetedAction.id, 1, 1, 0);
  var req2 = new LocationTypeRequiredNearActionTargetLocation(targetedAction.id, 2, 1, 1);
  var locationTypeRequirementsAtTarget = Array(req1, req2);

  override def beforeEach() {
    db = mock[InsulaeDatabase];
    geoDb = mock[GeographyDatabase];
    indDb = mock[IndustryDatabase];
    pf = mock[PathFinder];
    av = new ActionVerifier(db, pf);

    when(db.geography).thenReturn(geoDb);
    when(db.industry).thenReturn(indDb);

  }

  "ActionVerifier" should "succeed when pathfinder returns the required location types" in {
    when(indDb.getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(locationTypeRequirementsAtTarget);
    when(pf.countLocationTypesWithinRange(targetedLocation, req1.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1));
    when(pf.countLocationTypesWithinRange(targetedLocation, req2.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1, 2 -> 1));

    av.verifyTargetLocationIsNearRequiredLocationTypes(targetedLocation, targetedAction);

    verify(indDb).getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }

  it should "fail when pathfinder does not return the required location types" in {
    when(indDb.getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(locationTypeRequirementsAtTarget);
    when(pf.countLocationTypesWithinRange(targetedLocation, req1.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1));
    when(pf.countLocationTypesWithinRange(targetedLocation, req2.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1, 3 -> 2));

    intercept[RequiredLocationTypesNotFoundNearTargetLocationException] {
      av.verifyTargetLocationIsNearRequiredLocationTypes(targetedLocation, targetedAction);
    }

    verify(indDb).getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }
}