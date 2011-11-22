package com.orange.groupbuy.api;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;

public class GroupBuyCreateAdData {

	private static final MongoDBClient mongoClient = new MongoDBClient(DBConstants.D_GROUPBUY);	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Date startDate = new Date();
		Date endDate = DateUtils.addDays(startDate, 30);
		
		Product product1 = new Product();
		product1.setMandantoryFields(DBConstants.C_NATIONWIDE, 
				"http://cl.mbaobao.com/?url=http://mkt.mbaobao.com/a-quality1001/&cps_cpid=1249&cps_adid=493", 
				"http://res.mbbimg.cn/union/201110/11/336x280.gif", 
				"珍稀之美惊羡腕间！麦包包秋冬奢侈精品手袋诚邀鉴赏精品馆！米兰设计美学与浓郁意式品位渗透，精致牛皮勾勒美妙立体剪影，蛇纹质感长城刺绣东西方文化…秋冬精品手袋诚邀鉴赏 @麦包包麦友团", 
				startDate, endDate, DBConstants.C_PRICE_NA, DBConstants.C_PRICE_NA, 
				0, "maibaobao", 
				"麦包包", "http://m.mbaobao.com/?&cps_cpid=1249&cps_adid=493");
		product1.setProductType(DBConstants.C_PRODUCT_TYPE_AD);
		
		Product product2 = new Product();
		product2.setMandantoryFields(DBConstants.C_NATIONWIDE, 
				"http://market.tuniu.com/Partner_redirect.php?url=http://chujing.tuniu.com/gangaotai/&p=9623&cmpid=union_9623", 
				"http://www.tuniu.com/icons/pic_adv/ef76b56808e3167230058950406db89420100628095741.jpg", 
				"购物者的天堂，港澳旅游乐翻天，特价中！", 
				startDate, endDate, DBConstants.C_PRICE_NA, DBConstants.C_PRICE_NA, 
				0, "tuniu", 
				"途牛旅游", "http://m.tuniu.com/?&cmpid=union_9623");
		product2.setProductType(DBConstants.C_PRODUCT_TYPE_AD);

		Product product3 = new Product();
		product3.setMandantoryFields(DBConstants.C_NATIONWIDE, 
				"http://www.letao.com/lianmeng/manage/linkImage.aspx?wh=300&ht=250&wid=82&sid=lingzhe", 
				"http://www.letao.com/lianmeng/manage/linkImage.aspx?wh=300&ht=250&wid=82", 
				"REKLiM时尚潮鞋，特价99元", 
				startDate, endDate, DBConstants.C_PRICE_NA, DBConstants.C_PRICE_NA, 
				0, "letao", 
				"乐淘", "http://m.letao.com/?&sid=lingzhe");
		product3.setProductType(DBConstants.C_PRODUCT_TYPE_AD);

		
		ProductManager.createProduct(mongoClient, product1);
		ProductManager.createProduct(mongoClient, product2);
		ProductManager.createProduct(mongoClient, product3);
	}

}
