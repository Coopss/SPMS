package com.spms.auth;

import java.sql.SQLException;

import org.junit.Test;

public class AuthDAOTest {
	
	@Test 
	public void createUserTest() {
		try {
    		AuthDAO dao = new AuthDAO();
    		Credentials tmp = new Credentials("testAuth", "testing");
    		
    		if (dao.userExists(tmp) == false) {
    			assert dao.createUser(tmp) == true : "The user does not exist";
    		} else {
    			assert dao.createUser(tmp) == false : "The user exists";
    		}
		} catch (Exception e) {
			assert false : "An exception has occurred";
		}
	}
	
	@Test 
	public void deleteUserTest() {
		try {
    		AuthDAO dao = new AuthDAO();
    		Credentials tmp = new Credentials("testAuth", "testing");
    		
    		if (dao.userExists(tmp) == true) {
    			assert dao.deleteUser(tmp) == true : "The user does not exist";
    		} else {
    			assert dao.deleteUser(tmp) == false : "The user exists";
    		}
		} catch (Exception e) {
			assert false : "An exception has occurred";
		}
	}
	
	@Test
	public void authenticateCorrectCredTest() {
		// test if the user can be authenticated with the right credentials
        try {
    		AuthDAO dao = new AuthDAO();
    		Credentials tmp = new Credentials("testAuth", "testing");
    		if (dao.userExists(tmp))
				assert dao.authenticate(tmp) == true : "The user cannot be authenticated with right credentials";
			else
				assert dao.authenticate(tmp) == false : "Non-existent user was authenticated";
		} catch (SQLException e) {
			assert false : "An exception as occurred";
		}  
	}
	
	@Test
	public void authenticateIncorrectCredTest() {
		// test if the user can be authenticated with the right credentials
        try {
    		AuthDAO dao = new AuthDAO();
    		Credentials tmp = new Credentials("testAuth", "wrong");
    		
    		if (dao.userExists(tmp))
				assert dao.authenticate(tmp) == false : "The user was authenticated with wrong credentials";
			else
				assert dao.authenticate(tmp) == false : "Non-existent user";
		} catch (SQLException e) {
			assert false : "An exception as occurred";
		}  
	}

}
