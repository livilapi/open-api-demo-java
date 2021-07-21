package com.paybank.javademo.modular.demo;

import com.paybank.javademo.modular.demo.util.HttpUtil;
import com.paybank.javademo.modular.demo.util.SignUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class Main {

	private static final String APP_KEY = "Your Merchant AppKey";
	private static final String APP_SECRET = "Your Merchant AppSecret";
	private static final String COIN_LIST_REQUEST_URL = "https://openapi.payurl.app/pay/order/coin/list";
	private static final String CREATE_ORDER_REQUEST_URL = "https://openapi.payurl.app/pay/order/create";

	public static String rechargeCoinList(String key, String secret) {
		Map<String, Object> message = new HashMap<>(10);
		message.put("appKey", key);
		message.put("ts", System.currentTimeMillis() / 1000);
		message.put("sign", SignUtil.getSign(message, secret));

		String resultJson = null;
		try {
			resultJson = HttpUtil.doPostJson(COIN_LIST_REQUEST_URL, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}

	public static String createPayOrder(String key, String secret) {
		Map<String, Object> message = new HashMap<>();
		message.put("appKey", key);
		message.put("ts", System.currentTimeMillis() / 1000);
		message.put("createType", 1);
		message.put("currencyType", 1);
		message.put("amount", 100);
//        message.put("createType", 2);
//        message.put("coinId", 1);
//        message.put("coinNum", 10);
//        message.put("createType", 3);
//        message.put("coinNum", 100);
		message.put("merchantOrderNum", "" + System.currentTimeMillis());
		message.put("goodsName", "Test Order - " + RandomStringUtils.randomAlphanumeric(16));
		message.put("sign", SignUtil.getSign(message, secret));

		String resultJson = null;
		try {
			resultJson = HttpUtil.doPostJson(CREATE_ORDER_REQUEST_URL, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}

	public static void main(String[] args) {
		// Get Coin List
		System.out.println(rechargeCoinList(APP_KEY, APP_SECRET));
		// Create Pay Order
//		System.out.println(createPayOrder(APP_KEY, APP_SECRET));
	}

}
