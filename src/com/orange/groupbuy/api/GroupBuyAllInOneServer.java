package com.orange.groupbuy.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import com.orange.common.api.CommonApiServer;
import com.orange.common.api.service.ServiceHandler;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.api.service.GroupBuyServiceFactory;
import com.orange.groupbuy.constant.DBConstants;

public class GroupBuyAllInOneServer extends CommonApiServer {

	public static final String VERSION_STRING = "Group Buy API Server Version 0.1";
	public static final String SPRING_CONTEXT_FILE = "classpath:/com/orange/groupbuy/api/applicationContext.xml";
	public static final String LOG4J_FLE = "classpath:/log4j.properties";
	// public static final String MONGO_SERVER = "localhost";
	// public static final String MONGO_DB_NAME = "groupbuy";
	// public static final String MONGO_USER = "";
	// public static final String MONGO_PASSWORD = "";
	public static final GroupBuyServiceFactory serviceFactory = new GroupBuyServiceFactory();

	private static final MongoDBClient mongoClient = new MongoDBClient(
			DBConstants.D_GROUPBUY);

	@Override
	public String getAppNameVersion() {
		return VERSION_STRING;
	}

	@Override
	public String getLog4jFile() {
		return LOG4J_FLE;
	}

	@Override
	public ServiceHandler getServiceHandler() {
		return ServiceHandler.getServiceHandler(mongoClient, serviceFactory);
	}

	@Override
	public String getSpringContextFile() {
		return SPRING_CONTEXT_FILE;
	}

	@Override
	public int getPort() {
		String port = System.getProperty("server.port");
		if (port != null && !port.isEmpty()) {
			return Integer.parseInt(port);
		}
		return 8000;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.orange.common.api.CommonApiServer#getHandler()
	 */
	@Override
	public Handler getHandler() {

		List<Handler> handlers = new ArrayList<Handler>();
		String webWar = System.getProperty("web.server.war");
		String webContext = System.getProperty("web.server.context");
		if (webContext != null && webWar != null) {
			WebAppContext webapp = new WebAppContext();
			webapp.setContextPath(webContext);
			webapp.setWar(webWar);
			handlers.add(webapp);
		}

		String locationContext = System.getProperty("location.server.context");
		String locationWar = System.getProperty("location.server.war");
		if (locationContext != null && locationWar != null) {
			WebAppContext locationApp = new WebAppContext();
			locationApp.setContextPath(locationContext);
			locationApp.setWar(locationWar);
			handlers.add(locationApp);
		}

		String uploadContext = System.getProperty("upload.server.context");
		String uploadResourceBase = System.getProperty("upload.resourcebase");

		if (uploadContext != null && uploadResourceBase != null) {
			ContextHandler uploadHandler = new ContextHandler(uploadContext);// upload
			ResourceHandler resource_handler = new ResourceHandler();
			resource_handler.setDirectoriesListed(true);
			resource_handler.setResourceBase(uploadResourceBase);// ./upload
			uploadHandler.setHandler(resource_handler);
			handlers.add(uploadHandler);
		}

		ContextHandler apiHandler = new ContextHandler("/api");
		apiHandler.setHandler(this);
		handlers.add(apiHandler);
		// contexts.addHandler(serviceHandler);
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlers.toArray(new Handler[handlers.size()]));
		return contexts;
	}

	public static void main(String[] args) throws Exception {
		GroupBuyAllInOneServer server = new GroupBuyAllInOneServer();
		server.startServer();
	}
}
