package com.mytest.utils.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class JsonUtils {

	private JsonUtils() {

	}

	private static final Gson GSON = new Gson();

	private static final Type mapType = new TypeToken<Map<String, Object>>() {	}.getType();

	private static final Type listType = new TypeToken<List<String>>() {	}.getType();

	public static String toJson(Map<String, String> map) {
		return GSON.toJson(map);
	}

	public static String toJson(List<?> list) {
		return GSON.toJson(list);
	}

	public static List<String> getListFromJson(String json) {
		return GSON.fromJson(json, listType);
	}

	public static String toJson(Object list, Type t) {
		return GSON.toJson(list, t);
	}

	public static Map<String, Object> getMapFromJson(String json) {
		if (json == null) {
			return null;
		}
		return GSON.fromJson(json, mapType);
	}
	
	public static String toJson(Object src) {
		return GSON.toJson(src);
	}
}