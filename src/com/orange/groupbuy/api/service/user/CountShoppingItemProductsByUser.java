package com.orange.groupbuy.api.service.user;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.UserManager;

public class CountShoppingItemProductsByUser  extends CommonGroupBuyService {

    private String appId;
    private String userId;
    
    public boolean setDataFromRequest(HttpServletRequest request) {
        userId = request.getParameter(ServiceConstant.PARA_USERID);
        appId = request.getParameter(ServiceConstant.PARA_APPID);

        if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL)) {
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
        
        Map<String, Integer> map = UserManager.getUserRecommendItemCountMap(mongoClient, userId);
        if (map == null) {
            return;
        }
        resultData = CommonServiceUtils.userShoppingItemProductCountToJSONArray(map);
        
    }

}
