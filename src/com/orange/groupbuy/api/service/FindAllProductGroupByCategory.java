package com.orange.groupbuy.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;

public class FindAllProductGroupByCategory extends CommonGroupBuyService {

	String appId;
	int topCount = 30;
	String city;
	boolean todayOnly;		

	@Override
	public String toString() {
		return "FindAllProductGroupByCategory [appId=" + appId + ", city="
				+ city + ", todayOnly=" + todayOnly + ", topCount=" + topCount
				+ "]";
	}

	@Override
	public void handleData() {
		
		JSONArray retJsonArray = new JSONArray();	

		List<String> categoryList = ProductManager.getAllCategories();
		List<String> categoryNameList = ProductManager.getAllCategoryNames();
		int i=0;
		for (String category : categoryList){
			
			// get category name
			String name = categoryNameList.get(i);
			
			List<Integer> list = new ArrayList<Integer>();
			list.add(Integer.valueOf(category));
			
			// get product list by category
//			List<Product> productList = ProductManager.getAllProductsByCategory(mongoClient, city, category, "0", topCount);
			List<Product> productList = ProductManager.getProducts(mongoClient, city, list, todayOnly, 
					false, 0.0, 0.0, 0.0, DBConstants.SORT_BY_START_DATE, 0, topCount);
			JSONArray json = CommonServiceUtils.productListToJSONArray(productList);

			// add into result
			if (name != null && json != null){
				JSONObject obj = new JSONObject();
				obj.put(ServiceConstant.PARA_CATEGORY_NAME, name);
				obj.put(ServiceConstant.PARA_CATEGORY_ID, category);
				obj.put(ServiceConstant.PARA_PRODUCT, json);
				
				retJsonArray.add(obj);
			}
			
			i++;
		}
		
		if (retJsonArray.size() > 0)
			resultData = retJsonArray;
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		String topCountStr = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		city = request.getParameter(ServiceConstant.PARA_CITY);

		String todayOnlyStr = request.getParameter(ServiceConstant.PARA_TODAY_ONLY);
		if (!StringUtil.isEmpty(todayOnlyStr)){
			todayOnly = (Integer.parseInt(todayOnlyStr) == 0 ? false : true);
		}		
		
		if (!StringUtil.isEmpty(topCountStr)){
			topCount = Integer.parseInt(topCountStr);
		}

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}
		return true;
	}

}
