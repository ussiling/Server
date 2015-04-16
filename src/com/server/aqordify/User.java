package com.server.aqordify;

public class User {
	private long id;
	private String firstName, lastName, voice;
	private String[] phone, email;
	private int[] admin, member;

	
	public User(long id, String firstName, String lastName, String voice, String[] phone, String[] email, int[] admin, int[] member){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.voice = voice;
		this.phone = phone;
		this.email = email;
		this.admin = admin;
		this.member = member;
		
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getVoice() {
		return voice;
	}


	public void setVoice(String voice) {
		this.voice = voice;
	}


	public String[] getPhone() {
		return phone;
	}


	public void setPhone(String[] phone) {
		this.phone = phone;
	}


	public String[] getEmail() {
		return email;
	}


	public void setEmail(String[] email) {
		this.email = email;
	}


	public int[] getAdmin() {
		return admin;
	}


	public void setAdmin(int[] admin) {
		this.admin = admin;
	}


	public int[] getMember() {
		return member;
	}


	public void setMember(int[] member) {
		this.member = member;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}



}