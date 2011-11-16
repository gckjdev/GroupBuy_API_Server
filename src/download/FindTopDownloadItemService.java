package download;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.api.service.CommonServiceUtils;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.TopDownload;
import com.orange.groupbuy.manager.TopDownloadManager;

public class FindTopDownloadItemService extends CommonGroupBuyService {

	String appId;
	String countryCode;
	
	int startOffset = 0;							// optional
	int maxCount = 30;								// optional
	
	@Override
	public String toString() {
		return "FindAllTopDownloadItemService [appId=" + appId + ", countryCode="
				+ countryCode + ", maxCount=" + maxCount + ", startOffset="
				+ startOffset + "]";
	}
	
	@Override
	public void handleData() {
		List<TopDownload> topDownloadList = TopDownloadManager.findAllTopDownloadItems(mongoClient, countryCode, startOffset, maxCount);
		resultData = CommonServiceUtils.topDownloadItemListToJSONObject(topDownloadList);
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		countryCode = request.getParameter(ServiceConstant.PARA_COUNTRYCODE);
		String startOffsetStr = request.getParameter(ServiceConstant.PRAR_START_OFFSET);
		if (!StringUtil.isEmpty(startOffsetStr)){
			startOffset = Integer.parseInt(startOffsetStr);
		}		

		String maxCountStr = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		if (!StringUtil.isEmpty(maxCountStr)){
			maxCount = Integer.parseInt(maxCountStr);
		}	
		
		return true;
	}

}
