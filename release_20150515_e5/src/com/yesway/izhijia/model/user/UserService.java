package com.yesway.izhijia.model.user;

import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.model.BaseService;

abstract class UserService extends BaseService{
	
	public abstract void requestVerifyCode(String phoneNumber, NetworkRequestListener listener);
	public abstract void login(String phoneNumber, String code, UserModel.Listener listener);
	public abstract void logout(NetworkRequestListener listener);
	
	public static UserService createUserService(){
		return new UserServiceImpl();
	}

}
