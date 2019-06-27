package com.yunkang.chat.tools;


import android.text.TextUtils;

import com.azhon.appupdate.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * gson 解析工具
 *
 * @author Elan 15.3.31
 */
public class GsonUtils {


    public static Gson gson = new Gson();

    public static <T> T deSerializedFromJson(String json, Class<T> clazz) throws JsonSyntaxException {
        return gson.fromJson(json, clazz);
    }

    public static <T> T deSerializedFromJson(String json, Type type) throws JsonSyntaxException {
        return gson.fromJson(json, type);
    }

    public static String serializedToJson(Object object) {
        if (object != null) {
            return gson.toJson(object);
        } else {
            return "";
        }
    }

    /**
     * 获取JsonObject
     *
     * @return JsonObject
     */
    public static JsonObject parseJson(String json) {
        JsonObject jsonObj = null;
        try {
            JsonParser parser = new JsonParser();
            jsonObj = parser.parse(json).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            LogUtil.e("frank", "parseJson Exception===" + e.toString());
        }
        return jsonObj;
    }

    /**
     * json字符串转成Bean对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T jsonToBean(String str, Class<T> type) {
        Gson gson = new Gson();
        try {
            T t = gson.fromJson(str, type);
            return t;
        } catch (JsonSyntaxException e) {
            LogUtil.e("frank", "jsonToBean Exception===" + e.toString()+"\n"+str);

        }
        return null;
    }
    public static <T> T jsonToBeanFromData(String str, Class<T> type) {
        return jsonToBean(str,"datas",type);
    }

    public static <T> T jsonToBean(String str, String key, Class<T> type) {
        String data = getStringFromJSON(str,key);
        if(data==null){
            return null;
        }
        Gson gson = new Gson();
        try {
            T t = gson.fromJson(data, type);
            return t;
        } catch (JsonSyntaxException e) {
            LogUtil.e("frank", "jsonToBean Exception===" + e.toString()+"\n"+str);

        }
        return null;
    }


    public static String getStringFromJSON(String json, String key1, String key2) {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(json).getJSONObject(key1);
            data = jsonObject.getString(key2);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e("frank", "getStringFromJSON Exception===" + e.toString());
        }
        return data;
    }


    public static long getLongFormJSON(String json, String key1, String key2) {
        long data = 0;
        try {
            JSONObject jsonObject = new JSONObject(json).getJSONObject(key1);
            data = jsonObject.getLong(key2);

        } catch (JSONException e) {
            LogUtil.e("frank", "getLongFormJSON Exception===" + e.toString());
        }
        return data;
    }

    /**
     * @param json
     * @param key1
     * @param key2
     * @return
     */
    // 现有逻辑有问题 ：更新包判断
    public static boolean getBooleanFormJSON(String json, String key1, String key2) {
        boolean data = true;
        try {
            JSONObject jsonObject = new JSONObject(json).getJSONObject(key1);
            data = jsonObject.getBoolean(key2);
        } catch (JSONException e) {
            LogUtil.e("frank", "getBooleanFormJSON Exception===" + e.toString());
        }
        return data;
    }
    public static Map<String,String> jsonToBeanMap(String json){
        Map<String,String> map;
        if (json == null|| TextUtils.isEmpty(json)){
            return new HashMap<String, String>();
        }else { //json data 字段不为空
            map = gson.fromJson(json,new TypeToken<Map<String, String>>() {}.getType());
            if (map!=null){
                return map;
            }
        }
        return new HashMap<String,String>();
    }

    public static <T, P> Map<T, P> jsonToBeanMap(String json, Type tClass) {
        Map<T, P> map;
        if (json == null || TextUtils.isEmpty(json)) {
            return new HashMap<T, P>();
        } else { //json data 字段不为空
            map = gson.fromJson(json, tClass);
            if (map != null) {
                return map;
            }
        }
        return new HashMap<T, P>();
    }

    /**
     * 根据key获取json object 的value
     *
     * @param json
     * @param key
     * @return
     */
    public static boolean getBooleanFormJSON(String json, String key) {
        boolean data = false;
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.getBoolean(key);
        } catch (JSONException e) {
            LogUtil.e("frank", "getBooleanFormJSON Exception===" + e.toString()+"\njson="+json+"\nkey="+key);
        }
        return data;
    }


    /**
     * 从JSON字符串提取出对应 Key的 字符串
     *
     * @param json
     * @param key
     * @return
     */
    public static String getStringFromJSON(String json, String key) {
        String data = null;
        if(json==null)return null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.getString(key);
        } catch (JSONException e) {
            LogUtil.e("frank", "getStringFromJSON Exception==="+"\n"+ e.toString()+"\n【key="+key+"】json="+json);
        }
        return data;
    }


    public static int getIntFromJSON(JSONObject obj, String key) {
        int data = 0;
        try {
            if (obj == null) {
                return data;
            }
            data = obj.getInt(key);
        } catch (JSONException e) {
            LogUtil.e("frank", "getIntFromJSON Exception===" + e.toString());
        }
        return data;
    }


    public static int getIntFromJSON(String json, String key) {
        int data = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.getInt(key);
        } catch (JSONException e) {
            LogUtil.e("frank", "getIntFromJSON Exception===" + e.toString());
        }
        return data;
    }
    public static double getDoubleFromJSON(String json, String key) {
        double data = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.getDouble(key);
        } catch (JSONException e) {
            LogUtil.e("frank", "getIntFromJSON Exception===" + e.toString());
        }
        return data;
    }

    public static int getIntFromJSON(String json, String key1, String key2) {
        int data = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String dataJson = jsonObject.getString(key1);
            data = getIntFromJSON(dataJson, key2);
        } catch (JSONException e) {
            LogUtil.e("frank", "getIntFromJSON Exception===" + e.toString());
        }
        return data;
    }

    public static long getLongFromJSON(String json, String key) {
        long data = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.getLong(key);
        } catch (JSONException e) {
            LogUtil.e("frank", "getLongFromJSON Exception===" + e.toString());
        }
        return data;
    }

    public static long getLongFromJSON(String json, String key1, String key2) {
        long data = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String dataJson = jsonObject.getString(key1);
            data = getLongFromJSON(dataJson, key2);
        } catch (JSONException e) {
            LogUtil.e("frank", "getLongFromJSON Exception===" + e.toString());
        }
        return data;
    }

    public static <T> List<T> jsonToBeanList(String json, Type tClass) {
        List<T> list;
        if (json == null || TextUtils.isEmpty(json)) { // json data 字段为空
            return new ArrayList<T>();
        } else { //json data 字段不为空
            try {
                list = gson.fromJson(json, tClass);
                if (list != null) {
                    return list;
                }
            } catch (JsonSyntaxException e) {
                LogUtil.e("frank", "jsonToBeanList_2 Exception ===" + e.toString());
            }
        }
        return new ArrayList<T>();
    }

    /**
     * 3.0
     * 从data里解析出对象数组  key1 一般为 : data  key2 ： your key
     *
     * @param json
     * @param tClass
     * @return list<T>  数据异常返回  list (size is 0)
     */
    public static <T> List<T> jsonToBeanList(String json, String key1, String key2, Type tClass) {
        List<T> list;
        String data = getStringFromJSON(json, key1, key2);
        if (data == null || TextUtils.isEmpty(data)) { // json data 字段为空
            return new ArrayList<T>();
        } else { //json data 字段不为空
            try {
                list = gson.fromJson(data, tClass);
                if (list != null) {
                    return list;
                }
            } catch (JsonSyntaxException je) {
                LogUtil.e("frank", "jsonToBeanList Exception===" + je.toString());
            }
        }
        return new ArrayList<T>();
    }

}
