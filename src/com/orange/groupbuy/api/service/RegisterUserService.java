package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.orange.common.mail.MailSender;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.User;
import com.orange.groupbuy.manager.UserManager;

public class RegisterUserService extends CommonGroupBuyService {

	private String appId;
	int registerType;						// by email, sina weibo, tecent weibo, etc							
	private String strRegisterType;
	
	// for email registration
	String email;
	String password;
	boolean needVerification = true;
	
	// for sina weibo or tecent weibo ID
	String snsId;
	// optional, shared, for sina weibo or tecent weibo 
	String nickName;
	String avatar;
	String accessToken;
	String accessTokenSecret;
	String province;
	String city;
	String location;
	String gender;
	String birthday;
	String domain;

	@Override
	public String toString() {
		return "RegisterUserService [appId=" + appId + ", email=" + email
				+ ", password=" + password + ", needVerification="
				+ needVerification + ", snsId=" + snsId + ", nickName="
				+ nickName + ", avatar=" + avatar + ", accessToken="
				+ accessToken + ", accessTokenSecret=" + accessTokenSecret
				+ ", province=" + province + ", city=" + city + ", location="
				+ location + ", gender=" + gender + ", birthday=" + birthday
				+ ", domain=" + domain + ", registerType=" + registerType
				+ ", strRegisterType=" + strRegisterType + "]";
	}

	public void sendVerification(BasicDBObject user){
		String verifyCode = user.getString(DBConstants.F_VERIFYCODE);

		// "localhost:8000/service?code=123456"
		String confirmUrl = ServiceConstant.VERIFY_URL + "?"
						  + ServiceConstant.PARA_VERIFYCODE + "=" + verifyCode;

		MailSender sm = new MailSender();
		sm.send(email, confirmUrl);
	}
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		strRegisterType = request
				.getParameter(ServiceConstant.PARA_REGISTER_TYPE);
		registerType = Integer.parseInt(strRegisterType);

		email = request.getParameter(ServiceConstant.PARA_EMAIL);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		String Str_NeedVerificaton = request
				.getParameter(ServiceConstant.PARA_VERIFICATION);

		nickName = request.getParameter(ServiceConstant.PARA_NICKNAME);
		avatar = request.getParameter(ServiceConstant.PARA_AVATAR);
		accessToken = request.getParameter(ServiceConstant.PARA_ACCESS_TOKEN);
		accessTokenSecret = request
				.getParameter(ServiceConstant.PARA_ACCESS_TOKEN_SECRET);
		province = request.getParameter(ServiceConstant.PARA_PROVINCE);
		city = request.getParameter(ServiceConstant.PARA_CITY);
		location = request.getParameter(ServiceConstant.PARA_LOCATION);
		gender = request.getParameter(ServiceConstant.PARA_GENDER);
		birthday = request.getParameter(ServiceConstant.PARA_BIRTHDAY);
		domain = request.getParameter(ServiceConstant.PARA_DOMAIN);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(strRegisterType,
				ErrorCode.ERROR_PARAMETER_REGISTER_TYPE_EMPTY,
				ErrorCode.ERROR_PARAMETER_REGISTER_TYPE_NULL))
			return false;

		switch (registerType) {
		case ServiceConstant.REGISTER_TYPE_EMAIL:
			if (Str_NeedVerificaton != null
					&& !Str_NeedVerificaton
							.equals(ServiceConstant.VERIFICATION)
					&& !Str_NeedVerificaton.equals("true")) {
				needVerification = false;
			}
			if (!StringUtil.isValidMail(email)) {
				log.info("<registerUser> user email(" + email + ") not valid");
				resultCode = ErrorCode.ERROR_EMAIL_NOT_VALID;
				return false;
			}
			if (!check(password, ErrorCode.ERROR_PARAMETER_PASSWORD_EMPTY,
					ErrorCode.ERROR_PARAMETER_PASSWORD_NULL))
				return false;

			if (!check(email, ErrorCode.ERROR_PARAMETER_EMAIL_EMPTY,
					ErrorCode.ERROR_PARAMETER_PASSWORD_NULL))
				return false;
			break;
		case ServiceConstant.REGISTER_TYPE_SINA:
			snsId = request.getParameter(ServiceConstant.PARA_SINAID);
			if (!check(snsId, ErrorCode.ERROR_PARAMETER_SNSID_EMPTY,
					ErrorCode.ERROR_PARAMETER_SNSID_NULL))
				return false;
			break;
		case ServiceConstant.REGISTER_TYPE_QQ:
			snsId = request.getParameter(ServiceConstant.PARA_QQID);
			if (!check(snsId, ErrorCode.ERROR_PARAMETER_SNSID_EMPTY,
					ErrorCode.ERROR_PARAMETER_SNSID_NULL))
				return false;
			break;
		default:
			resultCode = ErrorCode.ERROR_PARAMETER_UNKNOWN_REGISTER_TYPE;
			log.info("<registerUser> unknown register type:" + strRegisterType);
			return false;
		}

		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public void handleData() {

		BasicDBObject user = new BasicDBObject();

		switch (registerType) {
		case ServiceConstant.REGISTER_TYPE_EMAIL:
			if (UserManager.findUserByEmail(mongoClient, email) != null) {
				log.info("<registerEmail> user email(" + email + ") exist");
				resultCode = ErrorCode.ERROR_EMAIL_EXIST;
				return;
			}

			user = UserManager.createUserByEmail(mongoClient, appId, email,
					password, needVerification);

			break;
		case ServiceConstant.REGISTER_TYPE_SINA:
			if (UserManager.findUserBySinaId(mongoClient, snsId) != null) {
				log.info("<registerSns> user ID (" + snsId + ")exist");
				resultCode = ErrorCode.ERROR_SNS_ID_EXIST;
				return;
			}

			user = UserManager.createUserBySnsId(mongoClient, appId, snsId,
					nickName, avatar, accessToken, accessTokenSecret, province,
					city, location, gender, birthday, domain, registerType);
			break;
		case ServiceConstant.REGISTER_TYPE_QQ:
			if (UserManager.findUserByTencentId(mongoClient, snsId) != null) {
				log.info("<registerSns> user ID (" + snsId + ")exist");
				resultCode = ErrorCode.ERROR_SNS_ID_EXIST;
				return;
			}

			user = UserManager.createUserBySnsId(mongoClient, appId, snsId,
					nickName, avatar, accessToken, accessTokenSecret, province,
					city, location, gender, birthday, domain, registerType);
			break;
		default:
			break;
		}

		if (user == null) {
			resultCode = ErrorCode.ERROR_CREATE_USER;
			log.info("<registerEmail> fail to create user");
			return;
		} else {
			log.info("<RegisterUserService> user=" + user.toString());
		}

		String userId = user.getString(MongoDBClient.ID);

		// set result data, return userId
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, userId);
		resultData = obj;

		// send email verification
		if (registerType == ServiceConstant.REGISTER_TYPE_EMAIL
				&& needVerification) {
			sendVerification(user);
		}
	}

}
