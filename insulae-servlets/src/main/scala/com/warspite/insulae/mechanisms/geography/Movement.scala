package com.warspite.insulae.mechanisms.geography
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.TransportationType

class Movement(val transportationType: TransportationType, val startingLocation: Location, val targetLocation: Location, val cost: Int) {
  override def toString = "Movement by " + transportationType.name + " " + startingLocation + "->" + targetLocation;
}