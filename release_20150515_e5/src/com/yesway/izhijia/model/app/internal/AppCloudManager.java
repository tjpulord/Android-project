package com.yesway.izhijia.model.app.internal;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yesway.izhijia.model.user.UserInfo;

public class AppCloudManager {
	private static final String requestHost = "http://111.207.170.16:9900";
	private static final String clsslistURI = "/system/ycarplus/getappclasslist";
	private static final String applistURI = "/system/ycarplus/getapplist";
	private static final String userapplistURI = "/system/ycarplus/getuserapplist";
	private static final String adduserappURI = "/system/ycarplus/adduserapp";
	private static final String removeuserappURI = "/system/ycarplus/removeuserapp";
	
	private String sessionID;
	private String zjid;
	
	private static AppCloudManager appCloudManager = new AppCloudManager();
	public static AppCloudManager getInstance(){
		return appCloudManager;
	}
	
	private static JSONObject formNtspHeader(String zjid, String sessionid){
		JSONObject j = new JSONObject();

		try {
			j.put("apikey", "8970b2bc-4f69-467c-a7ff-c391d53035f9");
			j.put("version", "1.0");
			j.put("sessionid", sessionid);
			j.put("zjid", zjid);
			j.put("productid", "-1");
			j.put("corpid", "1001");
			j.put("errcode", "0");
			j.put("errmsg", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return j;
	}
	
	private AppClass[] parseAppClass(String result) {
		// TODO Auto-generated method stub
		JSONObject jObject;
		AppClass appList[];
		try {
			jObject = new JSONObject(result);
			if(jObject.getJSONObject("ntspheader").getInt("errcode") != 0)
			{
				return null;
			}
			JSONArray jsonArray = jObject.getJSONArray("appclasslist");
			appList = new AppClass[jsonArray.length()];
			Log.d("appcloud manager", appList.toString());
			// set each oject in the AppClass list
			for (int i=0; i<jsonArray.length(); i++){
				JSONObject temp = (JSONObject) jsonArray.get(i);
				appList[i].setAddTime(temp.getString("addtime"));
				appList[i].setClassName(temp.getString("classname"));
				appList[i].setId(temp.getString("id"));
				appList[i].setLevel(temp.getInt("level"));
				appList[i].setOrderID(temp.getInt("orderid"));
				appList[i].setParentID(temp.getString("parentid"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return appList;
	}
	private Application[] parseApplications(String result) {
		// TODO Auto-generated method stub
		JSONObject jObject;
		Application appList[];
		try {
			jObject = new JSONObject(result);
			if(jObject.getJSONObject("ntspheader").getInt("errcode") != 0)
			{
				return null;
			}
			JSONArray jsonArray = jObject.getJSONArray("appclasslist");
			appList = new Application[jsonArray.length()];
			Log.d("appcloud manager", appList.toString());
			// set each oject in the AppClass list
			for (int i=0; i<jsonArray.length(); i++){
				JSONObject temp = (JSONObject) jsonArray.get(i);
				appList[i].setAddTime(temp.getString("addtime"));
				appList[i].setAppName(temp.getString("appname"));
				appList[i].setAppTitleName(temp.getString("apptitlename"));
				appList[i].setAppType(temp.getInt("apptype"));
				appList[i].setBestNewVersion(temp.getString("bestnewversion"));
				appList[i].setClassID(temp.getString("classid"));
				appList[i].setDownloadURL(temp.getString("downoadurl"));
				appList[i].setId(temp.getString("appkey"));
				appList[i].setIsBuiltin(temp.getInt("isbuiltin"));
				appList[i].setIsCarLink(temp.getInt("iscarlink"));
				appList[i].setIsDownload(temp.getInt("isdownload"));
				appList[i].setIsMustNet(temp.getInt("ismustnet"));
				appList[i].setLowerUpgradeVersion(temp.getString("lowerupgradeversion"));
				appList[i].setOrderID(temp.getInt("orderid"));
				appList[i].setPic1Height(temp.getString("pic1height"));
				appList[i].setPic1Width(temp.getString("pic1width"));
				appList[i].setPic1URL(temp.getString("pic1url"));
				appList[i].setPic2Height(temp.getString("pic2height"));
				appList[i].setPic2Width(temp.getString("pic2width"));
				appList[i].setPic2URL(temp.getString("pic2url"));
				appList[i].setSize(temp.getString("size"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return appList;
	}
	
	public AppClass[] getAppClassList(){
		JSONObject o = formNtspHeader("","");
		JSONObject main = new JSONObject();
		try {
			main.put("ntspheader", o);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String result = requestHTTP(requestHost+clsslistURI, main.toString());
		return parseAppClass(result);
	}
	
	public Application[] getApplicationList(){
		JSONObject o = formNtspHeader("", "");
		JSONObject main = new JSONObject();
		try {
			main.put("ntspheader", o);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String result = requestHTTP(requestHost+applistURI, main.toString());
		return parseApplications(result);
	}

	private String requestHTTP(String urlStr, String body) {

		byte[] buffer = new byte[32 * 1024];
		HttpURLConnection conn = null;
		String result = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			//è®¾ç½® URLConnection#setDoOutput() ä¸º true ï¼Œéš�æ€§è®¾ç½®è¯·æ±‚æ–¹æ³•ä¸º POSTã€‚ æ ‡å‡†çš„ HTTP POST æ˜¯ä¸€ç§� application/x-www-form-urlencoded ç±»åž‹çš„ç½‘ç»œè¡¨å�•ï¼Œä¼ é€’çš„å�‚æ•°éƒ½ä¼šè¢«å†™å…¥è¯·æ±‚ä¿¡æ�¯ä¸»ä½“ä¸­ã€‚
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.addRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.addRequestProperty("Content-Length", Integer.toString(body.length()));
			
			OutputStream output = null;
			try {
			     output = conn.getOutputStream();
			     output.write(body.getBytes());
			     output.flush();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally {
			     if (output != null)
			     {
			         try { 
			             output.close(); 
			         } 
			         catch (IOException logOrIgnore) {
			        	 logOrIgnore.printStackTrace();
			         }
			     }
			}

			//conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());   

			ByteArrayOutputStream boutput = new ByteArrayOutputStream();
			
			//System.out.println(conn.getContentLength() + " " + conn.getResponseCode());

			int s = 0;
			int t = 0;
			int count = 0;
			while ((s = bis.read(buffer)) != -1) {
		        //boutput.write(buffer);
				boutput.write(buffer, 0, s);
		        t += s;
		        count++;
			}
			boutput.flush();
			result = boutput.toString();
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}
}
