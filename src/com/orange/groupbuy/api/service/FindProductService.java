package com.orange.groupbuy.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import org.apache.cassandra.cli.CliParser.newColumnFamily_return;
import com.mongodb.DBCursor;

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
	int reCountStatus = 0;                          // optional
	int minCategory = -1;
	int maxCategory = -1;
	String siteId = null;							// optional

	boolean gpsQuery = false;						// internal usage
	int productType = DBConstants.UNDEFINE;			// optional
	
	
	
	@Override
	public String toString() {
		return "FindProductService [appId=" + appId + ", categoryList="
				+ categoryList + ", city=" + city + ", gpsQuery=" + gpsQuery
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", maxCategory=" + maxCategory + ", maxCount=" + maxCount
				+ ", maxDistance=" + maxDistance + ", minCategory="
				+ minCategory + ", productType=" + productType
				+ ", reCountStatus=" + reCountStatus + ", siteId=" + siteId
				+ ", sortBy=" + sortBy + ", startOffset=" + startOffset
				+ ", todayOnly=" + todayOnly + "]";
	}

	@Override
	public void handleData() {
		
		DBCursor cursor = ProductManager.getProductCursor(mongoClient, city, 
				categoryList,minCategory, maxCategory, 				
				todayOnly, gpsQuery, latitude, longitude, maxDistance, 
				productType, siteId,
				sortBy, startOffset, maxCount);
		if (reCountStatus > 0) {
			int reCnt = ProductManager.getCursorCount(cursor);
			List<Product> productList = ProductManager.getProduct(cursor);
			JSONArray productArray = CommonServiceUtils.productListToJSONArray(productList);
			JSONObject object = new JSONObject();
			safePut(object, ServiceConstant.PARA_LIST, productArray);
			safePut(object, ServiceConstant.PARA_RETURN_COUNT, reCnt);
			resultData = object;
		} else {
			List<Product> productList = ProductManager.getProduct(cursor); 	
			resultData = CommonServiceUtils.productListToJSONArray(productList);		
		}
		
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
		String longitudeStr = request.getParameter(ServiceConstant.PARA_LONGITUDE);
		String maxDistanceStr = request.getParameter(ServiceConstant.PARA_MAX_DISTANCE);
		
		siteId = request.getParameter(ServiceConstant.PARA_SITE_ID);
		
		if (!StringUtil.isEmpty(latitudeStr) && !StringUtil.isEmpty(longitudeStr) && !StringUtil.isEmpty(maxDistanceStr)){
			gpsQuery = true;
			latitude = Double.parseDouble(latitudeStr);
			longitude = Double.parseDouble(longitudeStr);
			maxDistance = Double.parseDouble(maxDistanceStr);
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
		if (categoryList != null && categoryList.size() == 1){
			int category = categoryList.get(0).intValue();
			
			// set Taobao Miaosha and Zhekou range
			if (category == DBConstants.C_CATEGORY_TAOBAO_MIAOSHA){
				category = -1;
				minCategory = DBConstants.C_CATEGORY_TAOBAO_MIAOSHA_MIN;
				maxCategory = DBConstants.C_CATEGORY_TAOBAO_MIAOSHA_MAX;
			}
			else if (category == DBConstants.C_CATEGORY_TAOBAO_ZHEKOU){
				category = -1;
				minCategory = DBConstants.C_CATEGORY_TAOBAO_ZHEKOU_MIN;
				maxCategory = DBConstants.C_CATEGORY_TAOBAO_ZHEKOU_MAX;
			}
		}
		
		String productTypeStr = request.getParameter(ServiceConstant.PARA_PRODUCT_TYPE);
		if (!StringUtil.isEmpty(productTypeStr)){
			productType = Integer.parseInt(productTypeStr);
		}

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
		
		String returnCountStr = request.getParameter(ServiceConstant.PARA_RETURN_COUNT); 
		if (!StringUtil.isEmpty(returnCountStr)){
			reCountStatus = Integer.parseInt(returnCountStr);
		}		
		
		return true;
	}
	
	private static void safePut(JSONObject object, String key, Object value) {
		if (value == null)
			return;
		object.put(key, value);
	}

}
