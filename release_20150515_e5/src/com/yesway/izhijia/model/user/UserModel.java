package com.yesway.izhijia.model.user;

import com.yesway.izhijia.common.NetworkRequestListener;

public class UserModel {
	private String phoneNumber;
	private String userName;
	
	private UserInfo userInfo;
	private UserService service;

	
	public static interface Listener extends NetworkRequestListener{
		void onUserLogin(UserInfo info);
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void requestVerifyCode(final String phone,final NetworkRequestListener listener) {
		if(service == null){
			service = UserService.createUserService();
		}
		service.requestVerifyCode(phone, listener);
	}
	
	public void login(String phoneNumber, String code,  final Listener listener){
		if(service == null){
			service = UserService.createUserService();
		}
		service.login(phoneNumber, code, listener);
	}
	
	public void setUserInfo(UserInfo info){
		this.userInfo = info;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	public void logout(NetworkRequestListener listener) {
		if(service == null){
			service = UserService.createUserService();
		}
		service.logout(listener);
	}
}
