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

public class LoginUserService extends CommonGroupBuyService {
	
	String email;
	String password;

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		email = request.getParameter(ServiceConstant.PARA_EMAIL);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		
		if (!StringUtil.isValidMail(email)){
			log.info("<LoginUser> user email("+email+") not valid");
			resultCode = ErrorCode.ERROR_EMAIL_NOT_VALID;
			return false;
		}
		
		if (!check(email, ErrorCode.ERROR_PARAMETER_EMAIL_EMPTY, ErrorCode.ERROR_PARAMETER_EMAIL_NULL))
			return false;
			
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
		
		BasicDBObject user = (BasicDBObject) UserManager.findUserByEmail(mongoClient, email);
		
		if (user == null){
			resultCode = ErrorCode.ERROR_USERID_NOT_FOUND;
			log.info("<loginUser> user not found");
			return;
		} 
		else if(user.getString(DBConstants.F_PASSWORD).equals(password)){
			log.info("<LoginUserService> user="+user.toString());	
		}
		else{
			resultCode = ErrorCode.ERROR_PASSWORD_NOT_MATCH;
			log.info("<loginUser> password not match");
			return;
		}
		
		String userId = user.getString(MongoDBClient.ID);
		
		// set result data, return userId
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, userId);
		resultData = obj;
		
	}

}
