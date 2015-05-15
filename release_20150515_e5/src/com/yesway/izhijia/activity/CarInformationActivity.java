package com.yesway.izhijia.activity;

import com.yesway.izhijia.R;
import com.yesway.izhijia.common.Constant;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CarInformationActivity extends BaseActivity implements OnClickListener {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_info);
		
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.home).setOnClickListener(this);
		findViewById(R.id.submit_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_btn:{
			finish();
			break;
		}
		case R.id.home:{
			setResult(Constant.ACTIVITY_CLOSE);
			finish();
			break;
		}
		case R.id.submit_btn:{
			finish();
			break;
		}
		}
	}

}
