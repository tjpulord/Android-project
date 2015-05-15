package com.yesway.izhijia.model;

import com.yesway.izhijia.common.NetworkRequestListener;

public class NetworkRequestAdapter implements NetworkRequestListener{
	protected NetworkRequestListener listener;
	
	public NetworkRequestAdapter (NetworkRequestListener listener){
		this.listener = listener;
	}

	@Override
	public void onRequestStart() {
		if (listener != null){
			listener.onRequestStart();
		}				
	}

	@Override
	public void onRequestProgress() {
		if (listener != null){
			listener.onRequestProgress();
		}
		
	}

	@Override
	public void onRequestFailed() {
		if (listener != null){
			listener.onRequestFailed();
		}
		
	}

	@Override
	public void onRequestComplete() {
		if (listener != null){
			listener.onRequestComplete();
		}
	}

	@Override
	public void onRequestCanceled() {
		if (listener != null){
			listener.onRequestCanceled();
		}
	}
}
