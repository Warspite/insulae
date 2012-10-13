package com.warspite.insulae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.Cli;
import com.warspite.common.database.DatabaseCreator;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.accessors.GeoAccessor;
import com.warspite.insulae.database.InsulaeDatabase;
import com.warspite.insulae.jetty.JettyContainer;
import com.warspite.insulae.mechanisms.Ticker;


public class EntryPoint {
	private final static Logger logger = LoggerFactory.getLogger(EntryPoint.class);
	private final static SessionKeeper sessionKeeper = new SessionKeeper();
	private final static String DEFAULT_INSTANCE_NAME = "default";

	public static void main(String[] args) throws Exception
	{
		final String instanceName = getInstanceName(args);
		final Cli cli = new Cli("Insulae", instanceName);
		final DatabaseCreator<InsulaeDatabase> dbCreator = new DatabaseCreator<InsulaeDatabase>();
		final Ticker ticker = new Ticker(60);
		final GeoAccessor geoAccessor = new GeoAccessor();
		
		try {
			logger.info("Extracting resources.");

			logger.info("Creating Jetty container.");
			final JettyContainer jettyContainer = new JettyContainer(sessionKeeper, dbCreator, ticker, geoAccessor);
			
			logger.info("Registering CLI listeners.");
			cli.registerListeners("jetty", jettyContainer);
			cli.registerListeners("sessions", sessionKeeper);
			cli.registerListeners("db", dbCreator);
			cli.registerListeners("ticker", ticker);
			cli.registerListeners("geo", geoAccessor);
			
			logger.info("Starting CLI.");
			cli.start();

			jettyContainer.stop();
		}
		catch (Throwable e) {
			logger.error("Caught exception.", e);
		}

		cli.println("Goodbye!");
	}

	private static String getInstanceName(String[] args) {
		if(args.length == 0 || args[0].isEmpty()) {
			logger.info("Missing instance name (assumed to be first parameter), defaulting to '" + DEFAULT_INSTANCE_NAME + "'.");
			return DEFAULT_INSTANCE_NAME;
		}
		
		if(args[0].contains(" ") || args[0].contains("?") || args[0].contains("/") || args[0].contains("\\"))
			throw new IllegalArgumentException("Received instance name '" + args[0] + "', which contains illegal character.");
		
		return args[0];
	}
}
