package com.yesway.izhijia.model.app;

import com.yesway.izhijia.model.BaseService;
import com.yesway.izhijia.model.app.AppModel.AppModelSyncListener;

abstract class AppService extends BaseService{
	
	public abstract void deleteApp(AppModel app);
	public abstract void downloadApp(AppModel app);
	
	public static AppService createAppService(){
		return new AppServiceImpl();
	}
	public abstract void syncApps(AppModelSyncListener listener);

}
