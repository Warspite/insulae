package com.warspite.insulae.account.servlets;

import com.warspite.common.servlets._
import javax.servlet.http.HttpServlet
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

class Account extends JsonServlet {
  private val logger = LoggerFactory.getLogger(getClass());

  override def get(request: HttpServletRequest): String = {
    "\"id\": 1, \"email\": \"a@b.com\""
  }
}
