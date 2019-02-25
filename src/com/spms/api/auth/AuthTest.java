package com.spms.api.auth;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.spms.database.SPMSDB;

public class AuthTest {

	public static void main(String[] args) throws SQLException {
//		Database.getConnection();
		
		Credentials tmp = new Credentials();
		tmp.setUsername("test");
		tmp.setPassword("abcd");
		
		
		AuthDAO dao = new AuthDAO();
		System.out.println("User exists before creation: " + dao.userExists(tmp));
		System.out.println("User deleted with correct credentials: " + dao.deleteUser(tmp));
		System.out.println("User has been created: " + dao.createUser(tmp));
		System.out.println("User exists after creation: " + dao.userExists(tmp));
		
		System.out.println("User can be succussfully authenticated: " + dao.authenticate(tmp));
		tmp.setPassword("abcde");
		System.out.println("User is authenticated with incorrect credentials: " + dao.authenticate(tmp));
		System.out.println("User deleted with incorrect credentials: " + dao.deleteUser(tmp));
		
	}

}
