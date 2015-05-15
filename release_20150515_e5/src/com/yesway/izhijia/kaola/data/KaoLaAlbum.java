package com.yesway.izhijia.kaola.data;

public class KaoLaAlbum {
	private String aid;
	private String name;
	private String cover;
	private String description;
	private KaoLaHost[] hostList;
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
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
