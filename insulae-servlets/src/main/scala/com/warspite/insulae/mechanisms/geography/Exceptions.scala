package com.warspite.insulae.mechanisms.geography

import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.world.Race

class InsulaeMechanismException(msg: String, inner: Throwable) extends RuntimeException (msg, inner) {
  def this(msg: String) = this(msg, null);
}

class NoPathFoundException(start: Location, target: Location) extends InsulaeMechanismException("Failed to find a path from location " + start.id + " to " + target.id + ".") {}

class NoStartingAreaTemplatesFoundException(race: Race) extends InsulaeMechanismException("Couldn't find any starting area template for " + race); 

class AreaTemplateCreationException(msg: String) extends InsulaeMechanismException(msg) {}
class AreaCreationException(msg: String) extends InsulaeMechanismException(msg) {}
