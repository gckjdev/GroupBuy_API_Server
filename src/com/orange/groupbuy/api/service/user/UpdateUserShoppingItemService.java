package com.orange.groupbuy.api.service.user;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.UserManager;

public class UpdateUserShoppingItemService extends CommonGroupBuyService {

	String userId;
	String appId;
	String itemId;
	String categoryName;
	String subCategoryName;
	String keywords;
	double maxPrice = -1.0f;
	double minRebate = -1.0f;		
	
	@Override
	public void handleData() {
		if (!UserManager.updateUserShoppingItem(mongoClient, userId, itemId, categoryName, 
				subCategoryName, keywords, maxPrice, minRebate)){
			resultCode = ErrorCode.ERROR_UPDATE_SHOPPING_ITEM;
			return;
		}
		
		return;		
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		itemId = request.getParameter(ServiceConstant.PARA_ITEMID);
		categoryName = request.getParameter(ServiceConstant.PARA_CATEGORY_NAME);
		subCategoryName = request.getParameter(ServiceConstant.PARA_SUB_CATEGORY_NAME); 
		keywords = request.getParameter(ServiceConstant.PARA_KEYWORD);
		
		String priceStr = request.getParameter(ServiceConstant.PARA_PRICE);
		String rebateStr = request.getParameter(ServiceConstant.PARA_REBATE);
		
		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		
		if (!check(itemId, ErrorCode.ERROR_PARAMETER_ITEMID_EMPTY,
				ErrorCode.ERROR_PARAMETER_ITEMID_NULL))
			return false;

		if (!StringUtil.isEmpty(priceStr)){
			maxPrice = Double.parseDouble(priceStr);
		}
		
		if (!StringUtil.isEmpty(rebateStr)){
			minRebate = Double.parseDouble(rebateStr);
		}
		
		return true;				
	}

}
