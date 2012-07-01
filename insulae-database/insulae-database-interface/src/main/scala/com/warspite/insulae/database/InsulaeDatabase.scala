package com.warspite.insulae.database
import account.AccountDatabase
import java.util.Properties
import com.warspite.common.database.Database

abstract class InsulaeDatabase(props: Properties) extends Database(props) {
  def account: AccountDatabase;
}