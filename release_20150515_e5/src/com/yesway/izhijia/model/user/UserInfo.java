package com.yesway.izhijia.model.user;

import com.yesway.izhijia.model.user.internal.UserProduct;

public class UserInfo {
	private String zjid;
	private String username;
	private String email;
	private String fullname;
	private int gender;
	private String personid;
	private String birthday;
	private String headphoto;
	private String country;
	private String province;
	private String city;
	private String county;
	private String tel1;
	private String tel2;
	private String tel3;
	private UserProduct productInfo;
	private String[] bindphone;
	private String machinephone;
	public String getZjid() {
		return zjid;
	}
	public void setZjid(String zjid) {
		this.zjid = zjid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getPersonid() {
		return personid;
	}
	public void setPersonid(String personid) {
		this.personid = personid;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHeadphoto() {
		return headphoto;
	}
	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getTel3() {
		return tel3;
	}
	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}
	public UserProduct getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(UserProduct productInfo) {
		this.productInfo = productInfo;
	}
	public String[] getBindphone() {
		return bindphone;
	}
	public void setBindphone(String[] bindphone) {
		this.bindphone = bindphone;
	}
	public String getMachinephone() {
		return machinephone;
	}
	public void setMachinephone(String machinephone) {
		this.machinephone = machinephone;
	}
	public String toString() {
		return "zjid:" + zjid + ", username:" + username + ", personid:"
				+ personid + ", city:" + city + ", county:" + county
				+ ", tel1:" + tel1 + ", machinephone:" + machinephone;
	}
}