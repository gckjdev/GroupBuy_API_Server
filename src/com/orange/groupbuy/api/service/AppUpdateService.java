package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.App;
import com.orange.groupbuy.manager.AppManager;

public class AppUpdateService extends CommonGroupBuyService {

	String appId;
	String uid;

	@Override
	public void handleData() {
		App app = AppManager.getApp(mongoClient, appId);
		if (app == null) {
			log
					.info("<AppUpdateService>: fail to find update information,appId="
							+ appId);
			return;
		}
		resultData = CommonServiceUtils.AppToJSON(app);
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "AppUpdateService [appId=" + appId + ", uid=" + uid + "]";
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		this.appId = request.getParameter(ServiceConstant.PARA_APPID);
		this.uid = request.getParameter(ServiceConstant.PARA_USERID);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}

		return true;
	}

}
