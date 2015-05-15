package com.yesway.izhijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.yesway.izhijia.R;
import com.yesway.izhijia.common.BaseUiListener;
import com.yesway.izhijia.common.Constant;
import com.yesway.izhijia.common.Utility;
import com.yesway.izhijia.engine.UiEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MemberInformationActivity extends BaseActivity implements OnClickListener{
	private Handler hander = new Handler();
	private Dialog waitDialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_info);
		TextView phoneText = (TextView)findViewById(R.id.user_id);
		String phoneNumber =  UiEngine.getInstance(this).getUserEngine().getCurrentUser().getPhoneNumber();
		phoneText.setText(Utility.getEncryptedPhoneNumber(phoneNumber));
		findViewById(R.id.logout_btn).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		initListView();
		super.onResume();
	}

	private void initListView() {
		ListView userInfoList = (ListView) findViewById(R.id.user_info_list);
		ArrayList<MememberListEntry> userInfos = new ArrayList<MememberListEntry>(2);
		userInfos.add(new MememberListEntry(getResources().getString(R.string.PHONE_NUMBER),  UiEngine.getInstance(this).getUserEngine().getCurrentUser().getPhoneNumber(), false));
		userInfos.add(new MememberListEntry(getResources().getString(R.string.USER_NAME), UiEngine.getInstance(this).getUserEngine().getCurrentUser().getUserName(), true, UserInfoSettingActivity.class));
		userInfoList.setAdapter(new MyAdapter(userInfos));
		
		ListView carInfoList = (ListView) findViewById(R.id.car_info_list);
		ArrayList<MememberListEntry> carInfos = new ArrayList<MememberListEntry>(2);
		carInfos.add(new MememberListEntry(getResources().getString(R.string.CAR_INFORMATION), "", true, CarInformationActivity.class));
		carInfos.add(new MememberListEntry(getResources().getString(R.string.CITY), "北京", true));
		carInfoList.setAdapter(new MyAdapter(carInfos));
	}
	
	private static class MememberListEntry{
		public String label;
		public String value;
		public boolean isShowMoreBtn;
		public Class<? extends Activity> activityClass;
		
		public MememberListEntry (String lab, String val, boolean showBtn){
			this(lab, val, showBtn, null);
		}
		
		public MememberListEntry (String lab, String val, boolean showBtn,  Class<? extends Activity> cls){
			label = lab;
			value = val;
			isShowMoreBtn = showBtn;
			activityClass = cls;
		}
	}
	
	private class MyAdapter extends BaseAdapter {
		private ArrayList<MememberListEntry> list = new  ArrayList<MememberListEntry>();
		
		public MyAdapter(List<MememberListEntry> info){
			list.addAll(info);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(view == null){
				view = LayoutInflater.from(MemberInformationActivity.this).inflate(R.layout.list_item_memeber_info, null);
			}
			final MememberListEntry info = list.get(position);
			
			TextView label = (TextView) view.findViewById(R.id.label);
			label.setText(info.label);
			
			TextView value = (TextView) view.findViewById(R.id.value);
			value.setText(info.value);
			
			View moreBtn = view.findViewById(R.id.more_btn);
			if(info.isShowMoreBtn){
				moreBtn.setVisibility(View.VISIBLE);
			}else{
				moreBtn.setVisibility(View.GONE);
			}
			
			if(info.activityClass != null){
				OnClickListener listener = new OnClickListener(){

					@Override
					public void onClick(View v) {
							Intent intent = new Intent(MemberInformationActivity.this, info.activityClass);	
							startActivityForResult(intent, Constant.REQUEST_CAR_INFO);
					}};
					
				view.setOnClickListener(listener);
				moreBtn.setOnClickListener(listener);
				
			}else{
				view.setOnClickListener(null);
				moreBtn.setOnClickListener(null);
			}
			return view;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Constant.ACTIVITY_CLOSE){
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.logout_btn){
			showConfirmDialog();
			return;
		}else if(v.getId() == R.id.back_btn){
			finish();
			return;
		}
	}
	
	private void showConfirmDialog(){
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setMessage(R.string.CONFIRM_LOGOUT);
		dialogBuilder.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				doLogout();
			}
		});
		dialogBuilder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog dlg = dialogBuilder.create();
		dlg.show();
	}

	private void doLogout() {
		UiEngine.getInstance(this).getUserEngine().logout(new BaseUiListener(this){
			@Override
			public void onRequestComplete() {
				super.onRequestComplete();
				if(waitDialog!=null){
					waitDialog.dismiss();
				}
				for(BaseActivity activity : UiEngine.getInstance(MemberInformationActivity.this).getAllActivities()){
					if(activity != MemberInformationActivity.this){
						activity.finish();
					}
				}
				Intent i = new Intent(MemberInformationActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
			}
			
			@Override
			public void onRequestStart() {
				hander.post(new Runnable() {
					
					@Override
					public void run() {
						waitDialog = Utility.showProgressDlg(MemberInformationActivity.this, R.string.user_logouting);
					}
				});
			}
			
			@Override
			public void onRequestFailed() {
				super.onRequestFailed();
				if(waitDialog!=null){
					waitDialog.dismiss();
				}
				Utility.showErrorDialog(MemberInformationActivity.this, R.string.fail_to_logout);
			}
		});
	}
}
