package com.yesway.izhijia.common;

public interface NetworkRequestListener {
	void onRequestStart();
	void onRequestProgress();
	void onRequestFailed();
	void onRequestComplete();
	void onRequestCanceled();
}
