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
import com.orange.groupbuy.manager.UserManager;

public class RegisterUserService extends CommonGroupBuyService {

	private String appId;
	
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
			
	int registerType;						// by email, sina weibo, tecent weibo, etc							
	
	@Override
	public String toString() {
		return "RegisterUserService [appId=" + appId + ", email=" + email
				+ ", password=" + password + ", verification="
				+ needVerification + "]";
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
		email = request.getParameter(ServiceConstant.PARA_EMAIL);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		String Str_NeedVerificaton = request.getParameter(ServiceConstant.PARA_VERIFICATION);
		
		if (Str_NeedVerificaton != null && !Str_NeedVerificaton.equals(ServiceConstant.VERIFICATION)) {
			needVerification = false;
		}
		if (!StringUtil.isValidMail(email)){
			log.info("<registerUser> user email("+email+") not valid");
			resultCode = ErrorCode.ERROR_EMAIL_NOT_VALID;
			return false;
		}
		if (!check(email, ErrorCode.ERROR_PARAMETER_EMAIL_EMPTY, ErrorCode.ERROR_PARAMETER_EMAIL_NULL)) {
			return false;
		}
		if (!check(password, ErrorCode.ERROR_PARAMETER_PASSWORD_EMPTY, ErrorCode.ERROR_PARAMETER_PASSWORD_NULL)) {
			return false;
		}
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
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
		
		if (UserManager.findUserByEmail(mongoClient, email) != null){
			log.info("<registerEmail> user email("+email+") exist");
			resultCode = ErrorCode.ERROR_EMAIL_EXIST;
			return;
		}
		
		BasicDBObject user =  UserManager.createUserByEmail(mongoClient, appId, email, password, needVerification);
	
		if (user == null) {
			resultCode = ErrorCode.ERROR_CREATE_USER;
			log.info("<registerEmail> fail to create user");
			return;
		} 
		else {
			log.info("<RegisterUserService> user="+user.toString());			
		}
		
		String userId = user.getString(MongoDBClient.ID);
		
		// set result data, return userId
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, userId);
		resultData = obj;
		
		// send email verification
		if (needVerification) {
			sendVerification(user);
		}
	}

}
