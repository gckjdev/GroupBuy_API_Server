package com.orange.groupbuy.api.service.user;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.UserManager;

public class DeleteUserShoppingItemService extends CommonGroupBuyService {

	String userId;
	String appId;
	String itemId;

	
	@Override
	public void handleData() {
		if (!UserManager.deleteUserShoppingItem(mongoClient, userId, itemId)){
			resultCode = ErrorCode.ERROR_DELETE_SHOPPING_ITEM;
			return;
		}
		
		return;		
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		itemId = request.getParameter(ServiceConstant.PARA_ITEMID);

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		
		if (!check(itemId, ErrorCode.ERROR_PARAMETER_ITEMID_EMPTY,
				ErrorCode.ERROR_PARAMETER_ITEMID_NULL))
			return false;
		
		return true;
	}

}
