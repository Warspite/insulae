package com.warspite.insulae.database.world

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object Race extends StoredType {
  val fields = List("id", "name", "description", "canonicalName");

  def apply(r: DataRecord) = {
    new Race(
      id = r.get[Int]("id"),
      name = r.get[String]("name"),
      description = r.get[String]("description"),
      canonicalName = r.get[String]("canonicalName"))
  }

  def apply(a: Race) = {
    new Race(
      id = a.id,
      name = a.name,
      description = a.description,
      canonicalName = a.canonicalName)
  }
}

class Race(var id: Int, var name: String, var description: String, var canonicalName: String) extends Mappable {
  def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "description" -> description,
      "canonicalName" -> canonicalName)

    if (includeNonDatabaseInsertionFields)
      map += "id" -> id;

    return map
  }
}

