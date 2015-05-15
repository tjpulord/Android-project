package com.example.animationsample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.spot.SpotManager;

import com.example.animationsample.MyView.Acceletor;

import android.R.menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	// private ImageView iv;
	// Button btnRoate;
	// MyThread mt;
	private static final int MENU_ITEM_SHARE = 0;
	private static final int MENU_ITEM_EIXT = 1;
	private static final float RATIO = (float) 0.8;
	private float gravityX;
	private float accX;
	private SensorManager sManager;
	private MyView myView;

	private AlertDialog menuDialog;
	private View menuView;
	private GridView menuGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		myView = new MyView(this);
		setContentView(myView);

		// custom menu list
		menuView = View.inflate(this, R.layout.menu_layout, null);
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);

		menuGrid = (GridView) menuView.findViewById(R.id.menuview);
		menuGrid.setAdapter(initMenuAdapter());
		menuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case MENU_ITEM_SHARE:
					Toast.makeText(getApplicationContext(), "share",
							Toast.LENGTH_SHORT).show();
					// start the share activity
					Intent intent = new Intent(MainActivity.this,
							ShareActivity.class);
					startActivity(intent);
					menuDialog.dismiss();
					break;
				case MENU_ITEM_EIXT:
					System.exit(0);
					break;

				default:
					break;
				}
			}
		});

		// 有米广告 初始化
		AdManager.getInstance(this).init("4b1049a5a0d0806c",
				"a3d32856861059ce", false);
		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.TOP | Gravity.END;
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 实例化迷你banner
//		DiyBanner banner = new DiyBanner(this, diyad)
		//SmartBannerManager.init(this);
		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {
			String adTag = "adListener";
			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.d(adTag, "adverstisement switched");
			}
			@Override
			public void onReceivedAd(AdView arg0) {
				Log.d(adTag, "adverstisement requested");
			}
			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.d(adTag, "advertisement failed");
			}
		});
		
		this.addContentView(adView, layoutParams);
	}

	private SimpleAdapter initMenuAdapter() {
		// TODO Auto-generated method stub
		int menu_item_ids[] = { R.id.menu_name, R.id.menu_image };
		String menu_items[] = { "name", "image" };
		String menu_names[] = { "分享", "退出" };
		int menu_image[] = { R.drawable.share, R.drawable.exit };
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menu_image.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put(menu_items[0], menu_names[i]);
			hashMap.put(menu_items[1], menu_image[i]);
			data.add(hashMap);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
				R.layout.menu_info, menu_items, menu_item_ids);
		return simpleAdapter;
	}

	Acceletor acclisten = new Acceletor() {

		@Override
		public float getAcc() {
			float tmp = accX;
			accX = 0;
			return tmp;
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		sManager.registerListener(this,
				sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		sManager.registerListener(this,
				sManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_UI);
		myView.setAcceListener(acclisten);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		myView.stop();
		sManager.unregisterListener(this);
		SpotManager.getInstance(this).onStop();
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float tmp = event.values[0] - gravityX;
			tmp = Math.abs(tmp);
			if (accX < tmp && tmp > 2) {
				accX = tmp;
			}
			break;
		case Sensor.TYPE_GRAVITY:
			gravityX = RATIO * event.values[0] + (1 - RATIO) * event.values[0];
			break;
		default:
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// menu layout
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).create();
			menuDialog.setView(menuView);
		}
		menuDialog.getWindow().setGravity(Gravity.BOTTOM);
		menuDialog.show();
	}
}
