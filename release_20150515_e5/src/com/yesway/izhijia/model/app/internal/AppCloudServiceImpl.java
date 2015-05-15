package com.yesway.izhijia.model.app.internal;

import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.model.BaseAsyncTask;

public class AppCloudServiceImpl extends AppCloudService {

	@Override
	public void getAppClassList(final NetworkRequestListener listener) {
		// TODO Auto-generated method stub
		downloadTask = new BaseAsyncTask(listener) {
			@Override
			protected Integer doInBackground(String... params) {
				if (listener != null){
					listener.onRequestProgress();
				}
				AppClass appClass[] = AppCloudManager.getInstance().getAppClassList();
				
				if (appClass != null){
					if (listener != null){
						//listener
					}
					return 0;
				}else {
					return -1;
				}
			}
		};
		downloadTask.execute();
	}


	@Override
	public void getAppList(NetworkRequestListener listener) {
		// TODO Auto-generated method stub
		downloadTask = new BaseAsyncTask(listener) {
			@Override
			protected Integer doInBackground(String... params) {
				// TODO Auto-generated method stub
				if (listener != null){
					listener.onRequestProgress();
				}
				Application applicationList[] = AppCloudManager.getInstance().getApplicationList();
				if(applicationList != null){
					if(listener!=null){
						// listener
					}
					return 0;
				}else {
					return -1;
				}
			}
		};
		downloadTask.execute();
	}

	@Override
	public void getUserAppList(String mediaType, NetworkRequestListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserApp(String mediaType, NetworkRequestListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserApp(String mediaType, NetworkRequestListener listener) {
		// TODO Auto-generated method stub
		
	}

}
