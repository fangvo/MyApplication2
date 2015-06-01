package com.fangvo.myapplication2.app;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

// клас для хранения даных

public class MyData {

    public static Map<String,GoodsData> data;// = new HashMap<String, GoodsData>();

    public static JSONObject priceList;// = new JSONArray();

    public static List<String> clientsName;

    public static Integer ifOfNextSell;

    public static String name;

    public static Boolean isLoaded = false;

}
