package com.yesway.izhijia.kaola.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.telecomsys.autokit.util.JsonUtil.JSONSerializer;

public class KaoLaHost implements JSONSerializer{
	private String name;
	private String description;
	private String img;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public KaoLaHost fromJSONObject(JSONObject jsonObj,String charset) {
		if(jsonObj == null){
			return this;
		}
		try{
			if (jsonObj.has("name")) {
				name = jsonObj.getString("name");
			}
			
			if (jsonObj.has("description")) {
				description = jsonObj.getString("description");
			}
			
			if (jsonObj.has("img")) {
				img = jsonObj.getString("img");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("name", name);
			jObject.put("description", description);
			jObject.put("img", img);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObject;
	}
}
