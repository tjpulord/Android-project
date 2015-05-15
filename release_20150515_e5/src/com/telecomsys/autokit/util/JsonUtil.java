package com.telecomsys.autokit.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	public interface JSONSerializer{
		public JSONObject toJSON();
	}
	
	public static String ListToJSON(List<? extends JSONSerializer> lists){
		JSONArray obj = new JSONArray();
		if(lists == null || lists.size() == 0){
			Log.d("listtojson:", "empty json array:"+obj.toString());
			return obj.toString();
		}
		else{
			for(JSONSerializer o: lists){
				obj.put(o.toJSON());
			}
			return obj.toString();
		}
	}
	
    public static String packJSONStr(String str) {
        if (str == null)
            return str;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); ++i)
        {
            if (str.charAt(i) == '\\')
            {
                sb.append('\\');
            }
            else if (str.charAt(i) == '"')
            {
                sb.append('\\');
            }
            else if (str.charAt(i) == '\n')
            {
                sb.append("\\n");
                continue;
            }
            else if (str.charAt(i) == '\r')
            {
                sb.append("\\r");
                continue;
            }
            else if (str.charAt(i) == '\t')
            {
                sb.append("\\t");
                continue;
            }
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }

    public static String unpackJSONStr(String str) {
        if (str == null)
            return str;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '\\')
            {
                if (i >= str.length() - 1) {
                    //not enough space to escape, error
                    //outputLog("Error in autokit messagebody:illegal slashes in the end of message:%d\n", i);
                    break;
                }
                char c = str.charAt(i+1);
                if (c == '\\')
                {
                    sb.append('\\');
                    i += 1;
                }
                else if (c == '"')
                {
                    sb.append('"');
                    i += 1;
                }
                else if (c == 'n')
                {
                    sb.append('\n');
                    i += 1;
                }
                else if (c == 'r')
                {
                    sb.append('\r');
                    i += 1;
                }
                else if (c == 't')
                {
                    sb.append('\t');
                    i += 1;
                }
                else if (c == '/') {
					sb.append('/');
					i += 1;
				}
                else
                {
                    //outputLog("Error in autokit messagebody:illegal character after slashes:%d\n", i);
                    break;
                }
            }
            else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }
}
