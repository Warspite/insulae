package com.warspite.insulae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.Cli;
import com.warspite.insulae.jetty.JettyContainer;


public class EntryPoint {
	private final static Logger logger = LoggerFactory.getLogger(EntryPoint.class);
	private final static Cli cli = new Cli("Insulae");
	private final static ResourceExtractor resourceExtractor = new ResourceExtractor();

	public static void main(String[] args) throws Exception
	{
		try {
			logger.info("Extracting resources.");
			resourceExtractor.extract("cli", ".cli", false, true);
			
			logger.info("Creating Jetty container.");
			final JettyContainer jettyContainer = new JettyContainer(resourceExtractor);
			
			logger.info("Registering CLI listeners.");
			cli.registerListeners("jetty", jettyContainer);
			
			logger.info("Starting CLI.");
			cli.start(true);
			
			jettyContainer.stop();
		}
		catch (Throwable e) {
			logger.error("Caught exception.", e);
		}

		cli.println("Goodbye!");
	}
}
