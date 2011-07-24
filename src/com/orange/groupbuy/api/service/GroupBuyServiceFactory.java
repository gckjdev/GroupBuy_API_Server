package com.orange.groupbuy.api.service;

import com.orange.common.api.service.CommonService;
import com.orange.common.api.service.CommonServiceFactory;
import com.orange.groupbuy.constant.ServiceConstant;

public class GroupBuyServiceFactory extends CommonServiceFactory {

	@Override
	public CommonService createServiceObjectByMethod(String method) {
		if (method == null){
			return null;
		}
		if (method.equalsIgnoreCase(ServiceConstant.METHOD_REGISTERDEVICE))
			return new RegisterDeviceService();
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_DEVICELOGIN))
			return new DeviceLoginService();
		else if(method.equalsIgnoreCase(ServiceConstant.METHOD_UPDATE_SUBSCRIPTION))
			return new UpdateSubscriptionService();
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTWITHPRICE)) {
			return new FindAllProductsWithPrice();
		}else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTWITHREBATE)) {
			return new FindAllProductsWithRebate();
		}else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTWITHBOUGHT)) {
			return new FindAllProductsWithBought();
		}else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTWITHLOCATION)) {
			return new FindAllProductsWithLocation();
		}
		else
			return null;
	}

}
