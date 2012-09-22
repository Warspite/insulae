package com.warspite.insulae.servlets.industry;

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

object ItemHoardingOrderServlet {
  val PARAM_BUILDINGID = "buildingId";
  val PARAM_AVATARID = "avatarId";
}

class ItemHoardingOrderServlet(db: InsulaeDatabase, sessionKeeper: SessionKeeper) extends RequestHeaderAuthenticator(sessionKeeper) {
  override def get(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    val session = auth(request);
    try {
      if (params.contains(ItemHoardingOrderServlet.PARAM_BUILDINGID)) {
        authBuilding(session, params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID));
        Map[String, Any]("itemHoardingOrders" -> db.industry.getItemHoardingOrderByBuildingId(params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID)));
      } else if (params.contains(ItemHoardingOrderServlet.PARAM_AVATARID)) {
        authAvatar(session, params.getInt(ItemHoardingOrderServlet.PARAM_AVATARID));
        Map[String, Any]("itemHoardingOrders" -> db.industry.getItemHoardingOrderByAvatarId(params.getInt(ItemHoardingOrderServlet.PARAM_AVATARID)));
      } else {
        throw new MissingParameterException(false, ItemHoardingOrderServlet.PARAM_BUILDINGID, ItemHoardingOrderServlet.PARAM_BUILDINGID);
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  def authBuilding(session: Session, buildingId: Int) {
    if (db.world.getAvatarById(db.industry.getBuildingById(buildingId).avatarId).accountId != session.id)
      throw new AuthorizationFailureException(session);
  }

  def authAvatar(session: Session, avatarId: Int) {
    if (db.world.getAvatarById(avatarId).accountId != session.id)
      throw new AuthorizationFailureException(session);
  }
}
