package com.example.edu.ksu.crop;

public class User {
	
	public String username;
	protected String email;
	
	
	public User(String username, String email){
		this.username = username;
		this.email = email;
	}
	
	public String getUsername(){
		return username.toString();
	}

}