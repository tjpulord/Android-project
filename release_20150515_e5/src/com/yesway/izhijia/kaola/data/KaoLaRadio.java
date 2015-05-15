package com.yesway.izhijia.kaola.data;

public class KaoLaRadio {
	private String rid;
	private String name;
	private String description;
	private String logo;
	private String cover;

	private KaoLaHost[] hostList;

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public KaoLaHost[] getHostList() {
		return hostList;
	}

	public void setHostList(KaoLaHost[] hostList) {
		this.hostList = hostList;
	}
}
