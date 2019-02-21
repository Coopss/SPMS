package com.spms.api.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthUtil {
	
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
}
