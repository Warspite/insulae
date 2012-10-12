package com.warspite.insulae.database.world

import com.warspite.common.database.DataRecord
import com.warspite.insulae.database._
import org.scala_tools.time.Imports._
import com.warspite.common.database.Mappable
import com.warspite.common.database.types.IdentifiedType
import com.warspite.common.database.types.DescriptiveType

object Realm {
  val START_DATE = "startDate";
  val END_DATE = "endDate";
  val fields = List(DescriptiveType.NAME, DescriptiveType.CANONICAL_NAME, Realm.START_DATE, Realm.END_DATE) ++ IdentifiedType.fields;

  def apply(r: DataRecord) = {
    new Realm(
      id = r.get[Int](IdentifiedType.ID),
      name = r.get[String](DescriptiveType.NAME),
      canonicalName = r.get[String](DescriptiveType.CANONICAL_NAME),
      startDate = r.get[DateTime](Realm.START_DATE),
      endDate = r.get[DateTime](Realm.END_DATE))
  }

  def apply(a: Realm) = {
    new Realm(
      id = a.id,
      name = a.name,
      canonicalName = a.canonicalName,
      startDate = a.startDate,
      endDate = a.endDate)
  }
}

class Realm(id: Int, var name: String, var canonicalName: String, var startDate: DateTime, var endDate: DateTime) extends IdentifiedType(id) {
  override def asMap(includeNonDatabaseInsertionFields: Boolean = true, includeSensitiveInformation: Boolean = false): Map[String, Any] = {
    
    var map = Map[String, Any](
      DescriptiveType.NAME -> name,
      DescriptiveType.CANONICAL_NAME -> canonicalName,
      Realm.START_DATE -> startDate,
      Realm.END_DATE -> endDate)

    return map ++ super.asMap(includeNonDatabaseInsertionFields, includeSensitiveInformation);
  }
}

