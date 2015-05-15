package com.yesway.izhijia.model.app;

import java.lang.reflect.Field;
import java.util.List;

import com.yesway.izhijia.R;
import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.model.app.internal.Application;

public class AppModel {
	public static final int STATUS_UNDOWNLOAD = 1;
	public static final int STATUS_DOWNLOADING = 2;
	public static final int STATUS_NEED_UPDATE = 3;
	public static final int STATUS_DOWNLOADED = 4;
	
	private int status = STATUS_UNDOWNLOAD;
	private int image;
	
	private Application app;
	
	private AppStatusListener listener;
	private AppAccessStatusListener accessListener;
	private AppService appService;
	
	public AppModel(){
		app = new Application();
	}
	
	public AppModel(Application app){
		this.app = app;
	}
	
	public static void syncApps(AppModelSyncListener listener){
		 AppService service = AppService.createAppService();
		 service.syncApps(listener);
	}
	
	public static interface AppModelSyncListener extends NetworkRequestListener{
		void onAppModelSynced(List<AppModel> apps);
	}
	
	public static interface AppStatusListener{
		void onAppStatusChanged(int oldStatus, int newStatus, AppModel app);
	}
	
	public static interface AppAccessStatusListener{
		void onAppAcessStatusChanged(boolean oldStatus, boolean newStatus, AppModel app);
	}
	
	public String getId() {
		return app.getId();
	}
	public String getName() {
		return app.getAppName();
	}
	public void setName(String name) {
		app.setAppName(name);
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		if(this.status == status){
			return;
		}
		
		int oldStatus = this.status;		
		this.status = status;
		
		if(listener != null){
			listener.onAppStatusChanged(oldStatus, status, this);
		}
	}
	
	public int getImage() {
		String imgString = app.getPic1URL();
		try {
			Field imageField = R.drawable.class.getField(imgString);
			return imageField.getInt(new R.drawable());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	
	public boolean isAllowAccess() {
		return app.isWhiteList();
	}
	public void setAllowAccess(boolean allowAccess) {
		if(app.isWhiteList() == allowAccess){
			return;
		}
		
		app.setWhiteList(allowAccess);
		if(accessListener != null){
			accessListener.onAppAcessStatusChanged(!allowAccess, allowAccess, this);
		}
		
	}
	public boolean isOwnApp() {
		if (app.getIsBuiltin() == 0){
			return false;
		}
		else {
			return true;
		}
	}
	public void setOwnApp(boolean isOwnApp) {
		if (isOwnApp){
			app.setIsBuiltin(1);
		}
		else {
			app.setIsBuiltin(0);
		}
	}
	
	public void setAccessListener(AppAccessStatusListener listener){
		this.accessListener = listener;
	}
	
	public AppAccessStatusListener getAccessListener(){
		return accessListener;
	}
	
	public void setListener(AppStatusListener listener){
		this.listener = listener;
	}
	
	public AppStatusListener getListener(){
		return listener;
	}
	public void delete() {
		getService();
		
		appService.deleteApp(this);
	}

	private void getService() {
		if(appService == null)
			appService = AppService.createAppService();
	}
	
	public void download() {
		getService();
		appService.downloadApp(this);
		
	}
}
