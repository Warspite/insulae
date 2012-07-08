package com.warspite.insulae.account.servlets;

import com.warspite.common.servlets._
import sessions._
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.common.database.DataRecord

class LoginServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def post(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {

    val s: Session = sessionKeeper.put(params.getInt("accountId"));
    return Map("id" -> s.id, "key" -> s.key);
  }
}
