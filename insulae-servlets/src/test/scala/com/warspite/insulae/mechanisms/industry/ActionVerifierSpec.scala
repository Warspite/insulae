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
import scala.collection.mutable.{ Map => MMap }
import com.warspite.insulae.database.industry.ResourceRequiredNearActionTargetLocation
import com.warspite.insulae.mechanisms.geography.Surveyor

@RunWith(classOf[JUnitRunner])
class ActionVerifierSpec extends FlatSpec with ShouldMatchersForJUnit with BeforeAndAfterEach with MockitoSugar {
  var db: InsulaeDatabase = null;
  var geoDb: GeographyDatabase = null;
  var indDb: IndustryDatabase = null;
  var s: Surveyor = null;
  var av: ActionVerifier = null;

  var targetedLocation = new Location(1, 1, 1, 1, 1, false);
  var targetedAction = new Action(1, "someName", "someDescription", "someAction", 5, 0, true, 2, 0);
  var lReq1 = new LocationTypeRequiredNearActionTargetLocation(targetedAction.id, 1, 1, 0);
  var lReq2 = new LocationTypeRequiredNearActionTargetLocation(targetedAction.id, 2, 1, 1);
  var rReq1 = new ResourceRequiredNearActionTargetLocation(targetedAction.id, 1, 1, 0);
  var rReq2 = new ResourceRequiredNearActionTargetLocation(targetedAction.id, 2, 2, 1);
  var locationTypeRequirementsAtTarget = Array(lReq1, lReq2);
  var resourceRequirementsAtTarget = Array(rReq1, rReq2);

  override def beforeEach() {
    db = mock[InsulaeDatabase];
    geoDb = mock[GeographyDatabase];
    indDb = mock[IndustryDatabase];
    s = mock[Surveyor];
    av = new ActionVerifier(db, s);

    when(db.geography).thenReturn(geoDb);
    when(db.industry).thenReturn(indDb);

  }

  "ActionVerifier" should "succeed when the required location types are found" in {
    when(indDb.getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(locationTypeRequirementsAtTarget);
    when(s.countLocationTypesWithinRange(targetedLocation, lReq1.maximumRange)).thenReturn(Map[Int, Int](1 -> 1));
    when(s.countLocationTypesWithinRange(targetedLocation, lReq2.maximumRange)).thenReturn(Map[Int, Int](1 -> 1, 2 -> 1));

    av.verifyTargetLocationIsNearRequiredLocationTypes(targetedLocation, targetedAction);

    verify(indDb).getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }

  it should "fail when the required location types are not found" in {
    when(indDb.getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(locationTypeRequirementsAtTarget);
    when(s.countLocationTypesWithinRange(targetedLocation, lReq1.maximumRange)).thenReturn(Map[Int, Int](1 -> 1));
    when(s.countLocationTypesWithinRange(targetedLocation, lReq2.maximumRange)).thenReturn(Map[Int, Int](1 -> 1, 3 -> 2));

    intercept[RequiredLocationTypesNotFoundNearTargetLocationException] {
      av.verifyTargetLocationIsNearRequiredLocationTypes(targetedLocation, targetedAction);
    }

    verify(indDb).getLocationTypesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }

  it should "succeed when the required resources are found" in {
    when(indDb.getResourcesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(resourceRequirementsAtTarget);
    when(s.countResourcesWithinRange(targetedLocation, rReq1.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1));
    when(s.countResourcesWithinRange(targetedLocation, rReq2.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1, 2 -> 2));

    av.verifyTargetLocationIsNearRequiredResources(targetedLocation, targetedAction);

    verify(indDb).getResourcesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }

  it should "fail when the required resources are not found" in {
    when(indDb.getResourcesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(resourceRequirementsAtTarget);
    when(s.countResourcesWithinRange(targetedLocation, rReq1.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1));
    when(s.countResourcesWithinRange(targetedLocation, rReq2.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1));

    intercept[RequiredResourcesNotFoundNearTargetLocationException] {
      av.verifyTargetLocationIsNearRequiredResources(targetedLocation, targetedAction);
    }

    verify(indDb).getResourcesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }

  it should "fail when resources are found, but not in sufficient quantities" in {
    when(indDb.getResourcesRequiredNearActionTargetLocationByActionId(targetedAction.id)).thenReturn(resourceRequirementsAtTarget);
    when(s.countResourcesWithinRange(targetedLocation, rReq1.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1));
    when(s.countResourcesWithinRange(targetedLocation, rReq2.maximumRange)).thenReturn(MMap[Int, Int](1 -> 1, 2 -> 1));

    intercept[RequiredResourcesNotFoundNearTargetLocationException] {
      av.verifyTargetLocationIsNearRequiredResources(targetedLocation, targetedAction);
    }

    verify(indDb).getResourcesRequiredNearActionTargetLocationByActionId(targetedAction.id);
  }
}