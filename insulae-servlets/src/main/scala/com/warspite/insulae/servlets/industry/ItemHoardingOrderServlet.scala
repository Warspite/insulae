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
import com.warspite.insulae.database.industry.ItemHoardingOrder

object ItemHoardingOrderServlet {
  val PARAM_BUILDINGID = "buildingId";
  val PARAM_AVATARID = "avatarId";
  val PARAM_ITEMTYPEID = "itemTypeId";
  val PARAM_AMOUNT = "amount";
  val PARAM_PRIORITY = "priority";
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
        throw new MissingParameterException(false, ItemHoardingOrderServlet.PARAM_BUILDINGID, ItemHoardingOrderServlet.PARAM_AVATARID);
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }

  override def delete(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    val session = auth(request);
    try {
      if (params.contains(ItemHoardingOrderServlet.PARAM_BUILDINGID) && params.contains(ItemHoardingOrderServlet.PARAM_ITEMTYPEID)) {
        authBuilding(session, params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID));
        db.industry.deleteItemHoardingOrderByBuildingIdAndItemTypeId(params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID), params.getInt(ItemHoardingOrderServlet.PARAM_ITEMTYPEID));
        Map[String, Any]();
      } else {
        throw new MissingParameterException(true, ItemHoardingOrderServlet.PARAM_BUILDINGID, ItemHoardingOrderServlet.PARAM_ITEMTYPEID);
      }
    } catch {
      case e: ClientReadableException => throw e;
      case e: IncompatibleTypeInDataRecordException => throw new ClientReadableException(e, "Sorry, I couldn't quite understand your request parameters. Please ensure they're not out of whack.");
      case e: ExpectedRecordNotFoundException => throw new ClientReadableException(e, "Sorry! Couldn't find the requested data.");
    }
  }
  
  override def put(request: HttpServletRequest, params: DataRecord): Map[String, Any] = {
    val session = auth(request);
    try {
      if (params.contains(ItemHoardingOrderServlet.PARAM_BUILDINGID) && params.contains(ItemHoardingOrderServlet.PARAM_ITEMTYPEID) && params.contains(ItemHoardingOrderServlet.PARAM_AMOUNT) && params.contains(ItemHoardingOrderServlet.PARAM_PRIORITY)) {
        authBuilding(session, params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID));
        db.industry.deleteItemHoardingOrderByBuildingIdAndItemTypeId(params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID), params.getInt(ItemHoardingOrderServlet.PARAM_ITEMTYPEID));
        db.industry.putItemHoardingOrder(new ItemHoardingOrder(params.getInt(ItemHoardingOrderServlet.PARAM_BUILDINGID), params.getInt(ItemHoardingOrderServlet.PARAM_ITEMTYPEID), params.getInt(ItemHoardingOrderServlet.PARAM_AMOUNT), params.getInt(ItemHoardingOrderServlet.PARAM_PRIORITY)));
        Map[String, Any]();
      } else {
        throw new MissingParameterException(true, ItemHoardingOrderServlet.PARAM_BUILDINGID, ItemHoardingOrderServlet.PARAM_ITEMTYPEID, ItemHoardingOrderServlet.PARAM_AMOUNT, ItemHoardingOrderServlet.PARAM_PRIORITY);
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
