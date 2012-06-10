package com.warspite.insulae.jetty;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.insulae.account.servlets.AccountServlet;
import com.warspite.insulae.account.servlets.LoginServlet;
import com.warspite.insulae.account.database.DummyMySqlAccountDatabase;
import com.warspite.common.cli.CliListener;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.ResourceExtractor;


public class JettyRunner extends Thread implements CliListener {
	private final String API_PATH = "/api";
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ResourceExtractor resourceExtractor;
	private final SessionKeeper sessionKeeper;
	private Server server;
	private File warFile;
	private boolean online = false;
	private boolean halt = false;


	public JettyRunner(final int serverPort, final ResourceExtractor resourceExtractor, final SessionKeeper sessionKeeper) {
		this.resourceExtractor = resourceExtractor;
		this.sessionKeeper = sessionKeeper;
		
		try {
			warFile = prepareWar();
			server = createServer(serverPort, warFile);
		}
		catch (Throwable e) {
			logger.error("Failed to create server.", e);
			cleanUp(warFile);
		}
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public void setHalt(final boolean halt) {
		this.halt = halt;
	}
	
	@Override
	public void run() {
		try {
			startServer(server);
			
			synchronized(this) {
				while(!halt) {
					Thread.sleep(250);
				}
			}
		} 
		catch (Exception e) {
			logger.error("Failure while running Jetty server.", e);
		}
		finally {
			try {
				stopServer(server);
				cleanUp(warFile);
			} 
			catch (Exception e) {
				logger.error("Failed to stop Jetty server.", e);
			}
		}
	}
	
	private File prepareWar() throws IOException {
		logger.debug("Extracting war file.");
		final List<File> extractedFiles = resourceExtractor.extract("wars", ".war", true, true);
		
		if(extractedFiles.size() != 1) {
			throw new IOException("Expected one and only one war file, but found " + extractedFiles.size());
		}
		
		logger.debug("Extracted " + extractedFiles.get(0));

		return extractedFiles.get(0);
	}

	private Server createServer(final int port, final File warFile) {
		logger.debug("Creating Jetty server at port " + port + ", using WAR " + warFile);
		final WebAppContext webapp = new WebAppContext();
		
		webapp.setContextPath("/");
		webapp.setWar(warFile.getAbsolutePath());
		webapp.addServlet(new ServletHolder(new AccountServlet(new DummyMySqlAccountDatabase(), sessionKeeper)), API_PATH + "/account/Account");
		webapp.addServlet(new ServletHolder(new LoginServlet(sessionKeeper)), API_PATH + "/account/Login");
		
		final Server server = new Server(port);
		server.setHandler(webapp);

		logger.debug("Jetty server created.");
		return server;
	}

	private void startServer(Server server) throws Exception {
		logger.debug("Starting Jetty server.");
		server.start();
		online = true;
		halt = false;
		logger.debug("Jetty server started.");
	}

	private void stopServer(Server server) throws Exception {
		logger.debug("Stopping Jetty server.");
		server.stop();
		online = false;
		halt = false;
		logger.debug("Jetty server stopped.");
	}
	
	private void cleanUp(final File warFile) {
		logger.debug("Cleaning up.");
		
		if( warFile.exists() )
			warFile.delete();
	}
}
