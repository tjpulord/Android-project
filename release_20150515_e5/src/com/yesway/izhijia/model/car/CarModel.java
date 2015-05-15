package com.yesway.izhijia.model.car;

import android.content.Context;

import com.telecomsys.autokit.communication.CommunicationManager;
import com.telecomsys.autokit.communication.DynamicCommonDataManager;
import com.telecomsys.autokit.communication.CommunicationManager.CommunicationManagerListener;
import com.yesway.izhijia.engine.PreferenceEngine;


public class CarModel implements CommunicationManagerListener {
	private int status  = CONNECT_STATUS_UNCONNECT;
	public final static int CONNECT_STATUS_UNCONNECT = 0;
	public final static int CONNECT_STATUS_CONNECTING = 1;
	public final static int CONNECT_STATUS_CONNECTED = 2;
	public final static int CONNECT_STATUS_FAILED = 3;
	
	private ConnectStatusListener listener;
	private CarService service;
	private Context appContext;
	private PreferenceEngine pref;
	private String connectIP;
	
	public CarModel(Context ctx){
		appContext = ctx.getApplicationContext();
		pref = new PreferenceEngine(appContext);
	}
	
	public static interface ConnectStatusListener{
		void onConnectStatusChange(int oldStatus, int newStatus);
	}

	public int getStatus() {
		return status;
	}

	public ConnectStatusListener getListener() {
		return listener;
	}

	public void setListener(ConnectStatusListener listener) {
		this.listener = listener;
	}
	
	public void connect(final String carIp, final int port){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				setStatus(CONNECT_STATUS_CONNECTING);
				CommunicationManager cm = CommunicationManager.getInstance();//new CommunicationManager(appContext);
				cm.setContext(appContext);
				cm.setCommunicationStatusListener(CarModel.this);
				cm.connect(carIp);
				connectIP = carIp;
				DynamicCommonDataManager cdm = DynamicCommonDataManager.getInstance();
				//cdm.initialize(cm);
				
//				comment this code as there is no need the CarService object
//				if(service == null){
//					service =  CarService.createCarService();
//				}
//				try {
//					service.connect(carIp, port);
//				} catch (Exception e) {
//					setStatus(CONNECT_STATUS_UNCONNECT);
//					return;
//				}
//				setStatus(CONNECT_STATUS_CONNECTED);
			}});
		t.start();
	}
	
	private void setStatus(int newStatus){
		if(status == newStatus){
			return;
		}
		
		int oldStatus = status;
		status = newStatus;
		
		if(listener != null){
			listener.onConnectStatusChange(oldStatus, newStatus);
		}
	}

	@Override
	public void onConnected() {
		setStatus(CONNECT_STATUS_CONNECTED);
		pref.saveIpAddress(connectIP);
		pref.setAutoConnectFlag(true);
	}

	@Override
	public void onDisconnected() {
		setStatus(CONNECT_STATUS_UNCONNECT);
	}

	@Override
	public void onConnectedFailed() {
		setStatus(CONNECT_STATUS_FAILED);
		pref.setAutoConnectFlag(false);
	}
}
