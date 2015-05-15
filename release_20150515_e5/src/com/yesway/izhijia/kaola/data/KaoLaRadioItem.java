package com.yesway.izhijia.kaola.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.telecomsys.autokit.util.JsonUtil;
import com.telecomsys.autokit.util.JsonUtil.JSONSerializer;

public class KaoLaRadioItem implements JSONSerializer{
	private String aid;
	private String title;
	private String description;
	private String albumId;
	private String albumName;
	private String albumDescription;
	private String cover;
	private String playurl;
	private String duration;
	private String categoryId;
	private String clockId;
	private String intercutTime;
	private String publishDate;
	private int number;
	private JSONArray klHostArray;
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getAlbumDescription() {
		return albumDescription;
	}
	public void setAlbumDescription(String albumDescription) {
		this.albumDescription = albumDescription;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getPlayurl() {
		return playurl;
	}
	public void setPlayurl(String playurl) {
		this.playurl = playurl;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getClockId() {
		return clockId;
	}
	public void setClockId(String clockId) {
		this.clockId = clockId;
	}
	public void setKlHostArray(JSONArray klHostArray) {
		this.klHostArray = klHostArray;
	}
	
	public KaoLaRadioItem fromJSONObject(JSONObject jsonObj, String charset){
		if(jsonObj == null){
			return this;
		}
		
		try{
			if (jsonObj.has("aid")) {
				aid = jsonObj.getString("aid");
			}
			
			if (jsonObj.has("title")) {
				title = jsonObj.getString("title");
			}
			if (jsonObj.has("description")) {
				description = jsonObj.getString("description");
			}
			if (jsonObj.has("albumId")) {
				albumId = jsonObj.getString("albumId");
			}
			if (jsonObj.has("albumName")) {
				albumName = jsonObj.getString("albumName");
			}
			if (jsonObj.has("albumDescription")) {
				albumDescription = jsonObj.getString("albumDescription");
			}
			if (jsonObj.has("cover")) {
				cover = jsonObj.getString("cover");
			}
			if (jsonObj.has("playurl")) {
				playurl = jsonObj.getString("playurl");
			}
			if (jsonObj.has("duration")) {
				duration = jsonObj.getString("duration");
			}
			if (jsonObj.has("categoryId")) {
				categoryId = jsonObj.getString("categoryId");
			}
			if (jsonObj.has("clockId")) {
				clockId = jsonObj.getString("clockId");
			}
			if(jsonObj.has("intercutTime")){
				intercutTime = jsonObj.getString("intercutTime");
			}
			if (jsonObj.has("number")){
				number = jsonObj.getInt("number");
			}
			if(jsonObj.has("publishDate")){
				publishDate = jsonObj.getString("publishDate");
				if(publishDate == "null"){
					publishDate = null;
				}
			}
			if(jsonObj.has("hostList")){
				JSONArray hosts = jsonObj.getJSONArray("hostList");
				klHostArray = hosts;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return this;
	}
	@Override
	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("aid", aid);
			jsonObject.put("title", title);
			jsonObject.put("description", "");
//			jsonObject.put("description", description);
			jsonObject.put("albumId", albumId);
			jsonObject.put("albumName", albumName);
			jsonObject.put("albumDescription", albumDescription);
			jsonObject.put("cover", cover);
			jsonObject.put("duration", duration);
			jsonObject.put("playurl", playurl);
			jsonObject.put("categoryId", categoryId);
			jsonObject.put("clockId", clockId);
			jsonObject.put("intercutTime", intercutTime);
			jsonObject.put("number", number);
			jsonObject.put("publishDate", publishDate);
			if(klHostArray != null){
//				JSONArray ja = new JSONArray(JsonUtil.ListToJSON(klHost));
				// hostList中存在非utf8字符，暂时去掉hostlist属性
				jsonObject.accumulate("hostList", new JSONArray());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return jsonObject;
	}
}
