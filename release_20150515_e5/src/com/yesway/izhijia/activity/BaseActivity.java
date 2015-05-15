package com.yesway.izhijia.activity;

import com.yesway.izhijia.engine.UiEngine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	protected Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		UiEngine.getInstance(this).addActivity(this);
	}

	@Override
	public void finish() {
		super.finish();
		UiEngine.getInstance(this).removeActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UiEngine.getInstance(this).removeActivity(this);
	}

	protected void showToast(int text) {
		if (toast != null) {
			toast.cancel();
		}

		toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();
	}
}
