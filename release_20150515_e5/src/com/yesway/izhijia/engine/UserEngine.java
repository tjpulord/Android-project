package com.yesway.izhijia.engine;

import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.model.user.UserModel;

public class UserEngine {
	private UserModel user;
	private PreferenceEngine pref;
	
	UserEngine(PreferenceEngine pref){
		this.pref = pref;		
	}
	
	public void setCurrentUser(UserModel usr){
		this.user = usr;
	}
	
	public void requestVerifyCode(String phone, NetworkRequestListener listener){
		user.requestVerifyCode(phone, listener);
	}

	public UserModel getCurrentUser() {
		if(user == null){
			user = new UserModel();
			String[] userInfo = pref.getUserInfo();
			user.setUserName(userInfo[0]);
			user.setPhoneNumber(userInfo[1]);
		}
		
		return user;
	}

	public void saveUser(UserModel user) {
		pref.setUserInfo(user.getUserName(), user.getPhoneNumber());
	}
	
	public void login(String phoneNumber, String code, UserModel.Listener listener){
		user.login(phoneNumber, code, listener);
	}

	public void logout(NetworkRequestListener listener) {
		user.logout(listener);
	}
}
