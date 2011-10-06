package com.orange.groupbuy.api.service;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.solr.SolrClient;
import com.orange.groupbuy.constant.ServiceConstant;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import com.orange.common.utils.DateUtil;


public class DeleteSolrIndexService extends CommonGroupBuyService {

	String query;
	String startDateString;
	String endDateString;
	// 删除所有的索引请用：http://localhost:8000/api/i?m=deletesolr&q=*:*
	// http://localhost:8000/api/i?m=deletesolr&q=s_id:meituan AND city:北京&sd=20110824000000&ed=20110824000000
	
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		query = request.getParameter(ServiceConstant.PARA_QUERY);
		startDateString = request.getParameter(ServiceConstant.PAPA_STARTDATE);
		endDateString = request.getParameter(ServiceConstant.PAPA_ENDDATE);
		if(query == null || query.isEmpty())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeleteSolrIndexService [query=" + query + "]";
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
			if (startDateString != null && !startDateString.isEmpty()) {
				Date startDate = DateUtil.dateFromString(startDateString);
				Long startDateLong = startDate.getTime();
//				System.out.println("startDate:" + startDate.toString());
				server.deleteByQuery("s_date:[* TO " + startDateLong + "]");
			} 
			if (endDateString != null && !endDateString.isEmpty()) {
				Date endDate = DateUtil.dateFromString(endDateString);
				Long endDateLong = endDate.getTime();
//				System.out.println("endDate:" + endDate.toString());
				server.deleteByQuery("e_date:[* TO " + endDateLong + "]");
			}
			server.commit();
		} catch (Exception e) {
			e.printStackTrace(); //TODO
		}

	}

}
