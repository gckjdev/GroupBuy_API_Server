package com.orange.groupbuy.api.service;


import javax.servlet.http.HttpServletRequest;
import com.orange.groupbuy.constant.ServiceConstant;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;


public class DeleteSolrIndexService extends CommonGroupBuyService {

	private String query;
	private CommonsHttpSolrServer server;
	// some delete query, delete完成要commit才能生效
	// http://localhost:8099/solr/update?stream.body=<delete><query>s_id:meituan</query></delete>
	// http://localhost:8099/solr/update?stream.body=<delete><query>city:北京</query></delete>
	// http://localhost:8099/solr/update?stream.body=<delete><query>*:*</query></delete>
	// http://localhost:8099/solr/update?stream.body=<commit/>
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		query = request.getParameter(ServiceConstant.PARA_QUERY);
		if(query == null || query.isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleData() {
		try {
			String address = System.getProperty("solr.address");
			String portStr = System.getProperty("solr.port");
			int port = 8099;
			
			if (address == null){
				address = "localhost";
			}
			if (portStr != null){
				port = Integer.parseInt(portStr);
			}
			
			String solrURL = "http://".concat(address).concat(":").concat(String.valueOf(port)).concat("/solr");			
			server = new CommonsHttpSolrServer(solrURL);
			
			server.deleteByQuery(query);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace(); //TODO
		}

	}

}
