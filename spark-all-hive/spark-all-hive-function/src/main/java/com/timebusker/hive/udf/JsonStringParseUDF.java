package com.timebusker.hive.udf;


import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description:JsonStringParseUDF:JSON解析器
 * @Author:Administrator
 * @Date2019/10/20 10:04
 **/

/**
 * 便捷UDF函数详细信息
 * 使用 DESCRIBE FUNCTION EXTENDED udf_json_parse 可查看;
 */
@Description(name = "udf_json_parse",
        value = "_FUNC_(jsonString,key) - valid the jsonString, if like return 1 else return 0",
        extended = "Example:\n > SELECT _FUNC_('jsonString', 'key') FROM src LIMIT 1; \n  'value'")
public class JsonStringParseUDF extends UDF {

    public static String evaluate(String jsonStr, String key) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        evaluate("{\"movie\":\"1721\",\"rate\":\"4\",\"timeStamp\":\"978300055\",\"uid\":\"1\"}", "movie");
    }
}
