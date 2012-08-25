package com.warspite.insulae.mechanisms.geography

import com.warspite.insulae.database.geography.Location

class InsulaeMechanismException(msg: String, inner: Throwable) extends RuntimeException (msg, inner) {
  def this(msg: String) = this(msg, null);
}

class NoPathFoundException(start: Location, target: Location) extends InsulaeMechanismException("Failed to find a path from location " + start.id + " to " + target.id + ".") {}

