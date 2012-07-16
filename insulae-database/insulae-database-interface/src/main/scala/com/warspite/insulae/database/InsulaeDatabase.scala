package com.warspite.insulae.database
import account.AccountDatabase
import java.util.Properties
import com.warspite.common.database.Database
import com.warspite.insulae.database.world.WorldDatabase
import com.warspite.insulae.database.geography.GeographyDatabase

abstract class InsulaeDatabase(props: Properties) extends Database(props) {
  def account: AccountDatabase;
  def world: WorldDatabase;
  def geography: GeographyDatabase;
}