package com.yesway.izhijia.common;

import android.app.Activity;
import android.app.ProgressDialog;

public class BaseUiListener implements NetworkRequestListener {
	
	protected Activity context;
	protected ProgressDialog dlg;
	
	public BaseUiListener(Activity activity){
		context = activity;
	}
	

	@Override
	public void onRequestStart() {
		dlg = Utility.showProgressDlg(context);
	}

	@Override
	public void onRequestProgress() {
		
	}

	@Override
	public void onRequestFailed() {
		if(dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	@Override
	public void onRequestComplete() {
		if(dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	@Override
	public void onRequestCanceled() {
		if(dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

}
