package com.orange.groupbuy.api.service.user;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.api.service.CommonGroupBuyService;

public class BindUserService extends CommonGroupBuyService {

	// mandantory
	String userId;
	String appId;	

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
	public void handleData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

}
