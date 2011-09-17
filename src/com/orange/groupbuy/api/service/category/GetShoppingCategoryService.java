package com.orange.groupbuy.api.service.category;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.ShoppingCategory;
import com.orange.groupbuy.manager.CategoryManager;

public class GetShoppingCategoryService extends CommonGroupBuyService {
	
	String appId;	

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;	
	
		return true;
		// TODO Auto-generated method stub
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public String toString() {
		return "GetShoppingCategoryService [appId=" + appId + "]";
	}

	@Override
	public void handleData() {
		
		List<ShoppingCategory> shoppingCategoryList = CategoryManager.findShoppingCategory(mongoClient);
		if (shoppingCategoryList != null && shoppingCategoryList.size() > 0){			
			JSONArray jsonArray = CommonServiceUtils.shoppingcategoryListToJSONArray(shoppingCategoryList);
			resultData = jsonArray;
		}
		// TODO Auto-generated method stub
		

	}

}
