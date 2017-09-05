package com.hlj.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil<T> {

	private static Gson gson = new GsonBuilder().create();

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return (T) gson.fromJson(json, clazz);
	}

}
