package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.constant.ServiceConstant;

public class GetCommentsService extends CommonGroupBuyService {
	String appId;
	String productId;
	
	@Override
	public String toString() {
		return "GetCommentsService [appId=" + appId
				+ ", productId=" + productId + "]";
	}
	
	@Override
	public void handleData() {	
		
	}
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		productId = request.getParameter(ServiceConstant.PARA_ID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		return true;
	}
	
	@Override
	public boolean needSecurityCheck() {
		return false;
	}
}
