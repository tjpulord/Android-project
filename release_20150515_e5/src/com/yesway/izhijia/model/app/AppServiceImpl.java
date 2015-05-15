package com.yesway.izhijia.model.app;

import java.util.ArrayList;
import java.util.List;

import com.yesway.izhijia.model.BaseAsyncTask;
import com.yesway.izhijia.model.app.AppModel.AppModelSyncListener;
import com.yesway.izhijia.model.app.internal.AppManager;
import com.yesway.izhijia.model.app.internal.AppManager.AppListener;
import com.yesway.izhijia.model.app.internal.Application;

class AppServiceImpl extends AppService {
	
	@Override
	public void downloadApp(AppModel app) {
		doDownloadApp(app);		
	}
	
	@Override
	public void deleteApp(AppModel app) {
		app.setStatus(AppModel.STATUS_UNDOWNLOAD);
	}
	
	private void doDownloadApp(final AppModel app) {
		 downloadTask = new BaseAsyncTask(null) {
			@Override
			protected void onPreExecute() {
			}

			@Override
			protected Integer doInBackground(String... params) {
				app.setStatus(AppModel.STATUS_DOWNLOADING);
				
				//sleep 2.5 seconds
				try{
					Thread.sleep(2500);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				return 0;
			}

			@Override
			protected void onPostExecute(Integer errorcode) {
				app.setStatus(AppModel.STATUS_DOWNLOADED);
			}
		};
		downloadTask.execute();
	}

	@Override
	public void syncApps(final AppModelSyncListener listener) {
		 downloadTask = new BaseAsyncTask(listener){
				@Override
				protected Integer doInBackground(String... params) {
					AppManager.instance().requestAppList(new AppListener(){

						@Override
						public void onGetAppList(List<Application> apps) {
							if(listener != null){
								ArrayList<AppModel> models = new ArrayList<AppModel>();
								((AppModelSyncListener)listener).onAppModelSynced(models);
							}
						}

						@Override
						public void onGetInstalledAppList(List<Application> apps) {
						}

						@Override
						public void onWhiteList(List<Application> apps) {
						}

						@Override
						public void onError(int error) {
							
						}});
					return 0;
				}
		 };
	}
}
