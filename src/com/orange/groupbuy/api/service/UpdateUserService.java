package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.User;
import com.orange.groupbuy.manager.UserManager;

public class UpdateUserService extends CommonGroupBuyService {

	String appId;
	String userId;
	// information for update, all optinal
	String password;
	String newPassword;
	String nickName;
	String avatar;
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		newPassword = request.getParameter(ServiceConstant.PARA_NEW_PASSWORD);
		avatar = request.getParameter(ServiceConstant.PARA_AVATAR);
		nickName = request.getParameter(ServiceConstant.PARA_NICKNAME);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public void handleData() {

		User user = UserManager.findUserByUserId(mongoClient, userId);
		if (user == null) {
			log.info("<UpateUserService> cannot find user:" + userId);
			resultCode = ErrorCode.ERROR_USERID_NOT_FOUND;
			return;
		}
				
		if (password != null && !password.isEmpty() && newPassword != null && !newPassword.isEmpty()){
			if (newPassword.length() < ServiceConstant.PARA_PASSWORD_MIN_LENGTH){
				resultCode = ErrorCode.ERROR_PASSWORD_NOT_VALID;
				return;
			}
			
			user.setPassword(newPassword);
		}

		if (nickName != null && nickName.length() > 0) {
			user.setNickName(nickName);
		}

		mongoClient.save(DBConstants.T_USER, user.getDbObject());
		log.info("<UpateUserService> update user ("+user.getUserId()+") successfully");
	}
}
