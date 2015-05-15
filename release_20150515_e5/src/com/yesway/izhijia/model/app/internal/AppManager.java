package com.yesway.izhijia.model.app.internal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.InputFilter.LengthFilter;
import android.util.Log;

import com.telecomsys.autokit.communication.DynamicCommonDataManager;
import com.telecomsys.autokit.util.JsonUtil;
import com.yesway.izhijia.common.BaseUiListener;
import com.yesway.izhijia.engine.PreferenceEngine;

public class AppManager {

	public interface AppListener{
		public void onGetAppList(List<Application> apps);
		public void onGetInstalledAppList(List<Application> apps);
		public void onWhiteList(List<Application> apps);
		public void onError(int error);
	}
	
	// 初始化三个applist，构造假数据
	private AppManager() {
		/** @TDDO remove the fake data if we can get applist from the cloud server**/
		Application app1 = new Application();
		app1.setAppName("考拉FM");
		app1.setAppTitleName("kaolafm");
		app1.setAppType(0);
		app1.setClassID("2");
		app1.setId("100");
		app1.setIsBuiltin(1);
		app1.setIsCarLink(1);
		app1.setIsDownload(1);
		app1.setFavorite(true);
		app1.setIsMustNet(1);
		app1.setOrderID(100);
		app1.setPic2URL("images/FM-KL-app.png");
		app1.setPic1URL("ic_kaola");
		
		Application app2 = new Application();
		app2.setAppName("百度音乐");
		app2.setAppTitleName("musicBD");
		app2.setAppType(0);
		app2.setClassID("2");
		app2.setId("6");
		app2.setIsBuiltin(1);
		app2.setIsCarLink(1);
		app2.setIsDownload(1);
		app2.setFavorite(true);
		app2.setIsMustNet(1);
		app2.setOrderID(100);
		app2.setPic2URL("images/baidu_music.png");
		app2.setPic1URL("ic_baidu_music");
		
		Application app3 = new Application();
		app3.setAppName("凤凰FM");
		app3.setAppTitleName("phenfixFM");
		app3.setAppType(0);
		app3.setClassID("2");
		app3.setId("200");
		app3.setIsBuiltin(0);
		app3.setIsCarLink(1);
		app3.setIsDownload(1);
		app3.setFavorite(true);
		app3.setIsMustNet(1);
		app3.setOrderID(130);
		app3.setPic2URL("images/phoenix_fm.png");
		app3.setPic1URL("ic_fh_fm");
		
		Application app4 = new Application();
		app4.setAppName("QQ音乐");
		app4.setAppTitleName("qqMusic");
		app4.setAppType(0);
		app4.setClassID("3");
		app4.setId("20");
		app4.setIsBuiltin(0);
		app4.setIsCarLink(1);
		app4.setIsDownload(1);
		app4.setFavorite(false);
		app4.setIsMustNet(1);
		app4.setOrderID(10);
		app4.setPic2URL("images/qq_music.png");
		app4.setPic1URL("ic_qq");
		
		
		allAppList.add(app1);
		allAppList.add(app2);
		allAppList.add(app3);
		allAppList.add(app4);
		
		installedApps.add(app1);
		installedApps.add(app2);
		installedApps.add(app3);
		
		// for white app list
//		app1.setWhiteList(true);
		app1.setExecfilename("/usr/local/apps/kaolafm/kaolafm");
		app1.setSize("9527");
		whiteListApps.add(app1);
	}

	public void requestWhiteListAppFromCloud(Activity act) {
		// TODO Auto-generated method stub
		if(appService == null){
			appService = AppCloudService.createAppCloudService();
		}
		appService.getAppClassList(new AppCloudListener(act));
	}

	public void requestAppClassFromCloud(Activity act) {
		// TODO Auto-generated method stub
		if(appService == null){
			appService = AppCloudService.createAppCloudService();
		}
		appService.getAppList(new AppCloudListener(act));
	}

	private static final String AppWhiteList = "AppWhiteList";
	private List<Application> whiteListApps = new ArrayList<Application>();
	private List<Application> installedApps = new ArrayList<Application>();
	private List<Application> allAppList = new ArrayList<Application>();
	private AppCloudService appService;
	private PreferenceEngine prefEngine;
	
	private static AppManager manager = null;
	
	public static AppManager instance(){
		if (manager == null){
			manager = new AppManager();
		}
		return manager;
	}
	
	public void setPrefEngine(PreferenceEngine prefEngine) {
		this.prefEngine = prefEngine;
	}
	
	public boolean requestAppList(AppListener listener){
		/** @TODO get applist from the cloud server**/
		listener.onGetAppList(allAppList);
		return true;
	}
	
	public boolean requestInstalledAppList(AppListener listener){
		/** @TODO get applist from the cloud server**/
		List<Application> installedAppList = new ArrayList<Application>();
		String applists = prefEngine.getAppListPref(PreferenceEngine.installedApp_list_pref);
		if (applists != ""){
			for (int i = 0; i < allAppList.size(); i++) {
				if(applists.contains(allAppList.get(i).getId()+",")){
					installedAppList.add(allAppList.get(i));
				}
			}
			listener.onGetInstalledAppList(installedAppList);
			installedApps = installedAppList;
		}else {
			listener.onGetInstalledAppList(installedApps);
			for (Application application : installedApps) {
				prefEngine.setAppListPref(PreferenceEngine.installedApp_list_pref, application.getId());
			}
		}
		return true;
	}
	
	public boolean requestWhiteList(AppListener listener){
		/** @TODO get applist from the cloud server**/
		String applists = prefEngine.getAppListPref(PreferenceEngine.whiteApp_list_pref);
		if(!applists.isEmpty()){
			List<Application> whiteAppList = new ArrayList<Application>();
			for (Application application : allAppList) {
				if(applists.contains(application.getId()+",")){
					application.setWhiteList(true);
					whiteAppList.add(application);
				}
			}
			listener.onWhiteList(whiteAppList);
			whiteListApps = whiteAppList;
		}else {
			whiteListApps.get(0).setWhiteList(true);
			listener.onWhiteList(whiteListApps);
			for (Application application : whiteListApps) {
				prefEngine.setAppListPref(PreferenceEngine.whiteApp_list_pref, application.getId());
			}
		}
		return DynamicCommonDataManager.getInstance().update(AppWhiteList, JsonUtil.ListToJSON(whiteListApps));
	}
	
	public void setInstalledList(String appId, boolean isInstalled){
		if (isInstalled){
			for (int i = 0, pos = 0; i < allAppList.size(); i++) {
				Application appl = allAppList.get(i);
				if (appl.getId() == appId){
					if (pos >= installedApps.size()){
						installedApps.add(appl);
					}else {
						installedApps.add(pos, appl);
					}
					prefEngine.setAppListPref(PreferenceEngine.installedApp_list_pref, appId);
				}else if (pos < installedApps.size() && appl.getId() == installedApps.get(pos).getId()){
					pos++;
				}
			}
		}else {
			for (Application application : installedApps) {
				if (application.getId() == appId){
					installedApps.remove(application);
					prefEngine.removeAppListPref(PreferenceEngine.installedApp_list_pref, appId);
					break;
				}
			}
			// traverse to remove this app if it exists in white app list
			for (Application application : whiteListApps) {
				if (application.getId() == appId){
					whiteListApps.remove(application);
					prefEngine.removeAppListPref(PreferenceEngine.whiteApp_list_pref, appId);
				}
			}
		}
	}
	
	public boolean setWhiteList(String appID, boolean isWhiteList) throws Exception{
		Application changed = null;
		/** @TODO get the app from all apps**/
		for (Application application : installedApps) {
			if (application.getId() == appID){
				application.setWhiteList(isWhiteList);
			}
		}
		whiteListApps.clear();
		prefEngine.setAppListPref(PreferenceEngine.whiteApp_list_pref, "");
		for (Application application : installedApps) {
			if (application.isWhiteList()){
				whiteListApps.add(application);
				prefEngine.setAppListPref(PreferenceEngine.whiteApp_list_pref, application.getId());
			}
		}
		
//		if(changed.isWhiteList() != isWhiteList){
//			changed.setWhiteList(isWhiteList);
//			if(isWhiteList){
//				whiteListApps.add(changed);
//			}
//			else{
//				whiteListApps.remove(changed);
//			}
		DynamicCommonDataManager.getInstance().update(AppWhiteList, JsonUtil.ListToJSON(whiteListApps));
//		}
		return true;
	}
	
	private class AppCloudListener extends BaseUiListener implements Application.ApplicationListener{

		public AppCloudListener(Activity activity) {
			super(activity);
		}

		@Override
		public void appClassListener(AppClass[] appClassList) {
			// TODO Auto-generated method stub
			Log.d("appManager", "add the class listener");
		}

		@Override
		public void applicationListener(Application[] applicationList) {
			// TODO Auto-generated method stub
			Log.d("appManager", "add the application listener");
		}
		
	}
}
