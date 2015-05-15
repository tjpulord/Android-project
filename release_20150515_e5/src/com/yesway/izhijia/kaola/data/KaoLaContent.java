package com.yesway.izhijia.kaola.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.telecomsys.autokit.util.JsonUtil.JSONSerializer;

public class KaoLaContent implements JSONSerializer{
	private String cid;
	private String type;
	private String name;
	private String img;
	private String description;
	private String categoryId;
	private String categoryName;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public KaoLaContent fromJSONObject(JSONObject jsonObj, String charset){
		if(jsonObj == null){
			return this;
		}
		try{
			if (jsonObj.has("cid")) {
				cid = jsonObj.getString("cid");
			}
			
			if (jsonObj.has("type")) {
				type = jsonObj.getString("type");
			}
			
			if (jsonObj.has("name")) {
				name = jsonObj.getString("name");
			}
			
			if (jsonObj.has("img")) {
				img = jsonObj.getString("img");
			}
			
			if (jsonObj.has("description")) {
				description = jsonObj.getString("description");
			}
			
			if (jsonObj.has("categoryId")) {
				categoryId = jsonObj.getString("categoryId");
			}
			
			if (jsonObj.has("categoryName")) {
				categoryName = jsonObj.getString("categoryName");
			}
			
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	
	@Override
	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("cid", cid);
			jsonObject.put("type", type);
			jsonObject.put("name", name);
			jsonObject.put("img", img);
			jsonObject.put("description", description);
			jsonObject.put("categoryId", categoryId);
			jsonObject.put("categoryName", categoryName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return jsonObject;
	}
}
