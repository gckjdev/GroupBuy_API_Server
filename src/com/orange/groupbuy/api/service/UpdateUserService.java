package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.UserManager;

public class UpdateUserService extends CommonGroupBuyService {

	String appId;
	String userId;

	// information for update, all optinal
	String password;
	String newPassword;

	// optional
	String nickName;
	String avatar;
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		newPassword = request.getParameter(ServiceConstant.PARA_NEW_PASSWORD);
		
		if (!check(password, ErrorCode.ERROR_PARAMETER_PASSWORD_EMPTY, ErrorCode.ERROR_PARAMETER_PASSWORD_NULL))
			return false;
		
		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public void handleData() {
		
//		BasicDBObject user = (BasicDBObject) UserManager.findUserByEmail(mongoClient, email);
//		
//		if (user == null){
//			resultCode = ErrorCode.ERROR_USERID_NOT_FOUND;
//			log.info("<updateUser> user not found");
//			return;
//		} 
//		else if(user.getString(DBConstants.F_PASSWORD).equals(password)){
//			log.info("<updateUsere> user="+user.toString());	
//		}
//		else{
//			log.info("<updateUser> user password("+password+") not match");
//			resultCode = ErrorCode.ERROR_PASSWORD_NOT_MATCH;
//			return;
//		}
		
		if(newPassword != null && newPassword.length() >= 0){
//			UserManager.updatePassword(mongoClient, email, new_password);
		}
		
//		String userId = user.getString(MongoDBClient.ID);
		
		// set result data, return userId
//		JSONObject obj = new JSONObject();
//		obj.put(ServiceConstant.PARA_USERID, userId);
//		resultData = obj;
		
	}
	
}
