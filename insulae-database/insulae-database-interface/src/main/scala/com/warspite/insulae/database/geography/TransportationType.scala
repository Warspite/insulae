package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object TransportationType {
  val fields = "name" :: IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new TransportationType(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String]("name"))
  }

  def apply(a: TransportationType) = {
    new TransportationType(
      id = a.id,
      name = a.name)
  }
}

class TransportationType(id: Int, var name: String) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name)

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

