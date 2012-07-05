package com.warspite.insulae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.Cli;
import com.warspite.common.database.DatabaseCreator;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.database.InsulaeDatabase;
import com.warspite.insulae.jetty.JettyContainer;


public class EntryPoint {
	private final static Logger logger = LoggerFactory.getLogger(EntryPoint.class);
	private final static Cli cli = new Cli("Insulae");
	private final static SessionKeeper sessionKeeper = new SessionKeeper();

	public static void main(String[] args) throws Exception
	{
		try {
			logger.info("Extracting resources.");

			logger.info("Creating Jetty container.");
			final JettyContainer jettyContainer = new JettyContainer(sessionKeeper, instantiateDatabase());

			logger.info("Registering CLI listeners.");
			cli.registerListeners("jetty", jettyContainer);
			cli.registerListeners("sessions", sessionKeeper);

			logger.info("Starting CLI.");
			cli.start(true);

			jettyContainer.stop();
		}
		catch (Throwable e) {
			logger.error("Caught exception.", e);
		}

		cli.println("Goodbye!");
	}

	private static InsulaeDatabase instantiateDatabase() {
		final DatabaseCreator<InsulaeDatabase> creator = new DatabaseCreator<InsulaeDatabase>();
		InsulaeDatabase db = creator.create();
		db.connect();
		return db;
	}
}
