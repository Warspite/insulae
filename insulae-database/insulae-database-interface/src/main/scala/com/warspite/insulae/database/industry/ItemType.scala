package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable

object ItemType extends StoredType {
  val fields = List("id", "name", "canonicalName");

  def apply(r: DataRecord) = {
    new ItemType(
      id = r.get[Int]("id"),
      name = r.get[String]("name"),
      canonicalName = r.get[String]("canonicalName"))
  }

  def apply(a: ItemType) = {
    new ItemType(
      id = a.id,
      name = a.name,
      canonicalName = a.canonicalName)
  }
}

class ItemType(var id: Int, var name: String, var canonicalName: String) extends Mappable {
  def asMap(includeId: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "canonicalName" -> canonicalName)

    if (includeId)
      map += "id" -> id;

    return map
  }
}

