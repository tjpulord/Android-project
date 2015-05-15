package com.yesway.izhijia.model.car;

import com.yesway.izhijia.model.BaseService;

abstract class CarService extends BaseService{
	public abstract void connect(String ip, int port) throws Exception;
	
	public static CarService createCarService(){
		return new CarServiceImpl();
	}
}
