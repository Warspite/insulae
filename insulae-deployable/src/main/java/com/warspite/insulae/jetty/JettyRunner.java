package com.warspite.insulae.jetty;
import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.CliListener;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.servlets.realm.RealmServlet;
import com.warspite.insulae.servlets.account.AccountServlet;
import com.warspite.insulae.servlets.account.LoginServlet;
import com.warspite.insulae.database.InsulaeDatabase;


public class JettyRunner extends Thread implements CliListener {
	private final String API_PATH = "/api";

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SessionKeeper sessionKeeper;
	private Server server;
	private boolean online = false;
	private boolean halt = false;
	private boolean aborted = false;

	private final int serverPort;

	private final InsulaeDatabase db;
	private final File warFile;


	public JettyRunner(final int serverPort, final SessionKeeper sessionKeeper, final InsulaeDatabase db, final File warFile) {
		this.serverPort = serverPort;
		this.sessionKeeper = sessionKeeper;
		this.db = db;
		this.warFile = warFile;
	}

	public boolean isOnline() {
		return online;
	}

	public void setHalt(final boolean halt) {
		this.halt = halt;
	}

	public boolean hasAborted() {
		return aborted;
	}
	
	@Override
	public void run() {
		try {
			startServer();

			synchronized(this) {
				while(!halt) {
					Thread.sleep(250);
				}
			}
		} 
		catch (Exception e) {
			logger.error("Failure while running Jetty server.", e);
			aborted = true;
		}
		finally {
			try {
				stopServer();
			} 
			catch (Exception e) {
				logger.error("Failed to stop Jetty server.", e);
			}
		}
	}

	private Server createServer() {
		logger.info("Jetty launching at port " + serverPort + ", WAR " + warFile);
		final WebAppContext webapp = new WebAppContext();

		webapp.setContextPath("/");
		webapp.setWar(warFile.getAbsolutePath());
		webapp.addServlet(new ServletHolder(new AccountServlet(db, sessionKeeper)), API_PATH + "/account/Account");
		webapp.addServlet(new ServletHolder(new LoginServlet(db, sessionKeeper)), API_PATH + "/account/Login");
		webapp.addServlet(new ServletHolder(new RealmServlet(db, sessionKeeper)), API_PATH + "/realm/Realm");

		final Server server = new Server(serverPort);
		server.setHandler(webapp);

		logger.debug("Jetty server created.");
		return server;
	}

	private void startServer() throws Exception {
		if( server == null ) {
			try {
				logger.debug("Creating Jetty server.");
				server = createServer();
				logger.debug("Jetty server created.");
			}
			catch(Throwable e) {
				logger.error("Failed to create Jetty server.", e);
				aborted = true;
				return;
			}
		}


		logger.debug("Starting Jetty server.");
		server.start();
		sessionKeeper.start();
		online = true;
		halt = false;
		logger.debug("Jetty server started.");
	}

	private void stopServer() throws Exception {
		if(server == null) {
			logger.debug("Tried to stop Jetty server, but there is no server to stop.");
			return;
		}

		logger.debug("Stopping Jetty server.");
		server.stop();
		sessionKeeper.stop();
		online = false;
		halt = false;
		logger.debug("Jetty server stopped.");
	}
}
