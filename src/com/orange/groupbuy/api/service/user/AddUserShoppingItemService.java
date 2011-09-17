package com.orange.groupbuy.api.service.user;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.App;
import com.orange.groupbuy.manager.AppManager;
import com.orange.groupbuy.manager.UserManager;

public class AddUserShoppingItemService extends CommonGroupBuyService {

	String userId;
	String appId;
	String itemId;
	String categoryName;
	String subCategoryName;
	String keywords;
	String city;
	double maxPrice = -1.0f;
	double minRebate = -1.0f;
	
	Date expireDate;
	
	@Override
	public String toString() {
		return "AddUserShoppingItemService [appId=" + appId + ", categoryName="
				+ categoryName + ", itemId=" + itemId + ", keywords="
				+ keywords + ", maxPrice=" + maxPrice + ", minRebate="
				+ minRebate + ", subCategoryName=" + subCategoryName
				+ ", userId=" + userId + "]";
	}

	@Override
	public void handleData() {
		
//		User user = UserManager.findUserByUserId(mongoClient, userId);
//		if (user == null){
//			resultCode = ErrorCode.ERROR_USERID_NOT_FOUND;
//			return;
//		}				
		
		App app = AppManager.getApp(mongoClient, appId);
		if (app == null){
			resultCode = ErrorCode.ERROR_APP_NOT_FOUND;
			return;
		}			
		
		if (!UserManager.addUserShoppingItem(mongoClient, userId, itemId, appId, categoryName, 
				subCategoryName, keywords, city, maxPrice, minRebate, expireDate)){
			resultCode = ErrorCode.ERROR_ADD_SHOPPING_ITEM;
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
		categoryName = request.getParameter(ServiceConstant.PARA_CATEGORY_NAME);
		subCategoryName = request.getParameter(ServiceConstant.PARA_SUB_CATEGORY_NAME); 
		keywords = request.getParameter(ServiceConstant.PARA_KEYWORD);
	    city = request.getParameter(ServiceConstant.PARA_CITY);
	    expireDate = DateUtil.dateFromString(request.getParameter(ServiceConstant.PARA_EXPIRE_DATE));
		
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
