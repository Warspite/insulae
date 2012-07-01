package com.warspite.insulae.account.servlets;

import com.warspite.common.servlets._
import sessions._
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.InsulaeDatabase

class LoginServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends JsonServlet(sessionKeeper) {
  override def post(request: HttpServletRequest): String = {

    val s: Session = sessionKeeper.put(getIntParameter(SESSION_ID_PARAMETER_NAME, request));
    return jsonify(Map("id" -> s.id, "key" -> s.key));
  }
}
