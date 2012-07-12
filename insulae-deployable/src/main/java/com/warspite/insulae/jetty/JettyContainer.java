package com.warspite.insulae.jetty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.CliListener;
import com.warspite.common.cli.annotations.Cmd;
import com.warspite.common.database.DatabaseCreator;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.database.InsulaeDatabase;

public class JettyContainer implements CliListener {
	public final static int DEFAULT_PORT = 80;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private JettyRunner jettyRunner = null;

	private final SessionKeeper sessionKeeper;
	private final DatabaseCreator<InsulaeDatabase> dbCreator;

	public JettyContainer(final SessionKeeper sessionKeeper, final DatabaseCreator<InsulaeDatabase> dbCreator) {
		this.sessionKeeper = sessionKeeper;
		this.dbCreator = dbCreator;
	}

	@Cmd(name="start",description="Start Jetty server, listening on <port>, using warfile with substring <warSubstring>.", printReturnValue = true)
	public String start(Integer port, String warSubstring) throws InterruptedException {
		try {
			final File warFile = findWar(warSubstring);

			synchronized(this) {
				if(jettyRunner != null)
					return "Jetty is already running. Stop it first to start a new instance.";

				jettyRunner = new JettyRunner(port, sessionKeeper, dbCreator.getDatabase(), warFile);
				jettyRunner.start();

				while(!jettyRunner.isOnline()) {
					logger.debug("Waiting for Jetty server to come online...");
					Thread.sleep(250);

					if(jettyRunner.hasAborted()) {
						logger.info("The Jetty server has aborted, giving up waiting.");
						return "The Jetty server has aborted, giving up waiting.";
					}
				}
				logger.info("Jetty server is online.");
			}
			
			return "Jetty started at port " + port + " with war " + warFile + ".";
		}
		catch(IOException e) {
			logger.error("Failed to load war file from substring '" + warSubstring + "'.", e);
			return "Failed to load war file from substring '" + warSubstring + "'.";
		}
	}

	@Cmd(name="stop",description="Stop Jetty server", printReturnValue = true)
	public String stop() throws InterruptedException {
		synchronized(this) {
			if(jettyRunner == null)
				return "There is no Jetty server to stop.";

			jettyRunner.setHalt(true);

			while(jettyRunner.isOnline()) {
				logger.info("Waiting for server shutdown...");
				Thread.sleep(250);
			}

			logger.info("Server is offline, exiting.");

			jettyRunner = null;
		}

		return "Jetty stopped.";
	}

	private File findWar(final String warSubstring) throws IOException {
		final File warDir = new File("wars");

		if( !warDir.exists() || !warDir.isDirectory() )
			throw new IOException("Failed to locate required directory " + warDir.getPath() + ". Please ensure that your installation is not corrupted.");

		final File[] warFiles = warDir.listFiles();
		final List<File> filteredWarFiles = new ArrayList<File>();

		for(File f : warFiles) {
			if(warSubstring == null || f.getName().contains(warSubstring))
				filteredWarFiles.add(f);
		}

		if(filteredWarFiles.size() != 1) {
			String filterMsg = (warSubstring == null ? "No substring filter was applied." : "War filename substring filter used: '" + warSubstring + "'.");
			throw new IOException("Failed to locate one (and only one) war file in " + warDir.getPath() + ". " + filterMsg + " Expected 1, but found " + filteredWarFiles.size());
		}

		return filteredWarFiles.get(0);
	}
}
