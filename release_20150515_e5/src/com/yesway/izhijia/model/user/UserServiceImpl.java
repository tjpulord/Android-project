package com.yesway.izhijia.model.user;

import com.yesway.izhijia.common.NetworkRequestListener;
import com.yesway.izhijia.model.BaseAsyncTask;
import com.yesway.izhijia.model.user.UserModel.Listener;
import com.yesway.izhijia.model.user.internal.UserManager;


class UserServiceImpl extends UserService {
	
	public UserServiceImpl() {
	}

	@Override
	public void requestVerifyCode(final String phoneNumber,final NetworkRequestListener listener) {
		downloadTask = new BaseAsyncTask(listener) {		
			@Override
			protected Integer doInBackground(String... params) {
				if(listener != null)
					listener.onRequestProgress();
				boolean isSuccess = UserManager.instance().sendMobileCode(phoneNumber);
				
				return isSuccess ? 0 : -1;
			}
		};
		downloadTask.execute();
	}

	@Override
	public void login(final String phoneNumber, final String code,final Listener listener ) {
		downloadTask = new BaseAsyncTask(listener) {		
			@Override
			protected Integer doInBackground(String... params) {
				if(listener != null)
					listener.onRequestProgress();
				UserInfo info = UserManager.instance().login(phoneNumber, code);
				
				if(info != null){
					if(listener != null){
						((Listener)listener).onUserLogin(info);
					}
					return 0;
				}else{
					return -1;
				}
				
			}
		};
		downloadTask.execute();
	}

	@Override
	public void logout(NetworkRequestListener listener) {
		downloadTask = new BaseAsyncTask(listener) {		
			@Override
			protected Integer doInBackground(String... params) {
				super.doInBackground(params);
				if(listener != null){
					listener.onRequestProgress();
				}
				boolean isSuccess = UserManager.instance().logout();
				return isSuccess ? 0 : -1;
			}
		};
		downloadTask.execute();
	}
}
