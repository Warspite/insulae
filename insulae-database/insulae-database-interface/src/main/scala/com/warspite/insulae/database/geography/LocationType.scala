package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.DescriptiveType
import com.warspite.common.database.types.IdentifiedType

object LocationType {
  val COLOR = "color";
  val fields = List(COLOR) ++ DescriptiveType.fields;

  def apply(r: DataRecord) = {
    new LocationType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      description = r.get[String](DescriptiveType.DESCRIPTION),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME),
      color = r.get[String](LocationType.COLOR));
  }

  def apply(a: LocationType) = {
    new LocationType(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName,
      color = a.color);
  }
}

class LocationType(id: Int, name: String, description: String, canonicalName: String, var color: String) extends DescriptiveType(id, name, description, canonicalName) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      LocationType.COLOR -> color);

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

