package com.yesway.izhijia.fragment;

import java.util.ArrayList;
import java.util.List;

import com.yesway.izhijia.R;
import com.yesway.izhijia.activity.ConnectStatusActivity;
import com.yesway.izhijia.common.Utility;
import com.yesway.izhijia.engine.CarEngine;
import com.yesway.izhijia.engine.KaoLaEngine;
import com.yesway.izhijia.engine.PreferenceEngine;
import com.yesway.izhijia.engine.UiEngine;
import com.yesway.izhijia.model.app.AppModel;
import com.yesway.izhijia.model.app.AppModel.AppAccessStatusListener;
import com.yesway.izhijia.model.app.AppModel.AppStatusListener;
import com.yesway.izhijia.model.app.internal.AppManager;
import com.yesway.izhijia.model.app.internal.Application;
import com.yesway.izhijia.model.car.CarModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagementFragment extends BaseFragment {
	private MyAdapter adapter;
	private ListView appList;
	private Handler handler;
	private PreferenceEngine pref;
	private ProgressDialog dlg;
	private Button btnConnectCarButton;
	private ImageButton statusBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_mgmt, container,	false);
		pref = new PreferenceEngine(this.getActivity());
		appList = (ListView) view.findViewById(R.id.app_list);
		List<AppModel> modelList = getAppList();
		adapter = new MyAdapter(modelList);
		appList.setAdapter(adapter);
		handler = new Handler();
		initMenuButton(view);
		
		statusBtn = (ImageButton)view.findViewById(R.id.carplus_status);
		btnConnectCarButton = (Button) view.findViewById(R.id.btn_connect);
		
		view.findViewById(R.id.banner).setOnClickListener(this);
		boolean isCarConnected = UiEngine.getInstance(this.getActivity()).getCarEngine().isCarConnected();
		setBtnStatus(isCarConnected);
		//statusBtn.setOnClickListener(this);
		btnConnectCarButton.setOnClickListener(this);
		return view;
	}
	
	private void setBtnStatus(boolean carConnected){
		if(carConnected){
			statusBtn.setImageResource(R.drawable.ic_installed);
			btnConnectCarButton.setVisibility(View.GONE);
		}else{
			statusBtn.setImageResource(R.drawable.ic_uninstalled);
			btnConnectCarButton.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_connect || v.getId() == R.id.banner){
			CarEngine carEngine = UiEngine.getInstance(this.getActivity()).getCarEngine();
			boolean isCarConnected = carEngine.isCarConnected();
			if(!isCarConnected){
				if (!pref.getAutoFlag()){
					Activity activity = this.getActivity();
					Intent i = new Intent(activity, ConnectStatusActivity.class);
					activity.startActivity(i);
					activity.finish();
				}else if(Utility.checkNetworkStatus(this.getActivity())){
					autoConnectCar(carEngine);
				}else {
					showToast(R.string.please_open_network);
				}
				
			}
			return;
		}
		super.onClick(v);
	}
	
	private void autoConnectCar(CarEngine carEngine) {
		int port = 8080;
		carEngine.setCarStatusListener(new CarModel.ConnectStatusListener() {
			
			@Override
			public void onConnectStatusChange(int oldStatus, int newStatus) {
				if (newStatus == CarModel.CONNECT_STATUS_CONNECTED ) {
					dismissProgressDialog();
					updateMainPanel();
//					if (newStatus == CarModel.CONNECT_STATUS_UNCONNECT){
//						startStatusConnect();
//					}else {
//						updateMainPanel();
//					}
				}else if (newStatus == CarModel.CONNECT_STATUS_CONNECTING) {
					showProgressDialog();
				}else if(newStatus == CarModel.CONNECT_STATUS_FAILED || (newStatus == CarModel.CONNECT_STATUS_UNCONNECT)){
					dismissProgressDialog();
					startStatusConnect();
					KaoLaEngine ke = UiEngine
							.getInstance(AppManagementFragment.this.getActivity())
							.getKaoLaEngine();
					if (ke.getScreenLockActivity() != null){
//						ke.getScreenLockActivity().finish();
//						ke.setScreenLockActivity(null);
						ke.onCarAppStopped();
					}
				}
			}
		});
		carEngine.connectCar(getIpAddress(), port);
	}
	
	private String getIpAddress(){
		return pref.getIpAddress();
	}
	
	private void updateMainPanel() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				boolean status = UiEngine
						.getInstance(AppManagementFragment.this.getActivity())
						.getCarEngine().isCarConnected();
				setBtnStatus(status);
				if(status){
					List<AppModel> modelList = getAppList();
					adapter.setModelList(modelList);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void startStatusConnect() {
		// 跳转到连接界面
		handler.post(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(getActivity(), ConnectStatusActivity.class));
				getActivity().finish();
			}
		});
	}
	
	private void dismissProgressDialog() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if(dlg != null && dlg.isShowing()){
					dlg.dismiss();
				}
			}
		});
	}

	private void showProgressDialog() {
		handler.post(new Runnable(){

			@Override
			public void run() {
				dlg = Utility.showProgressDlg(getActivity());
			}});
	}
	
	private void showToast(final int text){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AppManagementFragment.this.getActivity(), text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private List<Application> allAppList = new ArrayList<Application>();
	private List<Application> installedAppList = new ArrayList<Application>();
	private List<Application> whiteAppList = new ArrayList<Application>();
	
	private List<AppModel> getAppList() {
		// 调用接口函数获取成员变量值
		AppManager appManager = AppManager.instance();
		appManager.setPrefEngine(pref);
		// for testing the cloud api
		//appManager.requestAppClassFromCloud(getActivity());
		//appManager.requestWhiteListAppFromCloud(getActivity());
		
		AppManager.AppListener appListener = new AppManager.AppListener() {
			
			@Override
			public void onGetAppList(List<Application> apps) {
				allAppList.clear();
				allAppList.addAll(apps);
			}

			@Override
			public void onGetInstalledAppList(List<Application> apps) {
				installedAppList.clear();
				installedAppList.addAll(apps);
			}

			@Override
			public void onWhiteList(List<Application> apps) {
				whiteAppList.clear();
				whiteAppList.addAll(apps);
			}

			@Override
			public void onError(int error) {
				// TODO override this method
				System.out.println("AppManagementFragment onError:" + error);
			}
		};
		
		appManager.requestAppList(appListener);
		appManager.requestInstalledAppList(appListener);
		appManager.requestWhiteList(appListener);
		
		// 合并返回的三个app list
		List<AppModel> modelList = new ArrayList<AppModel>();
		
		for (Application appObj : allAppList) {
			AppModel appModel = new AppModel(appObj);
			modelList.add(appModel);
		}
		// update the installed app list
		for(int i=0, j=0; i<modelList.size() && j<installedAppList.size(); i++){
			if (modelList.get(i).getId() == installedAppList.get(j).getId()){
				modelList.get(i).setStatus(AppModel.STATUS_DOWNLOADED);
				j++;
			}
			else {
				modelList.get(i).setStatus(AppModel.STATUS_UNDOWNLOAD);
			}
		}
		// update the white app list
		for(int i=0, j=0; i<modelList.size() && j<whiteAppList.size(); i++){
			if (modelList.get(i).getId() == whiteAppList.get(j).getId()){
				modelList.set(i, new AppModel(whiteAppList.get(j)));
				modelList.get(i).setStatus(AppModel.STATUS_DOWNLOADED);
				j++;
			}
		}

		return modelList;
	}

	private class MyAdapter extends BaseAdapter {
		private List<AppModel> modelList = new ArrayList<AppModel>();

		public MyAdapter(List<AppModel> appList) {
			modelList.addAll(appList);
		}
		
		public void setModelList(List<AppModel> modelList) {
			this.modelList.clear();
			this.modelList = modelList;
		}

		@Override
		public int getCount() {
			return modelList.size();
		}

		@Override
		public Object getItem(int position) {
			return modelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = LayoutInflater
						.from(AppManagementFragment.this.getActivity());
				view = inflater.inflate(R.layout.list_item_app_mgmt, null);
			}

			final AppModel app = modelList.get(position);
			final View passInView = view;
			app.setListener(new AppStatusListener(){

				@Override
				public void onAppStatusChanged(int oldStatus, final int newStatus,
						final AppModel app) {
					handler.post(new Runnable(){
						@Override
						public void run() {
							if (newStatus == AppModel.STATUS_DOWNLOADED){
								AppManager.instance().setInstalledList(app.getId(), true);
							}else if (newStatus == AppModel.STATUS_UNDOWNLOAD){
								AppManager.instance().setInstalledList(app.getId(), false);
							}
							updateView(passInView, app);
						}});
					
				}});
			
			
			app.setAccessListener(new AppAccessStatusListener(){

				@Override
				public void onAppAcessStatusChanged(boolean oldStatus,
						final boolean newStatus, final AppModel app) {
					handler.post(new Runnable(){
						@Override
						public void run() {
							try {
								AppManager.instance().setWhiteList(app.getId(), newStatus);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							updateView(passInView, app);
						}});
				}});
			updateView(view, app);
			return view;
		}

		private void updateView(View view, final AppModel app) {
			TextView appName = (TextView) view.findViewById(R.id.app_name);
			appName.setText(app.getName());

			ImageView appIcon = (ImageView)view.findViewById(R.id.app_icon);
			appIcon.setImageResource(app.getImage());
			
			TextView status = (TextView) view.findViewById(R.id.operation);
			status.setOnClickListener(null);

			switch (app.getStatus()) {
			case AppModel.STATUS_DOWNLOADED: {
				if (app.isOwnApp()) {
					status.setVisibility(View.GONE);
				} else {
					status.setVisibility(View.VISIBLE);
					String text = AppManagementFragment.this
							.getString(R.string.DELETE);
					text += " >";
					status.setText(text);
					status.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							app.delete();	
						}});
				}
				break;
			}
			case AppModel.STATUS_DOWNLOADING: {
				status.setVisibility(View.GONE);
				break;
			}
			case AppModel.STATUS_NEED_UPDATE: {
				// 内嵌程序
				if (app.isOwnApp()) {
					String text = AppManagementFragment.this
							.getString(R.string.UPDATE);
					text += " >";
					status.setText(text);
					status.setVisibility(View.VISIBLE);
					// TODO add onClickListener to update
				} else {
					status.setVisibility(View.GONE);
				}
				break;
			}
			case AppModel.STATUS_UNDOWNLOAD: {
				status.setVisibility(View.GONE);
				break;
			}
			}
			
			final LinearLayout imageContainer = (LinearLayout)view.findViewById(R.id.image_container);
			imageContainer.removeAllViews();
			
			if(app.getStatus() == AppModel.STATUS_UNDOWNLOAD){
				final Button btn = new Button(AppManagementFragment.this.getActivity());
				btn.setText(R.string.DOWNLOAD);
				imageContainer.addView(btn);
				btn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						app.download();

					}});
				
			}else if (app.getStatus() == AppModel.STATUS_DOWNLOADING){
				Button btn = new Button(AppManagementFragment.this.getActivity());
				btn.setText(R.string.DOWNLOADING);
				imageContainer.addView(btn);
			} else if(app.isAllowAccess()){
				ImageView imageView = new ImageView(AppManagementFragment.this.getActivity());
				imageView.setImageResource(R.drawable.ic_white_app);
				imageContainer.addView(imageView);
				
				imageView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
							app.setAllowAccess(false);
						
					}});
			} else{
				ImageView imageView = new ImageView(AppManagementFragment.this.getActivity());
				imageView.setImageResource(R.drawable.ic_non_white_app);
				imageContainer.addView(imageView);
				imageView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
							app.setAllowAccess(true);
						
					}});
			}
		}
	}

}
