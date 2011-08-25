package com.orange.groupbuy.api.service;

import com.orange.common.api.service.CommonService;
import com.orange.common.api.service.CommonServiceFactory;
import com.orange.groupbuy.api.service.user.AddUserShoppingItemService;
import com.orange.groupbuy.api.service.user.DeleteUserShoppingItemService;
import com.orange.groupbuy.api.service.user.UpdateUserShoppingItemService;
import com.orange.groupbuy.constant.ServiceConstant;

public class GroupBuyServiceFactory extends CommonServiceFactory {

	@Override
	public CommonService createServiceObjectByMethod(String method) {
		if (method == null){
			return new VerifyUserService();
		}
		if (method.equalsIgnoreCase(ServiceConstant.METHOD_REGISTERDEVICE)){
			return new RegisterDeviceService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_DEVICELOGIN)){
			return new DeviceLoginService();
		}
		else if(method.equalsIgnoreCase(ServiceConstant.METHOD_UPDATE_SUBSCRIPTION)){
			return new UpdateSubscriptionService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTSWITHPRICE)) {
			return new FindAllProductsWithPrice();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTSWITHREBATE)) {
			return new FindAllProductsWithRebate();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTSWITHBOUGHT)) {
			return new FindAllProductsWithBought();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTSWITHLOCATION)) {
			return new FindAllProductsWithLocation();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_GETAPPUPDATE)) {
			return new AppUpdateService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTSGROUPBYCATEGORY)) {
			return new FindAllProductGroupByCategory();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_SEARCHPRODUCT)) {
			return new SearchService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_FINDPRODUCTS)) {
			return new FindProductService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_UPDATEKEYWORD)) {
			return new UpdateKeywordService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_REGISTERUSER)) {
			return new RegisterUserService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_LOGIN)) {
			return new LoginUserService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_UPDATEUSER)) {
			return new UpdateUserService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_ADDSHOPPINGITEM)) {
			return new AddUserShoppingItemService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_UPDATESHOPPINGITEM)) {
			return new UpdateUserShoppingItemService();
		}
		else if (method.equalsIgnoreCase(ServiceConstant.METHOD_DELETESHOPPINGITEM)) {
			return new DeleteUserShoppingItemService();
		}
		else
			return null;
	}

}
