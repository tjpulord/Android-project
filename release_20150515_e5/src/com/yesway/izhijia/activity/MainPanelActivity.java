package com.yesway.izhijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.telecomsys.autokit.communication.DynamicCommonDataManager;
import com.yesway.izhijia.R;
import com.yesway.izhijia.common.Utility;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.fragment.AppManagementFragment;
import com.yesway.izhijia.fragment.MyCarFragment;
import com.yesway.izhijia.telephony.MyPhoneSignalStateListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainPanelActivity extends BaseActivity implements OnClickListener {

	private FragmentTabHost mTabHost;
	private LayoutInflater inflater;
	private TextView contractTextView;
	private DrawerLayout drawer;
	private MyPhoneSignalStateListener signalListener;
	private TelephonyManager telManager;
	private boolean isCarConnected;
	
	private static final String GSM_SIGNAL_STRING = "hs.state.signalstrength";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_panel);
		inflater = LayoutInflater.from(this);
		final FrameLayout mainPanel = (FrameLayout) findViewById(R.id.content_frame);
		inflater.inflate(R.layout.activity_tab, mainPanel);

		initMainPanel();
		initNavigationDrawer();
		drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		final View user_info = findViewById(R.id.user_info);
		
		
		drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener(){

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				int width = user_info.getMeasuredWidth();
				Float f = width * slideOffset;
				mainPanel.setX(f);
			}
		});

		UiEngine.getInstance(this).getKaoLaEngine().syncToDevice();
		
		// initial the TelephoneManager to get the signal strength.
		signalListener = new MyPhoneSignalStateListener();
		telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//telManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	private void initMainPanel() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		TextView tv1 = (TextView) inflater.inflate(R.layout.tab_indicator_view,
				null);
		tv1.setText(R.string.INTER_CONNECT_APP_MANAGEMENT);
		TextView tv2 = (TextView) inflater.inflate(R.layout.tab_indicator_view,
				null);
		tv2.setText(R.string.MY_CAR);
		
		mTabHost.addTab(mTabHost.newTabSpec("appmgmt").setIndicator(tv1),
				AppManagementFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("mycar").setIndicator(tv2),
				MyCarFragment.class, null);
	}

	private void initNavigationDrawer() {
		initUserInformation();
		initFunctionList();
		initContractInformation();
	}
	

	private void initUserInformation() {
		TextView user_id = (TextView) findViewById(R.id.user_id);
		String phoneNumber =  UiEngine.getInstance(this).getUserEngine().getCurrentUser().getPhoneNumber();
		user_id.setText(Utility.getEncryptedPhoneNumber(phoneNumber));
		// image.setb
		user_id.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.ic_launcher, 0, 0);

		findViewById(R.id.user_info_btn).setOnClickListener(this);
		findViewById(R.id.user_info).setOnClickListener(this);
	}

	private void initFunctionList() {
		ListView functionList = (ListView) findViewById(R.id.function_list);
		List<FunctionInformation> functions = getFunctionList();
		MyAdapter functionAdapter = new MyAdapter(functions);
		functionList.setAdapter(functionAdapter);
	}

	private void initContractInformation() {
		contractTextView = (TextView) findViewById(R.id.phone_number);
		contractTextView.setText("400-0095-190");
		contractTextView.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.ic_phone, 0, 0, 0);
		contractTextView.setOnClickListener(this);
	}

	private List<FunctionInformation> getFunctionList() {
		ArrayList<FunctionInformation> result = new ArrayList<FunctionInformation>(
				4);
		result.add(new FunctionInformation(R.drawable.ic_share,
				R.string.SHARE, ShareActivity.class));
		result.add(new FunctionInformation(R.drawable.ic_service_contract,
				R.string.SERVICE_CONTRACT, ServiceContractActivity.class));
		result.add(new FunctionInformation(R.drawable.ic_help,
				R.string.USAGE_HELP, UsageHelpActivity.class));
		result.add(new FunctionInformation(R.drawable.ic_software_upgrade,
				R.string.SOFTWARE_UGRADE, SoftwareUpgradeActivity.class));
		return result;
	}

	private static class FunctionInformation {
		public int imageId;
		public int textid;
		public Class<? extends Activity> activity_class;

		public FunctionInformation(int imageId, int textId,
				Class<? extends Activity> activity_class) {
			this.imageId = imageId;
			this.textid = textId;
			this.activity_class = activity_class;
		}
	}

	private class MyAdapter extends BaseAdapter {
		private List<FunctionInformation> functionList = new ArrayList<FunctionInformation>();

		public MyAdapter(List<FunctionInformation> appList) {
			functionList.addAll(appList);
		}

		@Override
		public int getCount() {
			return functionList.size();
		}

		@Override
		public Object getItem(int position) {
			return functionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.list_item_function, null);
			}

			final FunctionInformation function = functionList.get(position);
			ImageView functionImage = (ImageView) view
					.findViewById(R.id.function_icon);
			functionImage.setImageResource(function.imageId);

			TextView functionText = (TextView) view
					.findViewById(R.id.function_name);
			functionText.setText(function.textid);

			ImageButton btn = (ImageButton) view.findViewById(R.id.more_btn);
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainPanelActivity.this,
							function.activity_class);
					startActivity(i);
				}
			};
			
			btn.setOnClickListener(listener);
			view.setOnClickListener(listener);

			// software upgrade case
			ImageView upgradeImage = (ImageView) view
					.findViewById(R.id.upgrade_icon);
			if (function.textid == R.string.SOFTWARE_UGRADE) {
				//TODO set correct icon
				upgradeImage.setImageResource(R.drawable.ic_new); 
				upgradeImage.setVisibility(View.VISIBLE);
			} else {
				upgradeImage.setVisibility(View.GONE);
			}
			return view;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_info_btn:
		case R.id.user_info: {			
			Intent i = new Intent(this, MemberInformationActivity.class);
			startActivity(i);
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {
					if(drawer.isDrawerOpen( GravityCompat.START)){
						drawer.closeDrawer(GravityCompat.START);
					}
					
				}}, 0);
			
			break;
		}

		case R.id.phone_number: {
			if (contractTextView == null || contractTextView.getText() == null) {
				return;
			}

			Uri uri = Uri.fromParts("tel",  contractTextView.getText().toString(), null);
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		default: {
			break;
		}
		}
	}

	@Override
	public void onBackPressed() {
		showExitAppPrompt(this);		
	}
	
	public void showExitAppPrompt(Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(R.string.confirm_exit);
		builder.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
					finish();
					System.exit(0);
			}});
		builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog dlg = builder.create();
		dlg.show();
	}

	public void openOrCloseDrawer() {
		if(drawer == null){
			return;
		}
		if(drawer.isDrawerOpen( GravityCompat.START)){
			drawer.closeDrawer(GravityCompat.START);
		}else{
			drawer.openDrawer( GravityCompat.START);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		telManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		signalListener.setListener(new MyPhoneSignalStateListener.SignalStrengthLevelListener() {
			
			@Override
			public void getSignalStrengthLevel(int level) {
				// Send the gsm signal strength level to CDM.
				Log.d("signalStrength:", "signal level: "+level);
				DynamicCommonDataManager.getInstance().update(GSM_SIGNAL_STRING, String.valueOf(level));
			}
		});
		isCarConnected = UiEngine.getInstance(this).getCarEngine().isCarConnected();
		if (isCarConnected){
			DynamicCommonDataManager.getInstance().update(GSM_SIGNAL_STRING, signalListener.getSignalLevel()+"");
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		telManager.listen(signalListener, PhoneStateListener.LISTEN_NONE);
	}
}
