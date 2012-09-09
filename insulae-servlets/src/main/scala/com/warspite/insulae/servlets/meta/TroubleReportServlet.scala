package com.warspite.insulae.servlets.meta;

import com.warspite.common.servlets._
import sessions._
import com.warspite.insulae.database._
import javax.servlet.http.HttpServlet
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.account.Account
import com.warspite.insulae.database.account.AccountException
import com.warspite.common.database.ExpectedRecordNotFoundException
import com.warspite.common.database.DataRecord
import com.warspite.common.database.IncompleteDataRecordException
import com.warspite.common.database.IncompatibleTypeInDataRecordException
import com.warspite.common.database.DatabaseException
import com.warspite.insulae.database.account.AccountEmailAlreadyExistsException
import com.warspite.insulae.database.account.AccountCallSignAlreadyExistsException
import com.warspite.insulae.database.world.Avatar
import com.warspite.insulae.database.world.AvatarNameAlreadyExistsException
import com.warspite.insulae.database.world.AvatarDataInconsistentException
import com.warspite.insulae.database.meta.TroubleReport
import org.scala_tools.time.Imports._

class TroubleReportServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def put(req: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    try {
      val tr = new TroubleReport(0, params.getInt("troubleReportTypeId"), params.getString("slogan"), params.getString("content"), DateTime.now);
      logger.info("Received " + tr);
      db.meta.putTroubleReport(tr);
      Map[String, Any]();
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompleteDataRecordException => throw new MissingParameterException(true, "troubleReportTypeId", "slogan", "content");
      case e: DatabaseException => throw new ClientReadableException(e, "There's some unexpected trouble with the database, so I couldn't perform that action just now...");
    }
  }
}
