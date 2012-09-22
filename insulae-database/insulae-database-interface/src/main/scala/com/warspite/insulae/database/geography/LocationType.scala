package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType

object LocationType {
  val fields = DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new LocationType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME))
  }

  def apply(a: LocationType) = {
    new LocationType(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName)
  }
}

class LocationType(id: Int, name: String, description: String, canonicalName: String) extends DescriptiveType(id, name, description, canonicalName) {
}

