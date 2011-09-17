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

		int result = -1;
		User user = UserManager.findUserByUserId(mongoClient, userId);

		if (user == null) {
			log.info("cannot find user:" + userId);
		} else {

			if (password != null
					&& password.length() > 0
					&& newPassword != null
					&& newPassword.length() > ServiceConstant.PARA_PASSWORD_MIN_LENGTH
					&& user.getString(DBConstants.F_PASSWORD).equals(password)) {
				user.put(DBConstants.F_PASSWORD, newPassword);
				result = 0;
			}

			if (avatar != null && avatar.length() > 0) {
				user.put(DBConstants.F_AVATAR, avatar);
				result = 0;
			}

			if (nickName != null && nickName.length() > 0) {
				user.put(DBConstants.F_NICKNAME, nickName);
				result = 0;
			}

			if (result != 0) {
				resultCode = ErrorCode.ERROR_UPDATE_USER_INFO_FAILED;
			} else {
				mongoClient.save(DBConstants.T_USER, (DBObject) user);
				resultCode = 0;
			}

		}
	}
}
