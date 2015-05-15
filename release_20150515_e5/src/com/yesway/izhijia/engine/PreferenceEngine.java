package com.yesway.izhijia.engine;


import java.util.ArrayList;

import com.yesway.izhijia.model.app.internal.AppManager.AppListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceEngine {
	private Context appContext;
	private static final String PREF_FILE = "pref_setting";
	
	public PreferenceEngine(Context ctx){
		if(ctx == null){
			throw new IllegalArgumentException("Context is required for preference engine"); 
		}
		appContext = ctx.getApplicationContext();
	}
	
	private static final String FRESH_BUILD_KEY = "fresh_build";
	public void setIsFreshBuild(boolean isFreshBuild){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(FRESH_BUILD_KEY, isFreshBuild);
		editor.commit();
	}
	
	public boolean isFreshBuild(){
		SharedPreferences sp = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		return sp.getBoolean(FRESH_BUILD_KEY, true);
	}
	
	private static final String USER_NAME_KEY = "user_name";
	private static final String USER_PHONE_KEY = "user_phone";
	public void setUserInfo(String userName, String phoneNumber){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(USER_NAME_KEY, userName);
		editor.putString(USER_PHONE_KEY, phoneNumber);
		editor.commit();
	}
	
	public String[] getUserInfo(){
		String[] result = new String[2];
		SharedPreferences sp = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		result[0] = sp.getString(USER_NAME_KEY, "");
		result[1] = sp.getString(USER_PHONE_KEY, "");
		return result;
	}
	
	private static final String openid_key ="kl_open_id";
	public void setOpenId(String openId){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(openid_key, openId);
		editor.commit();
	}
	
	public String getOpenId(){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		return pref.getString(openid_key, "");
	}
	
	private static final String connected_PIN_key = "connected_pin";
	public void setConnectedPinKey(String pin){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(connected_PIN_key, pin);
		editor.commit();
	}
	
	public String getConnectedPinKey() {
		SharedPreferences sp = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		return sp.getString(connected_PIN_key, "");
	}
	
	private static final String auto_connected = "auto_connect";
	public void setAutoConnectFlag(boolean autoFlag) {
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(auto_connected, autoFlag);
		editor.commit();
	}
	
	public boolean getAutoFlag() {
		SharedPreferences sp = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		return sp.getBoolean(auto_connected, false);
	}
	
	private static final String IP_ADDRESS_STRING = "ip_address";
	public void saveIpAddress(String ipString) {
		SharedPreferences  pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(IP_ADDRESS_STRING, ipString);
		editor.commit();
	}
	public String getIpAddress() {
		SharedPreferences sp = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		return sp.getString(IP_ADDRESS_STRING, "");
	}
	
	
	public static final String whiteApp_list_pref = "white_app_list"; 
	public static final String installedApp_list_pref = "installed_app_list"; 
	public static final String allApp_list_pref = "app_app_list";
	public void setAppListPref(String pref_key, String appId){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		String applists = pref.getString(pref_key, "");
		if (appId.isEmpty()){
			applists = appId;
		}else if(!applists.contains(appId+",")){
			applists += appId+",";
		}
		Editor editor = pref.edit();
		editor.putString(pref_key, applists);
		editor.commit();
	}
	
	public void removeAppListPref(String pref_key, String appId){
		SharedPreferences pref = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		String applists = pref.getString(pref_key, "");
		if(applists.contains(appId+",")){
			applists = applists.replace(appId+",", "");
		}
		Editor editor = pref.edit();
		editor.putString(pref_key, applists);
		editor.commit();
	}
	
	public String getAppListPref(String pref_key){
		SharedPreferences sp = appContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		return sp.getString(pref_key, "");
	}
	
	
}
