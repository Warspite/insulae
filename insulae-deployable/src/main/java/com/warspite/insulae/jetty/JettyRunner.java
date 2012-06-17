package com.warspite.insulae.jetty;
import java.io.File;
import java.io.IOException;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.CliListener;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.account.database.DummyMySqlAccountDatabase;
import com.warspite.insulae.account.servlets.AccountServlet;
import com.warspite.insulae.account.servlets.LoginServlet;


public class JettyRunner extends Thread implements CliListener {
	private final String API_PATH = "/api";
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SessionKeeper sessionKeeper;
	private Server server;
	private File warFile;
	private boolean online = false;
	private boolean halt = false;


	public JettyRunner(final int serverPort, final SessionKeeper sessionKeeper) {
		this.sessionKeeper = sessionKeeper;
		
		try {
			warFile = findWar();
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
		sessionKeeper.start();
		online = true;
		halt = false;
		logger.debug("Jetty server started.");
	}

	private void stopServer(Server server) throws Exception {
		logger.debug("Stopping Jetty server.");
		server.stop();
		sessionKeeper.stop();
		online = false;
		halt = false;
		logger.debug("Jetty server stopped.");
	}
	
	private void cleanUp(final File warFile) {
		logger.debug("Cleaning up.");
		
		if( warFile.exists() )
			warFile.delete();
	}
	
	private File findWar() throws IOException {
		final File warDir = new File("wars");
		
		if( !warDir.exists() || !warDir.isDirectory() )
			throw new IOException("Failed to locate required directory " + warDir.getPath() + ". Please ensure that your installation is not corrupted.");
		
		final File[] warFiles = warDir.listFiles();
		
		if(warFiles.length != 1)
			throw new IOException("Failed to locate war file in " + warDir.getPath() + ". Expected 1, but found " + warFiles.length);
		
		return warFiles[0];
	}
}
