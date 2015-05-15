package com.yesway.izhijia.model;

import com.yesway.izhijia.common.NetworkRequestListener;


public class BaseService {
	protected BaseAsyncTask downloadTask;
	
	public void cancel(NetworkRequestListener listener) {
		if(downloadTask != null){
			downloadTask.cancel(true);
		}
		if(listener != null){
			listener.onRequestCanceled(); 
		}
	}

}
