package com.server.aqordify;

public class Quire {
	private String name, city, adminName;
	private int adminId;
	
	public Quire(String name, String city, int adminId, String adminName){
		this.name = name;
		this.city = city;
		this.adminId = adminId;
		this.adminName = adminName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	
}
