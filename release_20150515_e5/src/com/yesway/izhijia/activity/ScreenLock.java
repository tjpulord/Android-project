package com.yesway.izhijia.activity;

import com.yesway.izhijia.R;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.view.GifView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ScreenLock extends Activity {
	private static final String TAG = "screen_lock";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_lock);
		
		GifView gif = (GifView) findViewById(R.id.screenLock);
        gif.setMovieResource(R.raw.ic_connect);
        UiEngine.getInstance(this).getKaoLaEngine().setScreenLockActivity(this);
        UiEngine.getInstance(this).getKaoLaEngine().syncToDevice();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Log.d(TAG, "override the onBackPressed medthod");
	}
}
