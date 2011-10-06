package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.User;
import com.orange.groupbuy.manager.UserManager;

public class LoginUserService extends CommonGroupBuyService {
	
	String appId;	
	String email;
	String password;
	String deviceToken;

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		email = request.getParameter(ServiceConstant.PARA_EMAIL);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		deviceToken = request.getParameter(ServiceConstant.PARA_DEVICETOKEN);
		
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
		
		User user = UserManager.findUserByEmail(mongoClient, email);
		
		if (user == null){
			resultCode = ErrorCode.ERROR_USER_EMAIL_NOT_FOUND;
			log.info("<loginUser> user email " + email + " not found");
			return;
		} 
		else if(user.getString(DBConstants.F_PASSWORD).equals(password)){
			log.info("<LoginUserService> user found user ="+user.toString());	
		}
		else{
			resultCode = ErrorCode.ERROR_PASSWORD_NOT_MATCH;
			log.info("<loginUser> password not match");
			return;
		}
		
		if (deviceToken != null){
			log.info("<loginUser> save device token " + deviceToken + " for user " + 
					user.getUserId());
			user.setDeviceToke(deviceToken);
			UserManager.save(mongoClient, user);
		}
		
		resultData = CommonServiceUtils.userToJSON(user);		
	}

}
