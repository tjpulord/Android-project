package com.yesway.izhijia.model;

import com.yesway.izhijia.common.NetworkRequestListener;

import android.os.AsyncTask;

public abstract class BaseAsyncTask  extends AsyncTask<String, Integer, Integer> {
	protected NetworkRequestListener listener;
	public BaseAsyncTask(NetworkRequestListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute() {
		if(listener != null)
			listener.onRequestStart();
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		if(listener != null){
			listener.onRequestProgress();
		}
		
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer errorcode) {
		if(errorcode != 0){
			if(listener != null)
				listener.onRequestFailed();
		} else{
			if(listener != null)
				listener.onRequestComplete();
		}
	}
}
