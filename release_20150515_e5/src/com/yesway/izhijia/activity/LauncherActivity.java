package com.yesway.izhijia.activity;

import com.telecomsys.autokit.communication.FileRequestListenerImpl;
import com.telecomsys.autokit.communication.FileTransmition;
import com.yesway.izhijia.R;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.fragment.SplashFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LauncherActivity extends BaseActivity implements OnPageChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean isFTT = UiEngine.getInstance(this).getPreferenceEngine().isFreshBuild();
		if(isFTT){
			initFTTScreen();
		}else{
			initNonFTTScreen();
		}
		FileTransmition.getInstance().setFileRequestListener(new FileRequestListenerImpl());
	}

	private void initNonFTTScreen() {
		setContentView(R.layout.fragment_splash);

		// display login screen automatically with a timer
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				showLoginScreen();
			}
		}, 1000);
	}

	private void initFTTScreen() {
		setContentView(R.layout.activity_ftt_launcher);
		ViewPager pager = (ViewPager)findViewById(R.id.splash_view);
		pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(this);
		this.onPageSelected(0);
	}
	
	
	private static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			SplashFragment fragment = new SplashFragment();
			fragment.init(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
	
	private void showLoginScreen() {
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		LinearLayout indicator = (LinearLayout)findViewById(R.id.indicator);		
		for(int i = 0; i<indicator.getChildCount(); i++){
			ImageView view = (ImageView)indicator.getChildAt(i);
			
			if(i == position){
				view.setImageResource(R.drawable.ic_indicator_selected);
			}else{
				view.setImageResource(R.drawable.ic_indicator_unselected);
			}
		}
	}
}
