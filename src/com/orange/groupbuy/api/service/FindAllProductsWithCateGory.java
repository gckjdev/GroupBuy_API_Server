package com.orange.groupbuy.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;
import com.orange.groupbuy.util.UrlUtil;

public class FindAllProductsWithCategory extends CommonGroupBuyService {

	String categories;
	String appId;
	String maxCount;
	String startOffset;
	String city;

	@Override
	public void handleData() {
		List<String> categoryList = UrlUtil.parserUrlArray(categories);
		if (categoryList == null || categoryList.size() < 1) {
			log
					.info("<FindAllProductsWithCategory>: category is null or illegal. categories="
							+ categories + ", city=" + city);
			resultCode = ErrorCode.ERROR_PARAMETER_CATEGORY_EMPTY;
		}

		List<Product> productList = ProductManager.getAllProductsWithCategory(
				mongoClient, city, categoryList, startOffset, maxCount);
		resultData = CommonServiceUtils.productListToJSONArray(productList);
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "FindAllProductsWithCateGory [appId=" + appId + ", categories="
				+ categories + ", city=" + city + ", maxCount=" + maxCount
				+ ", startOffset=" + startOffset + "]";
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		startOffset = request.getParameter(ServiceConstant.PRAR_START_OFFSET);
		city = request.getParameter(ServiceConstant.PARA_CITY);
		categories = request.getParameter(ServiceConstant.PARA_CATEGORIES);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL)) {
			return false;
		}
		if (!check(categories, ErrorCode.ERROR_PARAMETER_CATEGORY_EMPTY,
				ErrorCode.ERROR_PARAMETER_CATEGORY_NULL)) {
			return false;
		}
		return true;
	}

}
