package com.yesway.izhijia.activity;

import com.yesway.izhijia.R;
import com.yesway.izhijia.common.Utility;
import com.yesway.izhijia.engine.KaoLaEngine;
import com.yesway.izhijia.engine.PreferenceEngine;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.fragment.AppManagementFragment;
import com.yesway.izhijia.model.car.CarModel;
import com.yesway.izhijia.model.car.CarModel.ConnectStatusListener;
import com.yesway.izhijia.view.GifView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ShareCompat.IntentBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectStatusActivity extends BaseActivity implements
		OnClickListener, ConnectStatusListener {
	private Handler handler = new Handler();
	private ProgressDialog dlg;
	private static final String tag = "ConnectStatusActivity";
	private PreferenceEngine pref;
	private EditText editText;
	private String pinCode;
	private String carIpAddress;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_input);

		pref = new PreferenceEngine(this);

		GifView gif = (GifView) findViewById(R.id.connect_icon);
		gif.setMovieResource(R.raw.ic_connect);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		findViewById(R.id.done_btn).setOnClickListener(this);
		editText = (EditText) findViewById(R.id.car_code);
		String address = pref.getConnectedPinKey();
		if (!address.isEmpty()) {
			editText.setText(address);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_connect_input);

		pref = new PreferenceEngine(this);

		GifView gif = (GifView) findViewById(R.id.connect_icon);
		gif.setMovieResource(R.raw.ic_connect);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		findViewById(R.id.done_btn).setOnClickListener(this);
		editText = (EditText) findViewById(R.id.car_code);
		String address = pref.getConnectedPinKey();
		if (!address.isEmpty()) {
			editText.setText(address);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn: {
			startMainPanel();
			break;
		}
		case R.id.done_btn: {
			doConnectCar();
			break;
		}
		}
	}

	private void doConnectCar() {
		pinCode = editText.getText().toString();

		if (pinCode == null || pinCode.trim().length() == 0) {
			showToast(R.string.please_input_code);
			editText.setText("");
			return;
		} else if (pinCode.trim().length() != 8) {
			showToast(R.string.pin_code_length_not_correct);
			return;
		}

		char[] codeArray = pinCode.toCharArray();
		// String ip_3 = null;
		// String ip_4 = null;
		//
		// if(codeArray[0] == '0'){
		// ip_3 = code.substring(1, 3);
		// }else{
		// ip_3 = code.substring(0, 3);
		// }
		//
		// if(codeArray[3] == '0'){
		// ip_4 = code.substring(4);
		// }else{
		// ip_4 = code.substring(3);
		// }

		// check the PIN code is Hex char
		for (char c : codeArray) {
			if (hexToDec(c) == -1) {
				showToast(R.string.re_input_car_code);
				// text.setText("");
				return;
			}
		}
		// PIN 码格式 C0:A8:54:45 -> 192.168.84.69
		int ip_1 = hexToDec(codeArray[0]) * 16 + hexToDec(codeArray[1]);
		int ip_2 = hexToDec(codeArray[2]) * 16 + hexToDec(codeArray[3]);
		int ip_3 = hexToDec(codeArray[4]) * 16 + hexToDec(codeArray[5]);
		int ip_4 = hexToDec(codeArray[6]) * 16 + hexToDec(codeArray[7]);
		carIpAddress = ip_1 + "." + ip_2 + "." + ip_3 + "." + ip_4;
		int port = 8080;
		Log.i(tag, "Car Ip is:" + carIpAddress);

		// check the network status
		if (Utility.checkNetworkStatus(this)) {
			UiEngine.getInstance(this).getCarEngine()
					.setCarStatusListener(this);
			UiEngine.getInstance(this).getCarEngine()
					.connectCar(carIpAddress, port);
		} else {
			showToast(R.string.please_open_network);
		}
	}

	private int hexToDec(char c) {
		int res = -1;
		if (c >= '0' && c <= '9') {
			res = c - '0';
		} else if (c >= 'a' && c <= 'f') {
			res = c - 'a' + 10;
		} else if (c >= 'A' && c <= 'F') {
			res = c - 'A' + 10;
		} else {
			Log.d(tag, "PIN错误！");
		}
		return res;
	}

	@Override
	public void onConnectStatusChange(final int oldStatus, final int newStatus) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				pref.setConnectedPinKey(pinCode);
				if (newStatus == CarModel.CONNECT_STATUS_CONNECTING) {
					showProgressDialog();
				} else if (newStatus == CarModel.CONNECT_STATUS_CONNECTED) {
					dismissProgressDialog();
					startMainPanel();
				} else {
					dismissProgressDialog();
					KaoLaEngine ke = UiEngine.getInstance(
							ConnectStatusActivity.this.getApplicationContext())
							.getKaoLaEngine();
					if (ke.getScreenLockActivity() != null) {
						// ke.getScreenLockActivity().finish();
						// ke.setScreenLockActivity(null);
						ke.onCarAppStopped();
					}
				}
			}
		});

	}

	private void startMainPanel() {
		Intent i = new Intent(this, MainPanelActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	private void dismissProgressDialog() {
		if (dlg != null && dlg.isShowing()) {
			dlg.dismiss();
		}
	}

	private void showProgressDialog() {
		dlg = Utility.showProgressDlg(this);
	}
}
