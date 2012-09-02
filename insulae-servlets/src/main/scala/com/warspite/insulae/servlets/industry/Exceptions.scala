package com.warspite.insulae.servlets.industry

import com.warspite.common.servlets.ClientReadableException

class MissingActionAgentParameterException() extends ClientReadableException ("Request to perform action was missing appropriate agent parameter.", "Your request to perform an action didn't contain any agent parameter. Please use 'agentBuildingId'.") {}
