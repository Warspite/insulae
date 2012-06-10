package com.warspite.insulae.account.servlets;

import com.warspite.common.servlets._
import sessions._
import javax.servlet.http.HttpServlet
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

class Account(sessionKeeper: SessionKeeper) extends JsonServlet(sessionKeeper) {
  val ACCOUNT_ID_PARAMETER_NAME = "accountId";
  
  override def get(request: HttpServletRequest): String = {
    val session = authenticateRequest(request);
    val accountId = getIntParameter(ACCOUNT_ID_PARAMETER_NAME, request);
    
    if(session.id != accountId)
      throw new ClientReadableException("Unauthorized access attempt to account " + accountId + " by session " + session.id + ".", "It seems you tried to get an account you don't have access to! That's not very good :(");
    
    return "\"accountId\": " + session.id
  }
}
