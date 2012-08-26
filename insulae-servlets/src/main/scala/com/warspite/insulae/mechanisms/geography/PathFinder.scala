package com.warspite.insulae.mechanisms.geography

import com.warspite.insulae.database.InsulaeDatabase
import com.warspite.insulae.database.geography.Location
import com.warspite.insulae.database.geography.TransportationType
import scala.collection.mutable.Queue
import scala.collection.mutable.PriorityQueue
import scala.math._
import scala.util.control.Breaks._
import scala.collection.mutable.Stack

case class PathEvaluatedLocation(var loc: Location, var accumulatedCostOfParent: Int, var cost: Int, var estimatedRemainingCost: Int) {
  def estimatedTotalCost() = { this.accumulatedCost() + this.estimatedRemainingCost }
  def accumulatedCost() = { this.accumulatedCostOfParent + this.cost }
}

object PathFinder {
  val AREA_TRANSITION_COST = 12;
}

class PathFinder(val db: InsulaeDatabase, val areaTransitionCost: Int) {
  def findPath(transportationTypeId: Int, startingLocationId: Int, targetLocationId: Int): Path = {
    return findPath(db.geography.getTransportationTypeById(transportationTypeId), db.geography.getLocationById(startingLocationId), db.geography.getLocationById(targetLocationId));
  }

  def findPath(transportationType: TransportationType, startingLocation: Location, targetLocation: Location): Path = {
    println("");
    println("");
    println("");
    println("");
    println("");
    println("");
    println("findPath()");
    var closedLocations = Queue[Int]();
    var tentativeLocations = List[PathEvaluatedLocation]();
    var parentLocations = scala.collection.mutable.Map[Int, PathEvaluatedLocation]();
    tentativeLocations ::= new PathEvaluatedLocation(startingLocation, 0, 0, estimateRemainingCost(startingLocation, targetLocation));

    while (!tentativeLocations.isEmpty) {
      var thisLoc = tentativeLocations.head;
      println("Investigation loc" + thisLoc.loc.id);

      if (thisLoc.loc.id == targetLocation.id)
        return buildPath(startingLocation, thisLoc, parentLocations, transportationType);

      tentativeLocations = tentativeLocations.drop(1);
      closedLocations += thisLoc.loc.id;

      db.geography.getLocationNeighborByLocationId(thisLoc.loc.id).map(n => {
        if (!closedLocations.contains(n.neighborLocationId)) {
          var neighborLoc = db.geography.getLocationById(n.neighborLocationId);
          var pathEvaluatedNeighbor = new PathEvaluatedLocation(neighborLoc, thisLoc.accumulatedCost(), calculateCost(thisLoc.loc, neighborLoc, transportationType), estimateRemainingCost(neighborLoc, targetLocation));

          tentativeLocations.find(t => t.loc.id == neighborLoc.id) match {
            case Some(t) => {
              println("    #" + t.loc.id + " is already tentative, its accumulated cost is " + t.accumulatedCost() + ", while via me it would be " + pathEvaluatedNeighbor.accumulatedCost());
              if (t.accumulatedCost() > pathEvaluatedNeighbor.accumulatedCost()) {
                t.accumulatedCostOfParent = pathEvaluatedNeighbor.accumulatedCostOfParent;
                t.cost = pathEvaluatedNeighbor.cost;
                parentLocations(t.loc.id) = thisLoc;
              }
            }
            case None => {
              tentativeLocations ::= pathEvaluatedNeighbor;
              parentLocations += n.neighborLocationId -> thisLoc;
            }
          }
        }
      });

      tentativeLocations = tentativeLocations.sortWith(_.estimatedTotalCost() < _.estimatedTotalCost());
      println("    I have " + tentativeLocations.size + " tentative locations.");
      for (t <- tentativeLocations) {
        println("    #" + t.loc.id + " (from #" + parentLocations(t.loc.id).loc.id + ") with acc. cost " + t.accumulatedCost() + " and est. total cost " + t.estimatedTotalCost());
      }
    }

    throw new NoPathFoundException(startingLocation, targetLocation);
  }

  def calculateCost(from: Location, to: Location, transportationType: TransportationType): Int = {
    var cost = db.geography.getTransportationCostByLocationTypeIdAndTransportationTypeId(to.locationTypeId, transportationType.id).costWithoutRoad;
    if (from.areaId != to.areaId)
      cost += areaTransitionCost;

    return cost;
  }

  def estimateRemainingCost(loc: Location, targetLoc: Location): Int = {
    if (loc.areaId != targetLoc.areaId)
      return areaTransitionCost;
    else
      return 2 * sqrt(pow(loc.coordinatesX - targetLoc.coordinatesX, 2) + pow(loc.coordinatesY - targetLoc.coordinatesY, 2)).toInt;
  }

  def buildPath(start: Location, destination: PathEvaluatedLocation, parentLocations: scala.collection.mutable.Map[Int, PathEvaluatedLocation], transportationType: TransportationType): Path = {
    val p = new Path();
    var immediateDestination = destination;
    var parent = parentLocations.get(destination.loc.id).orNull;

    while (parent != null) {
      p.movementStack.push(new Movement(transportationType, parent.loc, immediateDestination.loc, immediateDestination.cost));

      println("I added a movement: " + parent.loc.id + "->" + immediateDestination.loc.id + "@" + immediateDestination.cost);

      immediateDestination = parent;
      parent = parentLocations.get(parent.loc.id).orNull;
    }

    return p;
  }
}