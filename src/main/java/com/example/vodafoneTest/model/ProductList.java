package com.example.vodafoneTest.model;

import java.util.HashMap;
import java.util.Map;

public class ProductList {

	
	private static Map<String, String> productList = null;
	
	static {
		
		productList = new HashMap<String,String>();
		productList.put("WG", "CyclePlusTracker");
		productList.put("69", "GeneralTracker");
	}
		
	
	public static String getProductType(String productId) {
		
		String key = productId.substring(0, 2);
		String deviceType = productList.get(key);
		
		if(deviceType == null || deviceType.isEmpty()) {
			return "Unknown";
		}
		
		return deviceType;
	}



}
