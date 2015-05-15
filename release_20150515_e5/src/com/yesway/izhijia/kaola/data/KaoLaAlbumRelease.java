package com.yesway.izhijia.kaola.data;

public class KaoLaAlbumRelease {
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
//	private int intercutTime;
	private KaoLaHost[] hostList;
	
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getAlbumDescription() {
		return albumDescription;
	}
	public void setAlbumDescription(String albumDescription) {
		this.albumDescription = albumDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public KaoLaHost[] getHostList() {
		return hostList;
	}
	public void setHostList(KaoLaHost[] hostList) {
		this.hostList = hostList;
	}
}
