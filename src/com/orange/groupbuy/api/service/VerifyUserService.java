package com.orange.groupbuy.api.service;

import javax.servlet.http.HttpServletRequest;

import com.mongodb.BasicDBObject;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.manager.UserManager;

public class VerifyUserService extends CommonGroupBuyService{
	
	String vcd;

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		vcd = request.getParameter(ServiceConstant.PARA_VERIFYCODE);
		
		if (!check(vcd, ErrorCode.ERROR_PARAMETER_VERIFYCODE_EMPTY, ErrorCode.ERROR_PARAMETER_VERIFYCODE_NULL))
			return false;
		
		return true;
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public void handleData() {
		
		BasicDBObject user = (BasicDBObject) UserManager.findUserByVerifyCode(mongoClient, vcd);
				
		if (user == null){
			log.info("<verifyUser> verifyCode("+vcd+") not found");
			resultCode = ErrorCode.ERROR_USERID_NOT_FOUND;
			return;
		}
		else{
			//Already active
			if (user.getString(DBConstants.F_STATUS).equals(DBConstants.STATUS_NORMAL)){
				log.info("<verifyUser> verifyCode("+vcd+") has been verified");
				resultCode = ErrorCode.ERROR_EMAIL_VERIFIED;
				return;
			}
			//Active now
			else if (user.getString(DBConstants.F_STATUS).equals(DBConstants.STATUS_TO_VERIFY)){
				UserManager.updateStatusByVerifyCode(mongoClient,DBConstants.STATUS_NORMAL,vcd);
			}
		}
	}

}
