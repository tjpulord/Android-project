package com.yesway.izhijia.kaola.data;

public class KaoLaAudio {
	private String id;
	private String title;
	private String description;
	private String albuId;
	private String albumName;
	private String albumDescription;
	private String cover;
	private String playurl;
	private String duration;
	private String categoryId;
	private String clockId;
	private KaoLaHost[] hostList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getAlbuId() {
		return albuId;
	}
	public void setAlbuId(String albuId) {
		this.albuId = albuId;
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
	public KaoLaHost[] getHostList() {
		return hostList;
	}
	public void setHostList(KaoLaHost[] hostList) {
		this.hostList = hostList;
	}
}
