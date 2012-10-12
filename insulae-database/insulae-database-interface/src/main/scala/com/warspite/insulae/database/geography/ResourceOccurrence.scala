package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.StoredType

object ResourceOccurrence {
  val AREA_TYPE_ID = "areaTypeId";
  val LOCATION_TYPE_ID = "locationTypeId";
  val RESOURCE_TYPE_ID = "resourceTypeId";
  val OCCURRENCE = "occurrence";
  val fields = List(AREA_TYPE_ID, LOCATION_TYPE_ID, RESOURCE_TYPE_ID, OCCURRENCE) ++ StoredType.fields;

  def apply(r: DataRecord) = {
    new ResourceOccurrence(
      areaTypeId = r.get[Int](ResourceOccurrence.AREA_TYPE_ID),
      locationTypeId = r.get[Int](ResourceOccurrence.LOCATION_TYPE_ID),
      resourceTypeId = r.get[Int](ResourceOccurrence.RESOURCE_TYPE_ID),
      occurrence = r.get[Double](ResourceOccurrence.OCCURRENCE));
  }

  def apply(a: ResourceOccurrence) = {
    new ResourceOccurrence(
      areaTypeId = a.areaTypeId,
      locationTypeId = a.locationTypeId,
      resourceTypeId = a.resourceTypeId,
      occurrence = a.occurrence);
  }
}

class ResourceOccurrence(var areaTypeId: Int, var locationTypeId: Int, var resourceTypeId: Int, var occurrence: Double) extends StoredType {
}

