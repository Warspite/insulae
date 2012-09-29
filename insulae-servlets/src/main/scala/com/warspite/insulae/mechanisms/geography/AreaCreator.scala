package com.warspite.insulae.mechanisms.geography

import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.TransportationType
import scala.collection.mutable.Queue
import scala.collection.mutable.PriorityQueue
import scala.math._
import scala.util.control.Breaks._
import scala.collection.mutable.Stack
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.world.Race
import com.warspite.insulae.database.geography.Area

object AreaCreator {
}

class AreaCreator(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def createStartingArea(race: Race): Area = {
    throw new RuntimeException("Unimplemented!");
  }
}
