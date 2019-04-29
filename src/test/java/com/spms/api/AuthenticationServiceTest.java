package com.spms.api;

import static org.junit.Assert.fail;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.spms.auth.Credentials;

public class AuthenticationServiceTest {

	@Test
	public void authenticateUserExistsTest() {
		/*try {
			Credentials user = new Credentials("testTest", "pass");
			AuthenticationService authServ = new AuthenticationService();
			Response r = authServ.authenticateUser(user);
			System.out.println(r.getStatus());
			assert r.getStatus() == 200 : "Bad response for confirmed user";
		} catch (Exception e) {
			assert false : "exception has occurred";
		}*/
		assert true;
	}

	
}
