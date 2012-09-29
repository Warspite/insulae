package com.warspite.insulae.servlets.world

import com.warspite.common.servlets.ClientReadableException
import com.warspite.insulae.database.world.Avatar
import com.warspite.insulae.database.world.Race

class MultipleStartingHubsException(raceId: Int) extends RuntimeException("Failed to create starting buildings for race " + raceId + ", because its starting building setup contains multiple hubs.") {}
class NoStartingHubsException(raceId: Int) extends RuntimeException("Failed to create starting buildings for race " + raceId + ", because its starting building setup contains no hubs.") {}
class NoStartingLocationFoundException(avatar: Avatar, race: Race) extends RuntimeException("Failed to create starting buildings for " + avatar + ", because no suitable starting location for " + race + " could be found.") {}