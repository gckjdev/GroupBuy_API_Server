package com.orange.groupbuy.api;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
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
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/groupbuy");
		webapp.setWar("GroupBuy_Web_UI-1.0-SNAPSHOT.war");

		ContextHandler serviceHandler = new ContextHandler("/api");
		serviceHandler.setHandler(this);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { serviceHandler, webapp });
		return contexts;
	}

	public static void main(String[] args) throws Exception {
		GroupBuyAllInOneServer server = new GroupBuyAllInOneServer();
		server.startServer();
	}
}
