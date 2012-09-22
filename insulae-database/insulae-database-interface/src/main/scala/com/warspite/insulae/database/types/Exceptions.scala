package com.warspite.insulae.database.types

class UnrecognizedAgentTypeException(agent: Object) extends RuntimeException ("Received an unrecognized type of agent: " + agent.getClass()) {}
