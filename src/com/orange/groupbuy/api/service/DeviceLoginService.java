package com.orange.groupbuy.api.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mongodb.DBObject;
import com.orange.common.api.service.CommonService;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.User;
import com.orange.groupbuy.manager.UserManager;

public class DeviceLoginService extends CommonGroupBuyService {

	String 	deviceId;
	String 	appId;
	boolean needReturnUser;
	
	@Override
	public String toString() {
		return "GroupBuyDeviceLoginService [appId=" + appId + ", deviceId="
				+ deviceId + ", needReturnUser=" + needReturnUser + "]";
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		deviceId = request.getParameter(ServiceConstant.PARA_DEVICEID);
		String needReturnUserStr = request.getParameter(ServiceConstant.PARA_NEED_RETURN_USER);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(deviceId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (needReturnUserStr == null){
			needReturnUser = false;
		}
		else{
			needReturnUser = ( Integer.parseInt(needReturnUserStr) == 1 );
		}
		
		return true;

	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleData() {		
		DBObject user = UserManager.findUserByDeviceId(mongoClient, deviceId);
		if (user == null){
			log.info("<DeviceLogin> deviceId("+deviceId+") is not bind to any user");
			resultCode = ErrorCode.ERROR_DEVICE_NOT_BIND;
			return;
		}
		
		resultCode = ErrorCode.ERROR_SUCCESS;
		resultData = CommonServiceUtils.userToJSON(user);
	}

}
