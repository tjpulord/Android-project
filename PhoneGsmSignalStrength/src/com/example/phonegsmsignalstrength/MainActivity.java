package com.example.phonegsmsignalstrength;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	private MyPhoneStateListener phoneListener;
	private TelephonyManager telmanager;
	private SignalLevel sLevel;
	
	private TextView tvShow, tvLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvShow = (TextView) findViewById(R.id.tvShow);
		tvLevel = (TextView) findViewById(R.id.tvLevel);
		phoneListener = new MyPhoneStateListener();
		telmanager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sLevel = new SignalLevel();
		
		telmanager.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		telmanager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		telmanager.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
	
	private class MyPhoneStateListener extends PhoneStateListener{
		
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			// TODO Auto-generated method stub
			super.onSignalStrengthsChanged(signalStrength);
			sLevel.setsStrength(signalStrength);
			int signalLevel = sLevel.getLevel();
			tvShow.setText("signal level: "+signalLevel);
			tvLevel.setText(signalStrength.toString());
			Log.d("signalStrength:", "signal level: "+signalLevel);
			Log.d("signalStrength:", signalStrength.toString());
		}
	};
	
}
