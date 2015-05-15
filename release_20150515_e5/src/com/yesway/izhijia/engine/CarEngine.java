package com.yesway.izhijia.engine;

import android.content.Context;
import com.telecomsys.autokit.communication.CommunicationManager;
import com.yesway.izhijia.model.car.CarModel;

public class CarEngine implements CarModel.ConnectStatusListener{
	private CarModel car;
	private CarModel.ConnectStatusListener listener;
	private CommunicationManager cm;
	
	public CarEngine(Context ctx){
		car = new CarModel(ctx);
		car.setListener(this);
	}
	
	public void connectCar(String carIpAddress, int port){
		car.connect(carIpAddress, port);
	}
	
	public void setCarStatusListener(CarModel.ConnectStatusListener listener){
		this.listener = listener;
	}

	@Override
	public void onConnectStatusChange(int oldStatus, int newStatus) {
		if(listener != null){
			listener.onConnectStatusChange(oldStatus, newStatus);
		}
	}

	public boolean isCarConnected() {
		return car.getStatus() == CarModel.CONNECT_STATUS_CONNECTED;
	}
}
