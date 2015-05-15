package com.yesway.izhijia.kaola;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.kaola.data.KaoLaCategory;
import com.yesway.izhijia.kaola.data.KaoLaContent;
import com.yesway.izhijia.kaola.data.KaoLaRadioItem;

public class KaoLaModel {
	private KaoLaService service;
	private String openId;
	
	public static interface ActivateListener extends NetworkRequestListener{
		void onDeviceActiviated(String openId);		
	}
	
	public static interface KaoLaInformationDownloadListener extends NetworkRequestListener{
		void onKaoLaCategories(String response);
		void onKaoLaContents(String response);
		void onKaoLaRadioPlayList(String response);
	}
	
	private void initKaoLaService(){
		if(service == null){
			service = KaoLaService.createService();
		}
	}
	
	public void activiateDevice(String deviceId, ActivateListener listener){
		initKaoLaService();
		service.activateDevice(deviceId, listener);
	}
	
	public void setOpenId(String openId){
		this.openId = openId;
	}
	
	public String getOpenId(){
		return openId;
	}

	public void getAllCategories(String deviceId, KaoLaInformationDownloadListener listener) {
		initKaoLaService();
		service.getAllCategories(deviceId,openId, listener);
	}

	public void getContentsByCategoryId(String deviceId, String cid,
			KaoLaInformationDownloadListener listener) {
		initKaoLaService();
		service.getContentListByCategoryId(deviceId, openId, cid, listener);
	}
	
	public void getRadioPlayListById(String deviceId, String radioId, KaoLaInformationDownloadListener listener){
		getRadioPlayListById(deviceId, radioId, null, listener);
	}
	
	public void getRadioPlayListById(String deviceId, String radioId, String clockId, KaoLaInformationDownloadListener listener){
		initKaoLaService();
		service.getRadioPlayList(deviceId, openId, radioId, clockId, listener);
	}
}
