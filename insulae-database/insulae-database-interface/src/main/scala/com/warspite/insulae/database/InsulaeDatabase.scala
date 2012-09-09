package com.warspite.insulae.database
import account.AccountDatabase
import java.util.Properties
import com.warspite.common.database.Database
import com.warspite.insulae.database.world.WorldDatabase
import com.warspite.insulae.database.geography.GeographyDatabase
import com.warspite.insulae.database.industry.IndustryDatabase
import com.warspite.insulae.database.meta.MetaDatabase

abstract class InsulaeDatabase(props: Properties) extends Database(props) {
  def account: AccountDatabase;
  def world: WorldDatabase;
  def geography: GeographyDatabase;
  def industry: IndustryDatabase;
  def meta: MetaDatabase;
}