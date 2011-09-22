package com.orange.groupbuy.api.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.ProductManager;

public class WriteCommentService extends CommonGroupBuyService {

	String appId;
	String userId;
	String productId;
	String nickName;	
	String content;

	boolean hasLocation;
	double longitude;
	double latitude;
	
	@Override
	public String toString() {
		return "WriteCommentService [nickName=" + nickName
				+ ", content=" + content + ", appId=" + appId
				+ ", hasLocation=" + hasLocation + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", productId=" + productId
				+ ", userId=" + userId + "]";
	}
	
	@Override
	public void handleData() {				
		// write counter into product
		ProductManager.writeCommet(mongoClient, productId, userId, nickName, content, new Date());
	}
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		productId = request.getParameter(ServiceConstant.PARA_ID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		nickName = request.getParameter(ServiceConstant.PARA_NICKNAME);
		content = request.getParameter(ServiceConstant.PARA_COMMENT_CONTENT);
		String latitudeStr = request.getParameter(ServiceConstant.PARA_LATITUDE);
		String longitudeStr = request.getParameter(ServiceConstant.PARA_LONGITUDE);
		
		if (!check(productId, ErrorCode.ERROR_PARAMETER_PRODUCTID_EMPTY,
				ErrorCode.ERROR_PARAMETER_PRODUCTID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		
		if (!StringUtil.isEmpty(longitudeStr) && !StringUtil.isEmpty(latitudeStr)){
			hasLocation = true;
			longitude = Double.parseDouble(longitudeStr);
			latitude = Double.parseDouble(latitudeStr);
		}
		else{
			hasLocation = false;
		}
		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}
}
