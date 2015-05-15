package com.yesway.izhijia.model.app.internal;

import org.json.JSONException;
import org.json.JSONObject;

import com.telecomsys.autokit.util.JsonUtil.JSONSerializer;

public class Application implements JSONSerializer{
	
	private String id;
	private String classID;
	private String appTitleName;
	private String appName;
	private String size;
	private int appType;
	private String downloadURL;
	private String bestNewVersion;
	private String lowerUpgradeVersion;
	private String pic1URL;
	private String pic1Width;
	private String pic1Height;
	private String pic2URL;
	private String pic2Width;
	private String pic2Height;
	private int isCarLink;
	private int isBuiltin;
	private int isDownload;
	private int isMustNet;
	private int orderID;
	private String addTime;
	private boolean favorite;
	private boolean whiteList;
	private String execfilename;
	
	public interface ApplicationListener{
		public void appClassListener(AppClass [] appClassList);
		public void applicationListener(Application[] applicationList);
	}
	
	public Application(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassID() {
		return classID;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public String getAppTitleName() {
		return appTitleName;
	}

	public void setAppTitleName(String appTitleName) {
		this.appTitleName = appTitleName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getBestNewVersion() {
		return bestNewVersion;
	}

	public void setBestNewVersion(String bestNewVersion) {
		this.bestNewVersion = bestNewVersion;
	}

	public String getLowerUpgradeVersion() {
		return lowerUpgradeVersion;
	}

	public void setLowerUpgradeVersion(String lowerUpgradeVersion) {
		this.lowerUpgradeVersion = lowerUpgradeVersion;
	}

	public String getPic1URL() {
		return pic1URL;
	}

	public void setPic1URL(String pic1url) {
		pic1URL = pic1url;
	}

	public String getPic1Width() {
		return pic1Width;
	}

	public void setPic1Width(String pic1Width) {
		this.pic1Width = pic1Width;
	}

	public String getPic1Height() {
		return pic1Height;
	}

	public void setPic1Height(String pic1Height) {
		this.pic1Height = pic1Height;
	}

	public String getPic2URL() {
		return pic2URL;
	}

	public void setPic2URL(String pic2url) {
		pic2URL = pic2url;
	}

	public String getPic2Width() {
		return pic2Width;
	}

	public void setPic2Width(String pic2Width) {
		this.pic2Width = pic2Width;
	}

	public String getPic2Height() {
		return pic2Height;
	}

	public void setPic2Height(String pic2Height) {
		this.pic2Height = pic2Height;
	}

	public int getIsCarLink() {
		return isCarLink;
	}

	public void setIsCarLink(int isCarLink) {
		this.isCarLink = isCarLink;
	}

	public int getIsBuiltin() {
		return isBuiltin;
	}

	public void setIsBuiltin(int isBuiltin) {
		this.isBuiltin = isBuiltin;
	}

	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public int getIsMustNet() {
		return isMustNet;
	}

	public void setIsMustNet(int isMustNet) {
		this.isMustNet = isMustNet;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public boolean isWhiteList() {
		return whiteList;
	}

	public void setWhiteList(boolean whiteList) {
		this.whiteList = whiteList;
	}
	
	public void setExecfilename(String execfilename) {
		this.execfilename = execfilename;
	}
	public String getExecfilename() {
		return execfilename;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
//			json.put("id", id);
			json.put("appkey", id);
			json.put("classid", classID);
			json.put("apptitlename", appTitleName);
			json.put("appidentifier", appTitleName);
			json.put("appname", appName);
			json.put("filesize", size);
//			json.put("size", size);
			json.put("apptype", appType);
//			json.put("downloadurl", downloadURL);
			json.put("url", downloadURL);
			json.put("bestnewversion", bestNewVersion);
			json.put("lowerupgradeversion", lowerUpgradeVersion);
			json.put("pic1url", pic1URL);
			json.put("pic1width", pic1Width);
			json.put("pic1height", pic1Height);
			json.put("pic2url", pic2URL);
			json.put("pic2width", pic2Width);
			json.put("pic2height", pic2Height);
			json.put("iscarlink", isCarLink);
			json.put("isbuiltin", isBuiltin);
			json.put("isdownload", isDownload);
			json.put("ismustnet", isMustNet);
			json.put("orderid", orderID);
			json.put("addtime", addTime);
			json.put("execfilename", execfilename);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return json;
	}
	
}
