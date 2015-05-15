package com.yesway.izhijia.activity;

import java.io.IOException;

import com.yesway.izhijia.R;
import com.yesway.izhijia.common.BaseUiListener;
import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.common.Utility;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.kaola.KaoLaService;
import com.yesway.izhijia.kaola.internal.KaoLaServiceImpl;
import com.yesway.izhijia.model.user.UserInfo;
import com.yesway.izhijia.model.user.UserModel;
import com.yesway.izhijia.model.user.UserModel.Listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends BaseActivity implements OnClickListener, TextWatcher {
	private EditText phoneNumber;
	private EditText verifyNumber;
	private Button loginButton;
	private UserModel user;
	private Button retrieveBtn;
	private ProgressDialog pDialog;
	private Handler hander = new Handler();
	
	private KaoLaService klservice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		retrieveBtn = (Button)findViewById(R.id.verify_number_retrieve_button);
		retrieveBtn.setOnClickListener(this);
		
		loginButton = (Button)findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		loginButton.setEnabled(false);
		
		phoneNumber = (EditText) findViewById(R.id.phone_number);		
		verifyNumber = (EditText) findViewById(R.id.verify_number);
		phoneNumber.addTextChangedListener(this);
		verifyNumber.addTextChangedListener(this);
		
		user = UiEngine.getInstance(this).getUserEngine().getCurrentUser();
		phoneNumber.setText(user.getPhoneNumber());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_login);
		retrieveBtn = (Button)findViewById(R.id.verify_number_retrieve_button);
		retrieveBtn.setOnClickListener(this);
		
		loginButton = (Button)findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		loginButton.setEnabled(false);
		
		phoneNumber = (EditText) findViewById(R.id.phone_number);		
		verifyNumber = (EditText) findViewById(R.id.verify_number);
		phoneNumber.addTextChangedListener(this);
		verifyNumber.addTextChangedListener(this);
		
		user = UiEngine.getInstance(this).getUserEngine().getCurrentUser();
		phoneNumber.setText(user.getPhoneNumber());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button: {
			doLogin();
//			onLoginSuccess();
//			testActiviateDevice();

//			testGetContentByCid();
//			testGetRadioPlayList();
//			testPlayAudio();
			break;
		}
		case R.id.verify_number_retrieve_button: {
			doGetCode();
			break;
		}
		default:
			break;
		}
	}

	
	private void testPlayAudio() {
		final String url = "http://image.kaolafm.net/mz/audios/201501/9dfe0144-242e-4a6f-8ba8-64a41ca5d34a.mp3?appid=yztsx9822&deviceid=100000031422647&audioid=1000000805182";
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener(){

				@Override
				public void onPrepared(MediaPlayer mp) {
					Log.i("bruce", "onPrepared" + url);
					mp.start();
					
				}});
			mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener(){

				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {
					Log.i("bruce", "onBufferingUpdate" + percent);
					if(percent == 100){
						mp.start();
					}
				}});
			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.i("bruce", "onCompletion");
					
					if(mp != null){
						mp.release();
					}
					
					mp = null;
				}});
			
			mediaPlayer.setOnInfoListener(new OnInfoListener(){

				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					Log.i("bruce", "onInfo");
					return false;
				}});
			
			mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener(){

				@Override
				public void onSeekComplete(MediaPlayer mp) {
					Log.i("bruce", "onSeekComplete");
				}});
			
			mediaPlayer.prepareAsync();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void testGetRadioPlayList() {
		UiEngine.getInstance(this).getKaoLaEngine().getRadioPlayListById("355136056429065", "1200000000227", null);
		
	}

	private void testGetContentByCid() {
		UiEngine.getInstance(this).getKaoLaEngine().getContentsByCategoryId("355136056429065", "734", null);
		
	}

	private void testActiviateDevice() {
		UiEngine.getInstance(this).getKaoLaEngine().getAllCategories("355136056429065", null);
	}

	private void doGetCode() {
		UiEngine.getInstance(this).getUserEngine().requestVerifyCode(phoneNumber.getText().toString(),new NetworkRequestListener(){

			@Override
			public void onRequestStart() {
				retrieveBtn.setEnabled(false);
			}

			@Override
			public void onRequestProgress() {
				hander.post(new Runnable(){
					@Override
					public void run() {
						retrieveBtn.setText(R.string.getting_code);
					}});
			}

			@Override
			public void onRequestFailed() {
				retrieveBtn.setText(R.string.RETRIEVE_VERIFY_NUMBER);
				retrieveBtn.setEnabled(true);
			}

			@Override
			public void onRequestComplete() {
				retrieveBtn.setText(R.string.RETRIEVE_VERIFY_NUMBER);
				retrieveBtn.setEnabled(true);
			}

			@Override
			public void onRequestCanceled() {
			}
			});
	}

	//listener for login
	private class MyUiListener extends BaseUiListener implements Listener{
		MyUiListener(Activity ctx){
			super(ctx);
		}
		
		@Override
		public void onRequestProgress() {
			// TODO Auto-generated method stub
			hander.post(new Runnable() {
				
				@Override
				public void run() {
					pDialog = Utility.showProgressDlg(LoginActivity.this, R.string.user_logining);
				}
			});
		}
		
		@Override
		public void onUserLogin(UserInfo info) {
			user.setUserInfo(info);
		}
		
		@Override
		public void onRequestFailed() {
			super.onRequestFailed();
			if(pDialog!=null){
				pDialog.dismiss();
				pDialog = null;
			}
			Utility.showErrorDialog(LoginActivity.this, R.string.fail_to_login);
		}
		
		@Override
		public void onRequestComplete() {
			super.onRequestComplete();
			//update user phone number
			if(pDialog!=null){
				pDialog.dismiss();
				pDialog = null;
			}
			onLoginSuccess();
		}
	}
	
	private void doLogin() {
		String phoneString = phoneNumber.getText().toString();
		if(phoneString.length() < 11){
			Toast.makeText(this, R.string.PLEASE_INPUT_PHONE_NUM, Toast.LENGTH_SHORT).show();
			return;
		}
		if(!Utility.checkNetworkStatus(this)){
			Toast.makeText(this, R.string.please_open_network, Toast.LENGTH_SHORT).show();
			return;
		}
		UiEngine.getInstance(this).getUserEngine()
				.login(phoneString, verifyNumber.getText().toString(),
						new MyUiListener(this));
	}
	
	

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(phoneNumber != null && phoneNumber.getText().length() > 0){
			loginButton.setEnabled(true);
		}else{
			loginButton.setEnabled(false);
		}
	}

	public void onLoginSuccess() {
		user.setPhoneNumber(phoneNumber.getText().toString());
		user.setUserName("frank");
		UiEngine.getInstance(LoginActivity.this).getUserEngine().saveUser(user);
		
		//start main panel view
//		Intent i = new Intent(LoginActivity.this, ConnectStatusActivity.class);
		Intent i = new Intent(LoginActivity.this, MainPanelActivity.class);
		startActivity(i);
		finish();
	}
}
