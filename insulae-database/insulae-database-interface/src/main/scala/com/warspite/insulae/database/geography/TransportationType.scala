package com.warspite.insulae.database.geography

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object TransportationType extends StoredType {
  val fields = List("id", "name");

  def apply(r: DataRecord) = {
    new TransportationType(
      id = r.get[Int]("id"),
      name = r.get[String]("name"))
  }

  def apply(a: TransportationType) = {
    new TransportationType(
      id = a.id,
      name = a.name)
  }
}

class TransportationType(var id: Int, var name: String) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name)

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
}

