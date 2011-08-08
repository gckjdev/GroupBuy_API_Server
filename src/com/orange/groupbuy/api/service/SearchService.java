package com.orange.groupbuy.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;
import com.orange.groupbuy.manager.UserManager;
import com.orange.groupbuy.util.UrlUtil;

public class SearchService extends CommonGroupBuyService {

	String deviceId;
	String appId;									// app id, mandantory
	String city;									// optional, city name, mandantory
	boolean todayOnly = false;						// optional, only return today's product
	List<Integer> categoryList;						// optional, return specify category's product
	int startOffset = 0;							// optional
	int maxCount = 30;								// optional
	String keyword;									// mandantory
	
	double latitude;								// optional, only for record user search history
	double longitude;								// optional, only for record user search history
	boolean hasLocation = false;					// internal usage
		
	@Override
	public String toString() {
		return "SearchService [appId=" + appId + ", categoryList="
				+ categoryList + ", city=" + city + ", hasLocation="
				+ hasLocation + ", keyword=" + keyword + ", latitude="
				+ latitude + ", longitude=" + longitude + ", maxCount="
				+ maxCount + ", startOffset=" + startOffset + ", todayOnly="
				+ todayOnly + ", deviceId=" + deviceId + "]";
	}

	@Override
	public void handleData() {

		String[] keywords = keyword.trim().split(" ");
		if (deviceId != null){
			if (hasLocation)
				UserManager.addSearchKeyword(mongoClient, deviceId, keywords, longitude, latitude);
			else
				UserManager.addSearchKeyword(mongoClient, deviceId, keywords);
		}
		
		List<Product> productList = ProductManager.searchProduct(mongoClient, city, categoryList, 
				todayOnly, keywords, startOffset, maxCount);
		
		resultData = CommonServiceUtils.productListToJSONArray(productList);		
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		city = request.getParameter(ServiceConstant.PARA_CITY);
		keyword = request.getParameter(ServiceConstant.PARA_KEYWORD);
		deviceId = request.getParameter(ServiceConstant.PARA_DEVICEID);
		
		String latitudeStr = request.getParameter(ServiceConstant.PARA_LATITUDE);
		String longitudeStr = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		if (!StringUtil.isEmpty(latitudeStr) && !StringUtil.isEmpty(longitudeStr)){
			hasLocation = true;
			latitude = Double.parseDouble(latitudeStr);
			longitude = Double.parseDouble(longitudeStr);
		}
		
		String todayOnlyStr = request.getParameter(ServiceConstant.PARA_TODAY_ONLY);
		if (!StringUtil.isEmpty(todayOnlyStr)){
			todayOnly = (Integer.parseInt(todayOnlyStr) == 0 ? false : true);
		}		
		
		String categoryStr = request.getParameter(ServiceConstant.PARA_CATEGORIES);
		categoryList = UrlUtil.parserUrlIntArray(categoryStr);

		String startOffsetStr = request.getParameter(ServiceConstant.PRAR_START_OFFSET);
		if (!StringUtil.isEmpty(startOffsetStr)){
			startOffset = Integer.parseInt(startOffsetStr);
		}		

		String maxCountStr = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		if (!StringUtil.isEmpty(maxCountStr)){
			maxCount = Integer.parseInt(maxCountStr);
		}		

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}				
		
		return true;
	}

}
