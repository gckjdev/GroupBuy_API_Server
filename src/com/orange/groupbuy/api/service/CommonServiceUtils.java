package com.orange.groupbuy.api.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.User;

public class CommonServiceUtils {

	public static JSONObject userToJSON(DBObject user) {
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, user.get(MongoDBClient.ID));
		return obj;
	}

}
