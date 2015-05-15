package com.yesway.izhijia.apptoolkit;

import java.util.Arrays;

import android.content.Context;

import com.yesway.izhijia.R;

import com.telecomsys.autokit.communication.DynamicCommonDataManager;
import com.telecomsys.autokit.communication.DynamicCommonDataManager.*;
import com.telecomsys.autokit.communication.CommunicationManager;

public class CarAppController implements DynamicCDMListener {

	private String appID;
	private CarAppListener carListener;
	private String[] keys;
	private Context ctx;
	public static interface CarAppListener {
        void onCarAppStarted();
        void onCarAppStopped();
        void onMessageReceived(String key, String value);
	}

	public void init(String appID, Context appContext, CarAppListener listener,String[] listenerKeys) {
		if(appContext == null) {
			return;
		}
		if ((appID == null) || appID.equals("")) {
			return;
		}
		if(listener == null) {
			System.out.println("warning: no car message listener specified");
		}
		this.appID = appID;
		ctx = appContext;
		carListener = listener;
		keys = listenerKeys;

		DynamicCommonDataManager cdm = DynamicCommonDataManager.getInstance();
		String perfix = ctx.getResources().getString(R.string.CDM_KEY_PERFIX_FOR_CAR) + "." + appID + ".";
		
		//Subscribe start/stop keys
		String keyStartApp = ctx.getResources().getString(R.string.CDM_MSG_KEY_STARTAPP);
		String keyStopApp = ctx.getResources().getString(R.string.CDM_MSG_KEY_STOPAPP);
		cdm.subscribe(perfix + keyStartApp, this);
		cdm.subscribe(perfix + keyStopApp, this);
		
		if (listenerKeys != null) {
			//Subscribe user defined keys
			for(int i = 0; i < listenerKeys.length; ++i) {
				String key = perfix + listenerKeys[i];
				boolean result = cdm.subscribe(key, this);
				if (!result) {
					System.out.println("Failed to subscribe key to CDM, key:" + key);
				}
			}
		}
	}
	public boolean sendMessage(String key, String value) {
		//TODO: detect whether the app has been started and not stopped yet
		String perfix = ctx.getResources().getString(R.string.CDM_KEY_PERFIX_FOR_HANDSET);
		String messageKey = perfix + "." + appID + "." + key;
		return DynamicCommonDataManager.getInstance().update(messageKey, value);
	}

	
	public CarAppController() {
		// TODO Auto-generated constructor stub
	}

	public void Destroy() {
		//TODO: unsubscribe the registered keys
	}

	public void onUpdate(String key, String value, int status) {
		if(carListener == null) {
			return;
		}
		String[] subKeys = key.split("car\\.app\\."+appID+"\\.");
		if((subKeys != null) && (subKeys.length == 2)) {
			String keyStartApp = ctx.getResources().getString(R.string.CDM_MSG_KEY_STARTAPP);
			String keyStopApp = ctx.getResources().getString(R.string.CDM_MSG_KEY_STOPAPP);
			if (subKeys[1].equals(keyStartApp)) {
				carListener.onCarAppStarted();
				sendMessage(keyStartApp,value);
			}
			else if (subKeys[1].equals(keyStopApp)) {
				carListener.onCarAppStopped();
			}
			else if(Arrays.asList(keys).contains(subKeys[1])){
				carListener.onMessageReceived(subKeys[1], value);
			}
			else{
				
			}
		}
	}
}
