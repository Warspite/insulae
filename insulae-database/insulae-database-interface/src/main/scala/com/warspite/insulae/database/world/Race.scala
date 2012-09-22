package com.warspite.insulae.database.world

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.DescriptiveType

object Race {
  val fields = DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new Race(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME))
  }

  def apply(a: Race) = {
    new Race(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName)
  }
}

class Race(id: Int, name: String, description: String, canonicalName: String) extends DescriptiveType(id, name, description, canonicalName) {
}

