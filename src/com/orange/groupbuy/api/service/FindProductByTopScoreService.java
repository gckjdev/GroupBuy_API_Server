package com.orange.groupbuy.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mongodb.DBCursor;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;

public class FindProductByTopScoreService extends CommonGroupBuyService {

	String appId;
	String city;
	int maxCount = 25;			
	int startOffset = 0;
	int startPrice = -100;
	int endPrice = 99999999;
	int category = -1;
	int reCountStatus = 0;                          // optional
	
	@Override
	public String toString() {
		return "FindProductByTopScoreService [appId=" + appId + ", city="
				+ city + ", maxCount=" + maxCount + ", startOffset="
				+ startOffset + ", startPrice=" + startPrice + ", endPrice="
				+ endPrice + ", category=" + category + "]";
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}
		String startOffsetStr = request.getParameter(ServiceConstant.PRAR_START_OFFSET);
		if (!StringUtil.isEmpty(startOffsetStr)){
			startOffset = Integer.parseInt(startOffsetStr);
		}		

		String maxCountStr = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		if (!StringUtil.isEmpty(maxCountStr)){
			maxCount = Integer.parseInt(maxCountStr);
		}	
	    city= request.getParameter(ServiceConstant.PARA_CITY);
		
	    String startPriceStr = request.getParameter(ServiceConstant.PARA_START_PRICE);
		if (!StringUtil.isEmpty(startPriceStr)){
			startPrice = Integer.parseInt(startPriceStr);
		}
		String endPriceStr = request.getParameter(ServiceConstant.PARA_END_PRICE);
		if (!StringUtil.isEmpty(endPriceStr)){
			endPrice = Integer.parseInt(endPriceStr);
		}	
		String categoryStr = request.getParameter(ServiceConstant.PARA_CATEGORIES);
		if (!StringUtil.isEmpty(categoryStr)){
			category = Integer.parseInt(categoryStr);
		}	
		
		String returnCountStr = request.getParameter(ServiceConstant.PARA_RETURN_COUNT); 
		if (!StringUtil.isEmpty(returnCountStr)){
			reCountStatus = Integer.parseInt(returnCountStr);
		}		
		
		
		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		DBCursor cursor = ProductManager.getTopScoreProductCursor(mongoClient, city, 
				category, startOffset, maxCount, startPrice, endPrice);
		if (reCountStatus == 0) {

			List<Product> productList = ProductManager.getProduct(cursor);
			
			if (productList == null || productList.size() == 0)
				return;
			
			for (Product p : productList){
				log.info("product id="+p.getId()+
						", top score="+p.getTopScore()+
						", title="+p.getTitle()+
						", bought="+p.getBought()+
						", startDate="+p.getStartDateString());
			}
			resultData = CommonServiceUtils.productListToJSONArray(productList);	
			
		} else {
			
			int returnCnt = ProductManager.getCursorCount(cursor);
			List<Product> productList = ProductManager.getProduct(cursor);
			JSONArray productArray = CommonServiceUtils.productListToJSONArray(productList);
			JSONObject object = new JSONObject();
			safePut(object, "list", productArray);
			safePut(object, "count", returnCnt);
			resultData = object;	
		}
	}
	
	private static void safePut(JSONObject object, String key, Object value) {
		if (value == null)
			return;
		object.put(key, value);
	}

}
