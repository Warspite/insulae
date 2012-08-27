package com.warspite.insulae.mechanisms.geography
import scala.collection.mutable.Stack

class Path() {
  val movementStack = new Stack[Movement];

  def cost(): Int = {
    var cost = 0;
    movementStack.map(m => cost += m.cost);
    return cost;
  }

  override def toString: String = {
    var str = "Path of cost " + cost();
    for (m <- movementStack)
      str += "\n    " + m;

    return str;
  }
}