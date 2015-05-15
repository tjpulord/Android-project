package com.yesway.izhijia.activity;

import com.yesway.izhijia.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class UsageHelpActivity extends BaseActivity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usage_help);
		findViewById(R.id.back_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back_btn){
			finish();
		}
	}

}
