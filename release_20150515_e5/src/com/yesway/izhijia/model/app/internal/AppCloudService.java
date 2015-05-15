package com.yesway.izhijia.model.app.internal;

import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.model.BaseService;


public abstract class AppCloudService extends BaseService {
	public abstract void getAppClassList(NetworkRequestListener listener);
	public abstract void getAppList(NetworkRequestListener listener);
	public abstract void getUserAppList(String mediaType, NetworkRequestListener listener);
	public abstract void addUserApp(String mediaType, NetworkRequestListener listener);
	public abstract void removeUserApp(String mediaType, NetworkRequestListener listener);
	

	public static AppCloudService createAppCloudService(){
		return new AppCloudServiceImpl();
	}
}
