package com.orange.groupbuy.api.service.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.ShoppingCategory;

public class GetUserShoppingItemListService extends CommonGroupBuyService {

	String userId;
	String appId;
	
	

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleData() {
		
		DBCursor cursor = mongoClient.find(DBConstants.T_USER,DBConstants.F_USERID,userId);
        List<BasicDBObject> userShoppingItemList = new ArrayList<BasicDBObject>();
		if(cursor !=null)
		{
	        Iterator<?> iter = cursor.iterator();
	        
	        while (iter.hasNext()){
	            BasicDBObject obj = (BasicDBObject)iter.next();
	            BasicDBObject userShoppingItem = new BasicDBObject(obj);
	            userShoppingItemList.add(userShoppingItem);
	        }
		}
		
		cursor.close();// TODO Auto-generated method stub
		
		if (userShoppingItemList != null && userShoppingItemList.size() > 0){			
			JSONArray jsonArray = CommonServiceUtils.userShoppingItemListToJSONArray(userShoppingItemList);
			resultData = jsonArray;
		}

	}
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);

		
		String priceStr = request.getParameter(ServiceConstant.PARA_PRICE);
		String rebateStr = request.getParameter(ServiceConstant.PARA_REBATE);
		
		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		

		return true;				
	}


}
