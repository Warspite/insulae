package com.warspite.insulae.database.industry

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType

object ItemType {
  val fields = List("name", "canonicalName") ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new ItemType(
      id = r.get[Int](IdentifiedType.ID),
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

class ItemType(id: Int, var name: String, var canonicalName: String) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    var map = Map[String, Any](
      "name" -> name,
      "canonicalName" -> canonicalName)

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

