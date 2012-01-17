package com.orange.groupbuy.api.service.spam;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.SpamManager;

public class ReportSpamMessage extends CommonGroupBuyService {

	String deviceId;
	String mobile;
	String type;
	
	@Override
	public void handleData() {
		SpamManager.reportSpam(mongoClient, mobile, deviceId, type);
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		deviceId = request.getParameter("device");
		mobile = request.getParameter("mobile");
		type = request.getParameter("type");		
		return true;
	}

}
