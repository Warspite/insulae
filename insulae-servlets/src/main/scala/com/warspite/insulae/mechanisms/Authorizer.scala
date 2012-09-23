package com.warspite.insulae.mechanisms
import com.warspite.common.cli.WarspitePoller
import com.warspite.common.cli.CliListener
import com.warspite.common.cli.annotations.Cmd
import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.mechanisms.industry.ItemHoarder
import com.warspite.common.servlets.AuthorizationFailureException
import com.warspite.common.servlets.sessions.Session
import com.warspite.insulae.database.industry.Building

class Authorizer(val db: InsulaeDatabase) {
  def authBuilding(session: Session, buildingId: Int) {
    authBuilding(session, db.industry.getBuildingById(buildingId));
  }

  def authBuilding(session: Session, building: Building) {
    if (db.world.getAvatarById(building.avatarId).accountId != session.id)
      throw new AuthorizationFailureException(session);
  }
  
  def authAvatar(session: Session, avatarId: Int) {
    if (db.world.getAvatarById(avatarId).accountId != session.id)
      throw new AuthorizationFailureException(session);
  }
}