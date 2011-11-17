package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.App;
import com.orange.groupbuy.manager.AppManager;

public class UpdateKeywordService extends CommonGroupBuyService {

	String appId;
	String userId;
	int type = DBConstants.KEYWORD_TYPE_DEFAULT;
		
	@Override
	public String toString() {
		return "UpdateKeywordService [appId=" + appId + ", uid=" + userId + "]";
	}

	@Override
	public void handleData() {
		App app = AppManager.getApp(mongoClient, appId);
		if (app == null) {
			log.info("<AppUpdateService>: fail to find update information,appId="
							+ appId);
			return;
		} 
		resultData = CommonServiceUtils.appKeywordToJSON(app, type);

	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		this.appId = request.getParameter(ServiceConstant.PARA_APPID);
		this.userId = request.getParameter(ServiceConstant.PARA_USERID);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}
		
		String typeStr = request.getParameter(ServiceConstant.PARA_TYPE);
		if (!StringUtil.isEmpty(typeStr)){
			type = Integer.parseInt(typeStr);
		}

		return true;
	}

}
