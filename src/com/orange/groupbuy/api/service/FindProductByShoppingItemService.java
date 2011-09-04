package com.orange.groupbuy.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.RecommendItem;
import com.orange.groupbuy.manager.RecommendItemManager;

public class FindProductByShoppingItemService extends CommonGroupBuyService {

    private String itemId;
    private String userId;
    private String appId;


    @Override
    public String toString() {
        return "FindProductByShoppingListService [itemId=" + itemId + ", userId=" + userId + ", appId=" + appId + "]";
    }

    @Override
    public boolean setDataFromRequest(HttpServletRequest request) {
        userId = request.getParameter(ServiceConstant.PARA_USERID);
        itemId = request.getParameter(ServiceConstant.PARA_ITEMID);
        appId = request.getParameter(ServiceConstant.PARA_APPID);

        if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL)) {
            return false;
        }
        if (!check(itemId, ErrorCode.ERROR_PARAMETER_ITEMID_EMPTY, ErrorCode.ERROR_PARAMETER_ITEMID_NULL)) {
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
        RecommendItem item = RecommendItemManager.findRecommendItem(mongoClient, userId, itemId);
        List<Product> productList = null;
        if (item != null) {
            productList = RecommendItemManager.getRecommendProducts(mongoClient, item);
            productList = RecommendItemManager.sortRecommendProducts(productList);
        }
        if(productList == null) {
            log.info("userId= " + userId + "itemId=" + itemId + "can't find any product.");
            return;
        }
        for (Product p : productList) {
            log.info("product id=" + p.getId() + ", score=" + p.getScore() + ", title=" + p.getTitle());
        }

        resultData = CommonServiceUtils.productListToJSONArray(productList);
    }

}
