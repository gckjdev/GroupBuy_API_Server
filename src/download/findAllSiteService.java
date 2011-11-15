package download;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Site;
import com.orange.groupbuy.manager.SiteManager;

public class findAllSiteService extends CommonGroupBuyService {

	String appId;
	String countryCode;
	int siteType = DBConstants.UNDEFINE;							// optional
	int sortBy = DBConstants.UNDEFINE;								// optional
	
	int startOffset = 0;							// optional
	int maxCount = 30;								// optional
	
	@Override
	public String toString() {
		return "findAllSiteService [appId=" + appId + ", countryCode="
				+ countryCode + ", maxCount=" + maxCount + ", startOffset="
				+ startOffset + "]";
	}

	@Override
	public void handleData() {		
		List<Site> siteList = SiteManager.findAllSites(mongoClient, countryCode, siteType, sortBy, startOffset, maxCount);
		resultData = CommonServiceUtils.siteListToJSONObject(siteList);
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		countryCode = request.getParameter(ServiceConstant.PARA_COUNTRYCODE);
		String siteTypeStr = request.getParameter(ServiceConstant.PARA_TYPE);
		String sortByStr = request.getParameter(ServiceConstant.PARA_SORT_BY);
		
		if (!StringUtil.isEmpty(siteTypeStr)){
			siteType = Integer.parseInt(siteTypeStr);
		}
		
		if (!StringUtil.isEmpty(sortByStr)){
			sortBy = Integer.parseInt(sortByStr);
		}
				
		return true;
	}

}
