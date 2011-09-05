package com.orange.groupbuy.api.service;

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
    private String maxCount;            
    private String startOffset;



    @Override
    public String toString() {
        return "FindProductByShoppingListService [itemId=" + itemId + ", userId=" + userId + ", appId=" + appId + "]";
    }

    @Override
    public boolean setDataFromRequest(HttpServletRequest request) {
        userId = request.getParameter(ServiceConstant.PARA_USERID);
        itemId = request.getParameter(ServiceConstant.PARA_ITEMID);
        appId = request.getParameter(ServiceConstant.PARA_APPID);
        maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
        startOffset = request.getParameter(ServiceConstant.PRAR_START_OFFSET);


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
        List<Product> subList = null;
        if (item != null) {
            productList = RecommendItemManager.getRecommendProducts(mongoClient, item);
            if(productList == null) {
                return;
            }
            productList = RecommendItemManager.sortRecommendProducts(productList);
            
            int count = getMaxcount(maxCount);
            int offset = getOffset(startOffset);
            int toIndex = count + offset;
            if(toIndex > productList.size()) {
                toIndex = productList.size();
            }
            subList = productList.subList(offset, toIndex);
        }
        if(subList == null) {
            log.info("userId= " + userId + "itemId=" + itemId + " can't find any product.");
            return;
        }
        for (Product p : subList) {
            log.info("product id=" + p.getId() + ", score=" + p.getScore() + ", title=" + p.getTitle());
        }

        resultData = CommonServiceUtils.productListToJSONArray(subList);
    }
    
    private int getOffset(String startOffset) {
        if (startOffset == null || startOffset.trim().length() < 1) {
            return 0;
        }
        return Integer.parseInt(startOffset);
    }
    
    private int getMaxcount(String maxCount) {
        if (maxCount == null || maxCount.trim().length() < 1) {
            return 5;
        }
        return Integer.parseInt(maxCount);
    }

}
