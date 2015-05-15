package com.yesway.izhijia.model.app.internal;

import org.json.JSONException;
import org.json.JSONObject;

public class UserApplication extends Application {
	
	public UserApplication(){
		super();
	}
	
	private String lastVersion;

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}
	
	public JSONObject toJSON(){
		JSONObject js = super.toJSON();
		if(js == null){
			return null;
		}
		try {
			js.put("lastversion", lastVersion);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return js;
	}
}
