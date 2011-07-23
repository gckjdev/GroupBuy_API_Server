package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import sun.print.resources.serviceui;

import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;

public class FindAllProductsWithLocation extends CommonGroupBuyService {
	String appId;
	String maxCount;
	String startOffset;
	String latitude;
	String longitude;
	String radius;

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
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		startOffset = request.getParameter(ServiceConstant.PRAR_START_OFFSET);
		latitude = request.getParameter(ServiceConstant.PARA_LANGUAGE);
		longitude = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		radius = request.getParameter(ServiceConstant.PARA_RADIUS);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}
		if (!check(latitude, ErrorCode.ERROR_PARAMETER_LANGUAGE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LANGUAGE_NULL)) {
			return false;
		}
		if (!check(longitude, ErrorCode.ERROR_PARAMETER_LONGITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LONGITUDE_NULL)) {
			return false;
		}
		
		if (!check(radius, ErrorCode.ERROR_PARAMETER_RADIUS_EMPTY,
				ErrorCode.ERROR_PARAMETER_RADIUS_NULL)) {
			return false;
		}
		return true;
	}

}
