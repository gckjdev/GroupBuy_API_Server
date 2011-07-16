package com.orange.groupbuy.api.service;

import com.orange.common.api.service.CommonService;
import com.orange.common.api.service.CommonServiceFactory;
import com.orange.groupbuy.constant.ServiceConstant;

public class GroupBuyServiceFactory extends CommonServiceFactory {

	@Override
	public CommonService createServiceObjectByMethod(String method) {
		if (method.equalsIgnoreCase(ServiceConstant.METHOD_REGISTERDEVICE))
			return new RegisterDeviceService();
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_DEVICELOGIN))
			return new GroupBuyDeviceLoginService();
		else
			return null;
	}

}
