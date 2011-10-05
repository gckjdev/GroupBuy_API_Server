package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import com.mongodb.DBObject;
import com.orange.common.utils.StringUtil;
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
	String nickName;
	String avatar;
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
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
	public String toString() {
		return "UpdateUserService [appId=" + appId + ", avatar=" + avatar
				+ ", nickName=" + nickName + ", password=" + password
				+ ", userId=" + userId + "]";
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
				
		if (!StringUtil.isEmpty(password)){
			user.setPassword(password);
		}

		if (!StringUtil.isEmpty(nickName)) {
			user.setNickName(nickName);
		}

		UserManager.save(mongoClient, user);
		log.info("<UpateUserService> update user ("+user.getUserId()+") successfully");
	}	
}
