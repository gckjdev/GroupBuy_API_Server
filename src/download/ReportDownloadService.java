package download;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.SiteManager;
import com.orange.groupbuy.manager.TopDownloadManager;

public class ReportDownloadService extends CommonGroupBuyService {

	String appId;
	String deviceId;
	String countryCode;
	String language;
	String fileType;
	String fileURL;
	int    fileSize;
	String fileName;
	String siteURL;
	String siteName;
	
	
	
	@Override
	public String toString() {
		return "ReportDownloadService [appId=" + appId + ", countryCode="
				+ countryCode + ", deviceId=" + deviceId + ", fileName="
				+ fileName + ", fileSize=" + fileSize + ", fileType="
				+ fileType + ", fileURL=" + fileURL + ", language=" + language
				+ ", siteName=" + siteName + ", siteURL=" + siteURL + "]";
	}

	@Override
	public void handleData() {
		TopDownloadManager.createOrUpdateTopDownload(mongoClient, appId, deviceId, countryCode, language,
				fileType, fileURL, fileSize, fileName, siteURL, siteName);
		
		SiteManager.createOrUpdateSiteDownload(mongoClient, siteURL, siteName, countryCode, fileType);
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		deviceId = request.getParameter(ServiceConstant.PARA_DEVICEID);
		countryCode = request.getParameter(ServiceConstant.PARA_COUNTRYCODE);
		language = request.getParameter(ServiceConstant.PARA_LANGUAGE);
		fileName = request.getParameter(ServiceConstant.PARA_FILE_NAME);
		fileType = request.getParameter(ServiceConstant.PARA_FILE_TYPE);
		fileURL = request.getParameter(ServiceConstant.PARA_FILE_URL);
		String fileSizeStr = request.getParameter(ServiceConstant.PARA_FILE_SIZE);
		siteURL = request.getParameter(ServiceConstant.PARA_SITE_URL);
		siteName = request.getParameter(ServiceConstant.PARA_SITE_NAME);
		
		fileSize = Integer.parseInt(fileSizeStr);
		
		if (StringUtil.isEmpty(fileURL) || StringUtil.isEmpty(fileName) ||
				StringUtil.isEmpty(siteURL) || StringUtil.isEmpty(fileSizeStr)){
			log.warn("<ReportDownloadService> but lack of enough parameters, data = "+this.toString());
			return false;
		}		
		return true;
	}

}
