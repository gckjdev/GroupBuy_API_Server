package com.orange.groupbuy.api.service;


import javax.servlet.http.HttpServletRequest;

import com.orange.common.solr.SolrClient;
import com.orange.groupbuy.constant.ServiceConstant;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;


public class DeleteSolrIndexService extends CommonGroupBuyService {

	String query;
	
	// http://localhost:8000/api/i?m=delete&q=s_id:meituan AND city:北京
	
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
			CommonsHttpSolrServer server = SolrClient.getSolrServer();						
			server.deleteByQuery(query);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace(); //TODO
		}

	}

}
