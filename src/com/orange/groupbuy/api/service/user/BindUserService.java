package com.orange.groupbuy.api.service.user;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.mail.MailSender;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.User;
import com.orange.groupbuy.manager.UserManager;

public class BindUserService extends CommonGroupBuyService {

	public static final int REGISTER_TYPE_EMAIL = 0 ;
	public static final int REGISTER_TYPE_SINAWEIBO = 1 ;
	public static final int REGISTER_TYPE_TENCENTWEIBO = 2 ;

	String userId;
	String appId;	

	int registerType;						// by email, sina weibo, tecent weibo, etc	
	
	String email;
	String password;
	boolean needVerification = true;

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

	public void sendVerification(User user){
		String verifyCode = user.getString(DBConstants.F_VERIFYCODE);

		String confirmUrl = ServiceConstant.VERIFY_URL + "?"
						  + ServiceConstant.PARA_VERIFYCODE + "=" + verifyCode;

		MailSender sm = new MailSender();
		sm.send(email, confirmUrl);
	}

	@Override
	public String toString() {
		return "BindUserService [userId=" + userId + ", appId=" + appId
				+ ", email=" + email + ", password=" + password
				+ ", needVerification=" +  ", snsId=" + snsId
				+ ", nickName=" + nickName + ", avatar=" + avatar
				+ ", accessToken=" + accessToken + ", accessTokenSecret="
				+ accessTokenSecret + ", province=" + province + ", city="
				+ city + ", location=" + location + ", gender=" + gender
				+ ", birthday=" + birthday + ", domain=" + domain
				+ ", registerType=" + registerType + ", needVerfication="
				+ needVerification + "]";
	}

	@Override
	public void handleData() {
		User user = UserManager.findUserByUserId(mongoClient, userId);
		if (user == null){
			log.info("Cannot find user"+userId);
		}
		else{
			switch (registerType){
			case REGISTER_TYPE_EMAIL:
				if (UserManager.findUserByEmail(mongoClient, email) != null){
					log.info("<registerEmail> user email("+email+") exist");
					resultCode = ErrorCode.ERROR_EMAIL_EXIST;
					return;
				}
				
				UserManager.bindUserByEmail(mongoClient, appId, user, email, password, needVerification);

				if (needVerification) {
					sendVerification(user);
				}
				break;
			case REGISTER_TYPE_SINAWEIBO:
				if(UserManager.findUserBySinaId(mongoClient, snsId) != null){
					log.info("<registerSns> user ID ("+snsId+")exist");
					resultCode = ErrorCode.ERROR_SNS_ID_EXIST;
					return;
				}
				break;
			case REGISTER_TYPE_TENCENTWEIBO:
				if(UserManager.findUserByTencentId(mongoClient, snsId) != null){
					log.info("<registerSns> user ID ("+snsId+")exist");
					resultCode = ErrorCode.ERROR_SNS_ID_EXIST;
					return;
				}
				break;
			default:
				break;
		
			} 
		}
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		email = request.getParameter(ServiceConstant.PARA_EMAIL);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		registerType = Integer.parseInt(request.getParameter(ServiceConstant.PARA_REGISTER_TYPE));
		needVerification = Boolean.parseBoolean(request.getParameter(ServiceConstant.PARA_VERIFICATION));
		
		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		
		if (registerType == REGISTER_TYPE_EMAIL && !check(password, ErrorCode.ERROR_PARAMETER_PASSWORD_EMPTY, ErrorCode.ERROR_PARAMETER_PASSWORD_NULL))
			return false;

		if (registerType == REGISTER_TYPE_EMAIL && !check(email, ErrorCode.ERROR_PARAMETER_EMAIL_EMPTY, ErrorCode.ERROR_PARAMETER_PASSWORD_NULL))
			return false;
		
		if((registerType == REGISTER_TYPE_SINAWEIBO ||registerType == REGISTER_TYPE_TENCENTWEIBO) && !check(snsId, ErrorCode.ERROR_PARAMETER_SNSID_EMPTY, ErrorCode.ERROR_PARAMETER_SNSID_NULL))
			return false;
		
		return true;
		
	}

}
