package common.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class JsonUtils {
    /**
     * simple method of reading a json path from a json string
     * this will cause the string to get parsed everytime and will be expensive
     * @param json
     * @param jsonPath
     * @return
     */
    public static Object readPath(String json, String jsonPath){
        return JsonPath.read(json, jsonPath);
    }

    /**
     * get a parsed document of some json string - use along with #readPathFromParsedJson
     * @param json
     * @return
     */
    public static Object getParsedJson(String json){
        return Configuration.defaultConfiguration().jsonProvider().parse(json);
    }


    public static Object readPathFromParsedJson(Object parsed, String path){
        return JsonPath.read(parsed, path);
    }


    public static Object readPathWithDefault(String json, String jsonPath, Object defaultValue){
        try {
            return readPath(json, jsonPath);
        } catch (PathNotFoundException e) {
            return defaultValue;
        }
    }


    public static Object readPathFromParsedJsonWithDefault(Object parsed, String jsonPath, Object defaultValue){
        try {
            return readPathFromParsedJson(parsed, jsonPath);
        } catch (PathNotFoundException e) {
            return defaultValue;
        }
    }

}

