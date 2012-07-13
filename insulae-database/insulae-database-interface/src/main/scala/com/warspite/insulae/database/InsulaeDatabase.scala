package com.warspite.insulae.database
import account.AccountDatabase
import java.util.Properties
import com.warspite.common.database.Database
import com.warspite.insulae.database.realm.RealmDatabase

abstract class InsulaeDatabase(props: Properties) extends Database(props) {
  def account: AccountDatabase;
  def realm: RealmDatabase;
}