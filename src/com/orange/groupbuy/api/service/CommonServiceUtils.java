package com.orange.groupbuy.api.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.App;
import com.orange.groupbuy.dao.Category;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.ShoppingCategory;
import com.orange.groupbuy.dao.Site;
import com.orange.groupbuy.dao.User;

public class CommonServiceUtils {

//	public static JSONObject userToJSON(DBObject user) {
//		JSONObject obj = new JSONObject();
//		obj.put(ServiceConstant.PARA_USERID, user.get(DBConstants.F_ID)
//				.toString());
//		return obj;
//	}
	
	public static JSONObject userToJSON(User user) {
		JSONObject obj = new JSONObject();
		safePut(obj, ServiceConstant.PARA_USERID, user.getUserId());
		safePut(obj, ServiceConstant.PARA_EMAIL, user.getEmail());
		safePut(obj, ServiceConstant.PARA_NICKNAME, user.getNickName());
		safePut(obj, ServiceConstant.PARA_AVATAR, user.getAvatar());
		safePut(obj, ServiceConstant.PARA_QQ_ACCESS_TOKEN, user.getQQAccessToken());
		safePut(obj, ServiceConstant.PARA_QQ_ACCESS_TOKEN_SECRET, user.getQQAccessTokenSecret());
		safePut(obj, ServiceConstant.PARA_SINA_ACCESS_TOKEN, user.getSinaAccessToken());
		safePut(obj, ServiceConstant.PARA_SINA_ACCESS_TOKEN_SECRET, user.getSinaAccessTokenSecret());
		safePut(obj, ServiceConstant.PARA_PASSWORD, user.getPassword());
		safePut(obj, ServiceConstant.PARA_SINA_ID, user.getSinaID());
		safePut(obj, ServiceConstant.PARA_QQ_ID, user.getQQID());
		return obj;
	}

	private static void safePut(JSONObject object, String key, Object value) {
		if (value == null)
			return;

		object.put(key, value);
	}

	public static JSONObject siteListToJSONObject(List<Site> siteList) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (Site site : siteList) {
			JSONObject object = new JSONObject();
			safePut(object, ServiceConstant.PARA_SITE_ID, site.getStringObjectId());
			safePut(object, ServiceConstant.PARA_SITE_URL, site.getSiteURL());
			safePut(object, ServiceConstant.PARA_FILE_TYPE, site.getFileType());
			safePut(object, ServiceConstant.PARA_TYPE, site.getType());
			safePut(object, ServiceConstant.PARA_SITE_NAME, site.getName());
			safePut(object, ServiceConstant.PARA_DOWNLOAD_COUNT, site.getDownloadCount());			
			safePut(object, ServiceConstant.PARA_COUNTRYCODE, site.getCountryCode());			
			
			jsonArray.add(object);
		}
		
		jsonObject.put(ServiceConstant.PARA_DATA, jsonArray);
		jsonObject.put(ServiceConstant.PARA_DATA_COUNT, siteList.size());
		
		return jsonObject;
	}
	
	public static JSONArray productListToJSONArray(List<Product> products) {

		if (products == null)
			return null;

		JSONArray jsonArray = new JSONArray();
		for (Product product : products) {
			JSONObject object = new JSONObject();

			safePut(object, ServiceConstant.PARA_ID, product.getId());
			safePut(object, ServiceConstant.PARA_LOC, product.getLoc());
			safePut(object, ServiceConstant.PARA_CITY, product.getCity());
			safePut(object, ServiceConstant.PARA_IMAGE, product.getImage());
			safePut(object, ServiceConstant.PARA_TITLE, product.getTitle());
			safePut(object, ServiceConstant.PARA_START_DATA,
					product.getStartDateString());
			safePut(object, ServiceConstant.PARA_END_DATA,
					product.getEndDateString());
			safePut(object, ServiceConstant.PARA_PRICE, product.getPrice());
			safePut(object, ServiceConstant.PARA_VALUE, product.getValue());
			safePut(object, ServiceConstant.PARA_BOUGHT, product.getBought());
			// safePut(object, ServiceConstant.PARA_SITE_ID,
			// product.getSiteId());
			safePut(object, ServiceConstant.PARA_SITE_NAME,
					product.getSiteName());
			 safePut(object, ServiceConstant.PARA_SITE_URL,
			 product.getSiteUrl());
			safePut(object, ServiceConstant.PARA_REBATE, product.getRebate());
			safePut(object, ServiceConstant.PARA_GPS, product.getGPS());
			safePut(object, ServiceConstant.PARA_ADDRESS, product.getAddress());
			safePut(object, ServiceConstant.PARA_TEL, product.getTel());
			safePut(object, ServiceConstant.PARA_WAP_URL, product.getWapLoc());
			safePut(object, ServiceConstant.PARA_UP, product.getUp());
			safePut(object, ServiceConstant.PARA_DOWN, product.getDown());
			
			safePut(object, ServiceConstant.PARA_TOP_SCORE,
					product.getTopScore());

			jsonArray.add(object);
		}
		return jsonArray;
	}

	public static Object AppToJSON(App app) {
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_VERSION, app.getVersion());
		obj.put(ServiceConstant.PARA_APPURL, app.getAppUrl());
		return obj;
	}

	public static Object appKeywordToJSON(App app) {

		List<String> keywordList = app.getAppKeywordList();
		if (keywordList == null)
			return null;

		JSONArray jsonArray = new JSONArray();
		for (String keyword : keywordList) {
			jsonArray.add(keyword);
		}

		return jsonArray;
	}

	public static JSONArray categoryListToJSONArray(List<Category> categoryList) {
		if (categoryList == null)
			return null;

		JSONArray jsonArray = new JSONArray();
		for (Category category : categoryList) {
			JSONObject object = new JSONObject();

			safePut(object, ServiceConstant.PARA_CATEGORY_NAME,
					category.getCategoryName());
			
			safePut(object, ServiceConstant.PARA_CATEGORY_ID,
					category.getCategoryId());
			
			jsonArray.add(object);
		}
		return jsonArray;
	}

	public static JSONArray shoppingcategoryListToJSONArray(
			List<ShoppingCategory> shoppingcategoryList) {
		if (shoppingcategoryList == null)
			return null;
		
		

		JSONArray jsonArray = new JSONArray();
		for (ShoppingCategory category : shoppingcategoryList) {
			JSONObject object = new JSONObject();
			JSONArray subjsonArray = subShoppingCategoryListToJSONArray(category.getSubCategoryList());

			
			safePut(object, ServiceConstant.PARA_CATEGORY_NAME,	category.getCategoryName());
			safePut(object, ServiceConstant.PARA_SUB_CATEGORY,subjsonArray);
			
			jsonArray.add(object);
		}
		return jsonArray;
	}
	
	public static JSONArray subShoppingCategoryListToJSONArray(
			BasicDBList list) {
		if(list == null)
			return null;

		JSONArray jsonArray = new JSONArray();
		for (Object subCategory : list) {
			String categoryId = ((BasicDBObject)subCategory).getString(DBConstants.F_SUB_CATEGORY_ID);
			String categoryName = ((BasicDBObject)subCategory).getString(DBConstants.F_SUB_CATEGORY_NAME);
			Object categoryKey = ((BasicDBObject) subCategory).get(DBConstants.F_SUB_CATEGORY_KEYS);
			
			JSONObject object = new JSONObject();
			safePut(object, ServiceConstant.PARA_SUB_CATEGORY_ID,	categoryId);
			safePut(object, ServiceConstant.PARA_SUB_CATEGORY_NAME,categoryName);
			safePut(object, ServiceConstant.PARA_SUB_CATEGORY_KEYS,categoryKey);
			jsonArray.add(object);
		}
		return jsonArray;
	}

	public static JSONArray userShoppingItemListToJSONArray(
			BasicDBList list) {
		if(list == null)
			return null;

		JSONArray jsonArray = new JSONArray();
		for(Object item : list){
			JSONObject object = new JSONObject();
			BasicDBObject shoppingItem = (BasicDBObject) item;
			safePut(object, ServiceConstant.PARA_ITEMID, shoppingItem.get(DBConstants.F_ITEM_ID));
			safePut(object, ServiceConstant.PARA_CATEGORY_NAME, shoppingItem.get(DBConstants.F_CATEGORY_NAME));
			safePut(object, ServiceConstant.PARA_SUB_CATEGORY_NAME, shoppingItem.get(DBConstants.F_SUB_CATEGORY_NAME));
			safePut(object, ServiceConstant.PARA_CITY, shoppingItem.get(DBConstants.F_CITY));
			safePut(object, ServiceConstant.PARA_LATITUDE, shoppingItem.get(DBConstants.F_LATITUDE));
			safePut(object, ServiceConstant.PARA_LONGITUDE, shoppingItem.get(DBConstants.F_LONGITUDE));
			safePut(object, ServiceConstant.PARA_RADIUS, shoppingItem.get(DBConstants.F_RADIUS));
			jsonArray.add(object);
			
		}
			
		
	
		return jsonArray;
	}
	
	public static JSONArray userShoppingItemProductCountToJSONArray(Map<String, Integer> map) {
	    JSONArray jsonArray = new JSONArray();
        for(String itemId : map.keySet()) {
            JSONObject object = new JSONObject();
            object.put(ServiceConstant.PARA_ITEMID, itemId);
            object.put(ServiceConstant.PARA_MATCH_ITEM_COUNT, map.get(itemId));
            jsonArray.add(object);
        }
        return jsonArray;
	}
}
