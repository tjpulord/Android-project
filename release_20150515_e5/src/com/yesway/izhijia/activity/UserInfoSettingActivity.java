package com.yesway.izhijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.yesway.izhijia.R;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.engine.UserEngine;
import com.yesway.izhijia.model.user.UserModel;

public class UserInfoSettingActivity extends BaseActivity implements OnClickListener {
	private EditText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		text = (EditText) findViewById(R.id.user_name);
		text.setText(UiEngine.getInstance(this).getUserEngine().getCurrentUser().getUserName());
		
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.done_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back_btn){
			finish();
		}else if(v.getId() == R.id.done_btn){
			UserEngine engine = UiEngine.getInstance(this).getUserEngine();
			UserModel user = engine.getCurrentUser();
			user.setUserName(text.getText().toString());
			engine.saveUser(user);
			finish();
		}
	}
}
