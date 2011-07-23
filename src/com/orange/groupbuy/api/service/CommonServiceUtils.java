package com.orange.groupbuy.api.service;

import java.util.List;
import java.util.Map;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.ServiceConstant;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.User;

public class CommonServiceUtils {

	public static JSONObject userToJSON(DBObject user) {
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, user.get(MongoDBClient.ID));
		return obj;
	}
	
	public static JSONArray productListToJSONArray(List<Product> products){
		JSONArray jsonArray = new JSONArray();
		for(Product product : products){
			JSONObject object = product.toJsonObject();
			jsonArray.add(object);
		}
		return jsonArray;
	}

}
