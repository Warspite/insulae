package com.warspite.insulae.jetty;
import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.common.cli.CliListener;
import com.warspite.common.servlets.sessions.SessionKeeper;
import com.warspite.insulae.mechanisms.*;
import com.warspite.insulae.mechanisms.geography.*;
import com.warspite.insulae.mechanisms.industry.*;
import com.warspite.insulae.servlets.world.*;
import com.warspite.insulae.servlets.account.*;
import com.warspite.insulae.servlets.geography.*;
import com.warspite.insulae.servlets.industry.*;
import com.warspite.insulae.servlets.meta.*;
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
	
	private ItemTransactor itemTransactor;
	private ItemHoarder itemHoarder;
	private ActionPerformer actionPerformer; 
	
	private final Ticker ticker;

	public JettyRunner(final int serverPort, final SessionKeeper sessionKeeper, final InsulaeDatabase db, final Ticker ticker, final File warFile) {
		this.serverPort = serverPort;
		this.sessionKeeper = sessionKeeper;
		this.db = db;
		this.ticker = ticker;
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
		
		itemTransactor = new ItemTransactor(db);
		itemTransactor.start();
		
		itemHoarder = new ItemHoarder(db, itemTransactor);
		
		final Authorizer authorizer = new Authorizer(db);
		final PathFinder pathFinder = new PathFinder(db, PathFinder.AREA_TRANSITION_COST());
		final Surveyor surveyor = new Surveyor(db);
		final ActionVerifier actionVerifier = new ActionVerifier(db, surveyor);
		final CustomActionEffector customActionEffector = new CustomActionEffector(db, pathFinder, actionVerifier);
		
		actionPerformer = new ActionPerformer(db, itemTransactor, actionVerifier, pathFinder, customActionEffector);

		webapp.setContextPath("/");
		webapp.setWar(warFile.getAbsolutePath());
		webapp.addServlet(new ServletHolder(new AccountServlet(db, sessionKeeper)), API_PATH + "/account/Account");
		webapp.addServlet(new ServletHolder(new SessionServlet(db, sessionKeeper)), API_PATH + "/account/Session");
		webapp.addServlet(new ServletHolder(new RealmServlet(db, sessionKeeper)), API_PATH + "/world/Realm");
		webapp.addServlet(new ServletHolder(new RaceServlet(db, sessionKeeper)), API_PATH + "/world/Race");
		webapp.addServlet(new ServletHolder(new SexServlet(db, sessionKeeper)), API_PATH + "/world/Sex");
		webapp.addServlet(new ServletHolder(new AvatarServlet(db, sessionKeeper)), API_PATH + "/world/Avatar");
		webapp.addServlet(new ServletHolder(new AreaServlet(db, sessionKeeper)), API_PATH + "/geography/Area");
		webapp.addServlet(new ServletHolder(new LocationServlet(db, sessionKeeper)), API_PATH + "/geography/Location");
		webapp.addServlet(new ServletHolder(new LocationTypeServlet(db, sessionKeeper)), API_PATH + "/geography/LocationType");
		webapp.addServlet(new ServletHolder(new ResourceServlet(db, sessionKeeper)), API_PATH + "/geography/Resource");
		webapp.addServlet(new ServletHolder(new ResourceTypeServlet(db, sessionKeeper)), API_PATH + "/geography/ResourceType");
		webapp.addServlet(new ServletHolder(new TransportationTypeServlet(db, sessionKeeper)), API_PATH + "/geography/TransportationType");
		webapp.addServlet(new ServletHolder(new TransportationCostServlet(db, sessionKeeper)), API_PATH + "/geography/TransportationCost");
		webapp.addServlet(new ServletHolder(new LocationNeighborServlet(db, sessionKeeper)), API_PATH + "/geography/LocationNeighbor");
		webapp.addServlet(new ServletHolder(new BuildingTypeServlet(db, sessionKeeper)), API_PATH + "/industry/BuildingType");
		webapp.addServlet(new ServletHolder(new BuildingServlet(db, sessionKeeper, pathFinder, authorizer)), API_PATH + "/industry/Building");
		webapp.addServlet(new ServletHolder(new BuildingActionAutomationServlet(db, sessionKeeper, authorizer)), API_PATH + "/industry/BuildingActionAutomation");
		webapp.addServlet(new ServletHolder(new ItemTypeServlet(db, sessionKeeper)), API_PATH + "/industry/ItemType");
		webapp.addServlet(new ServletHolder(new ItemStorageServlet(db, sessionKeeper)), API_PATH + "/industry/ItemStorage");
		webapp.addServlet(new ServletHolder(new ItemHoardingOrderServlet(db, sessionKeeper, authorizer)), API_PATH + "/industry/ItemHoardingOrder");
		webapp.addServlet(new ServletHolder(new ActionServlet(db, sessionKeeper, actionPerformer, authorizer)), API_PATH + "/industry/Action");
		webapp.addServlet(new ServletHolder(new ActionItemCostServlet(db, sessionKeeper)), API_PATH + "/industry/ActionItemCost");
		webapp.addServlet(new ServletHolder(new ActionItemOutputServlet(db, sessionKeeper)), API_PATH + "/industry/ActionItemOutput");
		webapp.addServlet(new ServletHolder(new LocationTypeRequiredNearActionTargetLocationServlet(db, sessionKeeper)), API_PATH + "/industry/LocationTypeRequiredNearActionTargetLocation");
		webapp.addServlet(new ServletHolder(new ResourceRequiredNearActionTargetLocationServlet(db, sessionKeeper)), API_PATH + "/industry/ResourceRequiredNearActionTargetLocation");
		webapp.addServlet(new ServletHolder(new TroubleReportServlet(db, sessionKeeper)), API_PATH + "/meta/TroubleReport");
		webapp.addServlet(new ServletHolder(new TroubleReportTypeServlet(db, sessionKeeper)), API_PATH + "/meta/TroubleReportType");

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

		logger.debug("Starting Ticker.");
		ticker.injectHelpers(db, itemHoarder, actionPerformer);
		ticker.setup();
		ticker.start();

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
		
		if(itemTransactor != null)
			itemTransactor.halt();
		
		
		ticker.halt();
		server.stop();
		sessionKeeper.stop();
		online = false;
		halt = false;
		logger.debug("Jetty server stopped.");
	}
}
