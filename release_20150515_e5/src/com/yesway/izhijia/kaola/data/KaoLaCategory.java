package com.yesway.izhijia.kaola.data;

import org.json.JSONException;
import org.json.JSONObject;

public class KaoLaCategory {
	private String cid;
	private String name;
	private String logo;
	private String hasSub;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getHasSub() {
		return hasSub;
	}
	public void setHasSub(String hasSub) {
		this.hasSub = hasSub;
	}
	
	public KaoLaCategory fromJSONObject(JSONObject jsonObj, String charset){
		if(jsonObj == null){
			return this;
		}
		try {
			if (jsonObj.has("cid")) {
				cid = jsonObj.getString("cid");
			}
			
			if(jsonObj.has("name")){
				name = jsonObj.getString("name");
			}
			
			if(jsonObj.has("logo")){
				logo = jsonObj.getString("logo");
			}
			
			if(jsonObj.has("hassub")){
				hasSub = jsonObj.getString("hassub");
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
}
