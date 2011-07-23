package com.orange.groupbuy.api.service;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Gps;
import com.orange.groupbuy.manager.SubscriptionManager;
import com.orange.groupbuy.util.UrlUtil;

public class UpdateSubscriptionService extends CommonGroupBuyService {

	String appId;
	String userId;
	String gps;
	String categories;
	String keywords;
	
	@Override
	public String toString() {
		return "UpdateSubscriptionService [appId=" + appId + ", categories="
				+ categories + ", gps=" + gps + ", keywords=" + keywords
				+ ", userId=" + userId + "]";
	}

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		List<String> categoryList = UrlUtil.parserUrlArray(categories);
		List<String> keywordList = UrlUtil.parserUrlArray(keywords);
		List<Gps> gpsList = UrlUtil.parseGpsArray(gps); 
		SubscriptionManager.updateSubscribe(userId, appId, gpsList, keywordList, categoryList);
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		gps = request.getParameter(ServiceConstant.PARA_GPS);
		keywords = request.getParameter(ServiceConstant.PARA_KEYWORDS);
		categories = request.getParameter(ServiceConstant.PARA_CATEGORIES); 
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		
		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		return true;
	}

}
