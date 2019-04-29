package com.spms.auth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;
import com.spms.api.AuthenticationService;

public class AuthUtil {
	
	private static final Logger log = LogManager.getLogger(AuthUtil.class);
	
	public static String generateSalt() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	
	public static String getHash(String password, String salt) {
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    StringBuilder sb = new StringBuilder();
		    byte[] bytes = digest.digest((password + salt).getBytes("UTF-8"));
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
		    return sb.toString();
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
		    return null;
		}
	}
	
	public static String newToken() { 
		try { 
			String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] messageDigest = md.digest(time.getBytes());
			BigInteger no = new BigInteger(1, messageDigest); 
			String hashtext = no.toString(16); 
			             
			while (hashtext.length() < 32) { 
				hashtext = "0" + hashtext; 
			} 
			return hashtext; 
			
		} catch (NoSuchAlgorithmException e) { 
			throw new RuntimeException(e); 
		} 
	} 
	
	
	public static String getUsername(javax.servlet.http.Cookie[] cookies, AuthDAO dao) {
		String token;
		String username = null;
		for (javax.servlet.http.Cookie c : cookies) {
			if (c.getName().equals("token")) {
				token = c.getValue();
				try {
					username = dao.getUserForToken(token);
					if (username == null) {
						continue;
					} else {
						return username;
					}
				} catch (SQLException e) {
					log.error(Util.stackTraceToString(e));
					return null;
				}
			}
		}
		return null;
	}

}
