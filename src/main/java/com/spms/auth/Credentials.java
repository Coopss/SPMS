package com.spms.auth;

import java.io.Serializable;

public class Credentials implements Serializable {

    private String username;
    private String password;
    
    public Credentials() {
    	
    }
    
    public Credentials(String uname, String pwd) {
    	this.username = uname;
    	this.password = pwd;
    }
	
    // Setters and getters
    public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}    
}
