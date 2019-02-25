package com.spms.api.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.spms.database.SPMSDB;

public class AuthDAO {
	
	private Connection conn = null;
	private static String authTableName = "internal.auth";
	
	public AuthDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}

	private String getSalt(Credentials cred) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("select salt from [" + authTableName + "] where username = '" + cred.getUsername() + "'");
		
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	return rs.getString(1);
	    }
	    
	    return null;
	}
	
	public boolean userExists(Credentials cred) throws SQLException {
	    PreparedStatement stmt = conn.prepareStatement("select * from [" + authTableName + "] where username = '" + cred.getUsername() + "'");
	    ResultSet rs = stmt.executeQuery();
		
	    while(!rs.next()) {
	    	return false;
	    }
	        
	    return true;
	}
	
	public boolean createUser(Credentials cred) throws SQLException {
		
		// Dont create and issue credentials if user already exists
		if (userExists(cred)) {
			return false;
		}
		
		String salt = AuthUtil.generateSalt();
		String hash = AuthUtil.getHash(cred.getPassword(), salt);
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("INSERT INTO [" + authTableName + "] (username, hash, salt, issuedToken) VALUES ('" + cred.getUsername() + "', '" + hash + "', '" + salt + "', NULL);");
			
		return true;	
	}

	public boolean authenticate(Credentials cred) throws SQLException {
		// check if user exists, if not, fail auth
		if (!userExists(cred)) {
			return false;
		}
		
		String salt = getSalt(cred);
		String hash = AuthUtil.getHash(cred.getPassword(), salt);
		
		PreparedStatement stmt = conn.prepareStatement("select hash from [" + authTableName + "] where username = '" + cred.getUsername() + "'");
		
		String correctHash = "";
		ResultSet rs = stmt.executeQuery();		
	    
		while(rs.next()) {
	    	correctHash = rs.getString(1);
	    }
		
		if (correctHash.equals(hash)) {
			return true;
		}
		
		return false;
		
	}

	public boolean deleteUser(Credentials cred) throws SQLException {
		// authenticate user first
		if (!authenticate(cred)) {
			return false;
		}
		
		Statement stmt = conn.createStatement();
		Integer deleted = stmt.executeUpdate("delete from [" + authTableName + "] where username = '" + cred.getUsername() + "';");
		
		if (deleted != 0) {
		    return true;
		}
		
		return false;
	}

// todo: rcooper
//	public String issueNewToken(Credentials cred) {
//		
//	}
//	
//	public String getUserForToken(String token) {
//		
//	}
}
