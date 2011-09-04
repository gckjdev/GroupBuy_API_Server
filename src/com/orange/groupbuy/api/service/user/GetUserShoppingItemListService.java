package com.orange.groupbuy.api.service.user;



import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

import com.mongodb.DBObject;

import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;


public class GetUserShoppingItemListService extends CommonGroupBuyService {

	String userId;
	String appId;
	
	

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "GetUserShoppingItemListService [userId=" + userId + ", appId="
				+ appId + "]";
	}

	@Override
	public void handleData() {
		
		ObjectId id = new ObjectId(userId);
		BasicDBObject user = (BasicDBObject) mongoClient.findOne(DBConstants.T_USER,DBConstants.F_USERID,id);
        DBObject userShoppingItemList =(DBObject) user.get(DBConstants.F_SHOPPING_LIST);
		if (userShoppingItemList != null ){			
			JSONArray jsonArray = CommonServiceUtils.userShoppingItemListToJSONArray(userShoppingItemList);
			resultData = jsonArray;
		}

	}
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		
		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		

		return true;				
	}


}
