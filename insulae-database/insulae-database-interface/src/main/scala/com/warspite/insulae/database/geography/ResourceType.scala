package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType

object ResourceType {
  val fields = DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new ResourceType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION))
  }

  def apply(a: ResourceType) = {
    new ResourceType(
      id = a.id,
      name = a.name,
      canonicalName = a.canonicalName,
      description = a.description);
  }
}

class ResourceType(id: Int, name: String, canonicalName: String, description: String) extends DescriptiveType(id, name, description, canonicalName) {
}

