package com.orange.groupbuy.api.service.category;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Category;
import com.orange.groupbuy.manager.CategoryManager;

public class GetAllCategoryService extends CommonGroupBuyService {

	String appId;
	
	// example request
	// http://localhost:8000/api/i?m=gac&app=GROUPBUY
	
	
	
	@Override
	public void handleData() {
		List<Category> categoryList = CategoryManager.findAllCategory(mongoClient);
		if (categoryList != null && categoryList.size() > 0){			
			JSONArray jsonArray = CommonServiceUtils.categoryListToJSONArray(categoryList);
			resultData = jsonArray;
		}
	}

	@Override
	public String toString() {
		return "GetAllCategoryService [appId=" + appId + "]";
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
	
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;	
	
		return true;
	}

}
