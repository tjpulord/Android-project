package com.yesway.izhijia.engine;

import java.util.ArrayList;
import java.util.List;

import com.yesway.izhijia.activity.BaseActivity;

import android.content.Context;

public class UiEngine {
	private static UiEngine instance;
	private Context context;
	
	private PreferenceEngine prefEngine;
	private CarEngine carEngine;
	private UserEngine userEngine;
	private KaoLaEngine klEngine;
	
	private ArrayList<BaseActivity> activityList = new ArrayList<BaseActivity>();
	
	private UiEngine(Context ctx){
		this.context = ctx;
	}
	
	public synchronized static UiEngine getInstance(Context ctx){
		if(instance != null){
			return instance;
		}
		if(ctx == null){
			throw new IllegalArgumentException("Context is required for UiEngine's initialization");
		}
		instance = new UiEngine(ctx.getApplicationContext());
		return instance;
	}
	
	public synchronized PreferenceEngine getPreferenceEngine(){
		if(prefEngine == null){
			prefEngine = new PreferenceEngine(context);
		}
		return prefEngine;
	}
	
	public synchronized CarEngine getCarEngine(){
		if(carEngine == null){
			carEngine = new CarEngine(context);
		}
		
		return carEngine;
	}
	
	public synchronized UserEngine getUserEngine(){
		if (userEngine == null){
			userEngine = new UserEngine(getPreferenceEngine());
		}
		return userEngine;
	}
	
	public synchronized KaoLaEngine getKaoLaEngine(){
		if (klEngine == null){
			klEngine = new KaoLaEngine(context, getPreferenceEngine());
		}
		return klEngine;
	}
	
	public void addActivity(BaseActivity act){
		activityList.add(act);
	}
	
	public void removeActivity(BaseActivity act){
		activityList.remove(act);
	}
	
	public List<BaseActivity> getAllActivities(){
		return new ArrayList<BaseActivity>(activityList);
	}
}
