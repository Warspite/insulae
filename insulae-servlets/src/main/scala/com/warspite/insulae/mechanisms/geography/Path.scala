package com.warspite.insulae.mechanisms.geography
import scala.collection.mutable.Stack

class Path() {
	val movementStack = new Stack[Movement];
	
	def cost(): Int = {
	  var cost = 0;
	  movementStack.map(m => cost += m.cost);
	  return cost;
	}
}