package com.yesway.izhijia.engine;


import android.telephony.TelephonyManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;

import com.yesway.izhijia.R;
import com.yesway.izhijia.kaola.KaoLaModel;
import com.yesway.izhijia.kaola.KaoLaModel.ActivateListener;
import com.yesway.izhijia.kaola.KaoLaModel.KaoLaInformationDownloadListener;
import com.yesway.izhijia.kaola.data.KaoLaCategory;
import com.yesway.izhijia.kaola.data.KaoLaContent;
import com.yesway.izhijia.kaola.data.KaoLaRadioItem;
import com.yesway.izhijia.activity.MainPanelActivity;
import com.yesway.izhijia.activity.ScreenLock;
import com.yesway.izhijia.apptoolkit.CarAppController;
import com.yesway.izhijia.apptoolkit.CarAppController.CarAppListener;

public class KaoLaEngine implements ActivateListener,CarAppListener,KaoLaInformationDownloadListener {
	private KaoLaModel model;
	private PreferenceEngine pref;
	private Context ctx;
	private MediaPlayer mp;
	private CarAppController carController;
	private Activity screenLockActivity = null;
	
	KaoLaEngine(Context context, PreferenceEngine pref){
		model = new KaoLaModel();
		this.ctx = context;
		this.pref = pref;
		model.setOpenId(this.pref.getOpenId());
	}

	public void syncToDevice(){
		if(carController == null){
			carController = new CarAppController();
			String[] keys = {	ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYSTART),
								ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYSTOP),
								ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYPAUSE),
								ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYRESUME),
								ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTCATEGORY),
								ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTCONTENT),
								ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTPLAYLIST)};
			carController.init("kaolafm", ctx, this, keys);
		}	
	}
	
	public void setScreenLockActivity(Activity screenLockActivity) {
		this.screenLockActivity = screenLockActivity;
	}
	
	public Activity getScreenLockActivity() {
		return screenLockActivity;
	}
	
	public void onCarAppStarted() {
		System.out.println("car app started");

		Intent screenLockIntent = new Intent(ctx, ScreenLock.class);
		screenLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(screenLockIntent);

	}
	public void onCarAppStopped() {
		if (mp!= null){
			if (mp.isPlaying()){
				mp.stop();
			}
			mp.reset();
			mp.release();
			mp = null;
		}
		// finish the screenlock activity
		if(screenLockActivity != null){
			screenLockActivity.finish();
			screenLockActivity = null;
		}
		System.out.println("car app stopped");
	}
	public void onMessageReceived(String key, String value) {
		String imei = ((TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYSTART))){
			String url = value;
			if(mp != null){
				//mp.stop();
				mp.reset();
				mp.release();
				mp = null;
			}
			mp = playAudioByURL(url, null);
		}
		else if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYSTOP))){
			if(mp != null){
				//mp.stop();
				mp.reset();
				mp.release();
				mp = null;
			}
		}
		else if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYPAUSE))){
			if(mp != null){
				mp.pause();
			}
		}
		else if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_PLAYRESUME))){
			if(mp != null){
				mp.start();
			}
		}
		else if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTCATEGORY))){
			getAllCategories(imei, this);
		}
		else if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTCONTENT))){
			String categoryID = value;
			getContentsByCategoryId(imei, categoryID, this);
		}
		else if(key.equals(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTPLAYLIST))){
			String radioID = value;
			getRadioPlayListById(imei, radioID, this);
		}
	}
	
	public void activiateDevice(String deviceId){
		String openId = model.getOpenId();
		if(openId != null && openId.length() > 0){
			return;
		}
		model.activiateDevice(deviceId, this);
	}
	
	
	public void getAllCategories(final String deviceId, final KaoLaInformationDownloadListener listener){
		String openId = model.getOpenId();
		
		if(openId == null || openId.isEmpty()){
			model.activiateDevice(deviceId, new ActivateAdapter(){
				@Override
				public void onDeviceActiviated(String openId) {
					model.setOpenId(openId);
					pref.setOpenId(openId);
					doGetAllCategories(deviceId,listener);
				}	
			});
		}else{
			doGetAllCategories(deviceId, listener);
		}
	}
	
	
	public void getContentsByCategoryId(final String deviceId, final String cid,  final KaoLaInformationDownloadListener listener){
		
		String openId = model.getOpenId();
		
		if(openId == null || openId.isEmpty()){
			model.activiateDevice(deviceId, new ActivateAdapter(){
				@Override
				public void onDeviceActiviated(String openId) {
					model.setOpenId(openId);
					pref.setOpenId(openId);
					doGetContentsByCategoryId(deviceId,cid, listener);
				}	
			});
		}else{
			doGetContentsByCategoryId(deviceId,cid, listener);
		}	
	}
	
	public void getRadioPlayListById(final String deviceId, final String radio,  final KaoLaInformationDownloadListener listener){
		
		String openId = model.getOpenId();
		
		if(openId == null || openId.isEmpty()){
			model.activiateDevice(deviceId, new ActivateAdapter(){
				@Override
				public void onDeviceActiviated(String openId) {
					model.setOpenId(openId);
					pref.setOpenId(openId);
					doGetRadioPlayListById(deviceId,radio, listener);
				}	
			});
		}else{
			doGetRadioPlayListById(deviceId,radio, listener);
		}	
	}
	
	public void onKaoLaCategories(String response){
		carController.sendMessage(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTCATEGORY), response);
	}
	
	public void onKaoLaContents(String response){
		carController.sendMessage(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTCONTENT), response);
	}
	
	public void onKaoLaRadioPlayList(String response){
		carController.sendMessage(ctx.getResources().getString(R.string.KAOLAFM_MSG_KEY_REQUESTPLAYLIST), response);
	}
	
	protected void doGetRadioPlayListById(String deviceId, String radio,
			KaoLaInformationDownloadListener listener) {
		model.getRadioPlayListById(deviceId, radio, listener);		
	}

	private void doGetContentsByCategoryId(String deviceId, String cid,
			KaoLaInformationDownloadListener listener) {
		model.getContentsByCategoryId(deviceId, cid, listener);		
	}

	private void doGetAllCategories(String deviceId,KaoLaInformationDownloadListener listener){
		model.getAllCategories(deviceId,listener);
	}
	
	private class ActivateAdapter implements ActivateListener{
		@Override
		public void onRequestStart() {
			
		}

		@Override
		public void onRequestProgress() {
			
		}

		@Override
		public void onRequestFailed() {
			
		}

		@Override
		public void onRequestComplete() {
			
		}

		@Override
		public void onRequestCanceled() {
			
		}

		@Override
		public void onDeviceActiviated(String openId) {
		}	
	}
	

	@Override
	public void onRequestStart() {
		
	}

	@Override
	public void onRequestProgress() {
		
	}

	@Override
	public void onRequestFailed() {
		
	}

	@Override
	public void onRequestComplete() {
		
	}

	@Override
	public void onRequestCanceled() {
		
	}

	@Override
	public void onDeviceActiviated(String openId) {
		model.setOpenId(openId);
		this.pref.setOpenId(openId);
	}
	
	public static interface AudioPlayListener{
		void onPlayStarted();
		void onPlayCompleted();
		void onBufferingUpdate(int percent);
	}
	
	public static MediaPlayer playAudioByURL(final String url, final AudioPlayListener listener){
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener(){
				@Override
				public void onPrepared(MediaPlayer mp) {
					if(mp != null){
						mp.start();
						if(listener != null){
							listener.onPlayStarted();
						}
					}					
				}});
			mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener(){
				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {
					if(percent == 100 && mp!= null && mp.isPlaying()){
						mp.start();
					}
					
					if(listener != null){
						listener.onBufferingUpdate(percent);
					}
				}});
			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					if(mp != null){
						mp.reset();
						mp.release();
					}
					
					if(listener != null){
						listener.onPlayCompleted();
					}
				}});
			
			mediaPlayer.setOnInfoListener(new OnInfoListener(){

				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					return false;
				}});
			
			mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener(){
				@Override
				public void onSeekComplete(MediaPlayer mp) {
				}});
			
			mediaPlayer.prepareAsync();
			return mediaPlayer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
