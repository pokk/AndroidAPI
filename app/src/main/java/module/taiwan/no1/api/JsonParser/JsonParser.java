package module.taiwan.no1.api.JsonParser;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * JSON Parser by Jackson lib
 *
 * @author Jieyi Wu
 * @version 0.0.2
 * @since 2015/08/01
 */
public class JsonParser
{
    /**
     * Java object change to JSON format.
     *
     * @param data for JSON object
     */
    public static String getJsonString(Object data)
    {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try
        {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        }
        catch (JsonProcessingException e)
        {
            Log.e(TAG, String.valueOf(e));
        }

        return json;
    }

    /**
     * JSON string to JsonNode object.
     *
     * @param str JSON string
     */
    public static JsonNode getJsonNode(String str)
    {
        if (str == null || str.isEmpty())
        {
            return null;
        }

        try
        {
            return new ObjectMapper().readTree(str);
        }
        catch (IOException e)
        {
            Log.e(TAG, String.valueOf(e));
        }

        return null;
    }

    /**
     * From parent Node get the JSON string value.
     *
     * @param str      JSON string
     * @param key      JSON key
     * @param trimChar trim character (if null, just ignore)
     * @return get the JSON value of the key which is assigned
     */
    public static Object getJsonPath(String str, String key, String trimChar)
    {
        if (str == null || str.isEmpty())
        {
            return null;
        }
        else if (key == null || key.isEmpty())
        {
            return null;
        }

        try
        {
            if (trimChar == null || trimChar.isEmpty())
            {
                return new ObjectMapper().readTree(str).path(key).toString();
            }
            else
            {
                return new ObjectMapper().readTree(str).path(key).toString().replace(trimChar, "");
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, String.valueOf(e));
        }

        return null;
    }

    /**
     * JSON string to an object.
     *
     * @param json JSON string
     * @param cls  class name
     * @return POJO object
     */
    public static Object getJson2Obj(String json, Class<?> cls)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.JAPAN);
        mapper.setDateFormat(dateFormat);
        Object pojo = new Object();

        try
        {
            pojo = mapper.readValue(json, cls);
        }
        catch (IOException e)
        {
            Log.e(TAG, String.valueOf(e));
            pojo = null;
        }

        return pojo;
    }

    /**
     * Get multi objects from JSON string.
     *
     * @param json JSON string
     * @param cls  class name
     * @return multi objects array list
     */
    public static ArrayList<Object> getJson2List(String json, Class<?> cls)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.JAPAN);
        mapper.setDateFormat(dateFormat);
        ArrayList<Object> pojoList = new ArrayList<Object>();

        try
        {
            pojoList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, cls));
        }
        catch (IOException e)
        {
            Log.e(TAG, String.valueOf(e));
            pojoList = null;
        }

        return pojoList;
    }

    /**
     * JSON string to a map data type.
     *
     * @param json JSON string
     * @return map data type
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getJson2Map(String json)
    {
        HashMap<String, String> result = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            result = mapper.readValue(json, HashMap.class);
        }
        catch (IOException e)
        {
            Log.e(TAG, String.valueOf(e));
            result = null;
        }

        return result;
    }

    /**
     * JSON string to multi map
     *
     * @param str JSON string
     * @return map data type of the listing
     */
    public static LinkedHashMap<Integer, HashMap<String, String>> getJson2LinkedMap(String str)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.JAPAN);
        mapper.setDateFormat(dateFormat);
        LinkedHashMap<Integer, HashMap<String, String>> dataList = new LinkedHashMap<Integer, HashMap<String, String>>();
        HashMap<String, String> jsonMap = null;
        JSONArray jArray;
        JSONObject[] jObj;

        try
        {
            jArray = new JSONArray(str);
            int maxCnt = jArray.length();
            jObj = new JSONObject[maxCnt];
            for (int cnt = 0 ; cnt < maxCnt ; cnt++)
            {
                jObj[cnt] = jArray.getJSONObject(cnt);
                jsonMap = mapper.readValue(jObj[cnt].toString(), HashMap.class);
                dataList.put(cnt, jsonMap);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            dataList = null;
        }
        catch (IOException e)
        {
            Log.e(TAG, String.valueOf(e));
            dataList = null;
        }

        return dataList;
    }
}