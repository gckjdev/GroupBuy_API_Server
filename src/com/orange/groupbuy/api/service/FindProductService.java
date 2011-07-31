package com.orange.groupbuy.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;
import com.orange.groupbuy.util.UrlUtil;

public class FindProductService extends CommonGroupBuyService {

	String appId;									// app id, mandantory
	String city;									// optional, city name, mandantory
	double latitude;								// optional
	double longitude;								// optional
	double maxDistance = 30;						// optional, radius of location, unit: km
	boolean todayOnly = false;						// optional, only return today's product
	List<Integer> categoryList;						// optional, return specify category's product
	int sortBy = DBConstants.SORT_BY_START_DATE;	// mandantory, sort type for product
	int startOffset = 0;							// optional
	int maxCount = 30;								// optional

	boolean gpsQuery = false;						// internal usage
	
	
	
	@Override
	public String toString() {
		return "FindProductService [appId=" + appId + ", categoryList="
				+ categoryList + ", city=" + city + ", gpsQuery=" + gpsQuery
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", maxCount=" + maxCount + ", maxDistance=" + maxDistance
				+ ", sortBy=" + sortBy + ", startOffset=" + startOffset
				+ ", todayOnly=" + todayOnly + "]";
	}

	@Override
	public void handleData() {
		
		List<Product> productList = ProductManager.getProducts(mongoClient, city, categoryList, 
				todayOnly, gpsQuery, latitude, longitude, maxDistance, 
				sortBy, startOffset, maxCount);
		
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
		
		String latitudeStr = request.getParameter(ServiceConstant.PARA_LATITUDE);
		String longitudeStr = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		String maxDistanceStr = request.getParameter(ServiceConstant.PARA_MAX_DISTANCE);
		
		if (!StringUtil.isEmpty(latitudeStr) && !StringUtil.isEmpty(longitudeStr)){
			gpsQuery = true;
			latitude = Double.parseDouble(latitudeStr);
			longitude = Double.parseDouble(longitudeStr);
		}
		
		if (!StringUtil.isEmpty(maxDistanceStr)){
			maxDistance = Double.parseDouble(maxDistanceStr);
		}
		
		String todayOnlyStr = request.getParameter(ServiceConstant.PARA_TODAY_ONLY);
		if (!StringUtil.isEmpty(todayOnlyStr)){
			todayOnly = (Integer.parseInt(todayOnlyStr) == 0 ? false : true);
		}		
		
		String categoryStr = request.getParameter(ServiceConstant.PARA_CATEGORIES);
		categoryList = UrlUtil.parserUrlIntArray(categoryStr);

		String sortByStr = request.getParameter(ServiceConstant.PARA_SORT_BY);
		if (!StringUtil.isEmpty(sortByStr)){
			sortBy = Integer.parseInt(sortByStr);
		}		
		
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
