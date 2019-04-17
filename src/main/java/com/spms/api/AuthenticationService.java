package com.spms.api;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;
import com.spms.api.annotations.Secured;
import com.spms.auth.AuthDAO;
import com.spms.auth.Credentials;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;

@Path("/authenticate")
public class AuthenticationService extends Application {
	private static final Logger log = LogManager.getLogger(AuthenticationService.class);
	private static AuthDAO dao;
	static {
      	// Get authdao object
        try {
             dao = new AuthDAO();
        } catch (Exception e) {
        	e.printStackTrace();
        }        
	}
	
	@Context
	private HttpServletRequest servletRequest;
	
	@GET
	@Path("/ping")
	@Operation(summary = "Checks if the Authentication Service is online.", tags = {"Authentication"}, description = "Pings the Authentication service, will fail to connect if service is down.", responses = {@ApiResponse(description = "string: \"ok\"", responseCode = "200")})
	public Response ping() {
        return Response.ok("ok").build();
    }
	
	@GET
	@Secured
	@Path("/getUsername")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get Username of Logged-In User", tags = {"Authentication"}, description = "Secured endpoint, gets username of user via cookie", responses = {@ApiResponse(description = "{\"username\":\"<CLIENT-USER-NAME>\"}", responseCode = "200"), @ApiResponse(description = "Somehow, the client got past auth without token", responseCode = "500"),@ApiResponse(description = "User credentials could not be confirmed.", responseCode = "401")})
	public Response getUsername() {
		String token;
		String username = null;
		javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
		for (javax.servlet.http.Cookie c : cookies) {
			if (c.getName().equals("token")) {
				token = c.getValue();
				try {
					username = dao.getUserForToken(token);
					if (username == null) {
						continue;
					} else {
						return Response.ok("{\"username\":\"" + username + "\"}").build();
					}
				} catch (SQLException e) {
					log.error(Util.stackTraceToString(e));
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
				}
			}
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Authenticate a User", tags = {"Authentication"}, description = "Authenticate a user given a JSON payload of credentials. No direct response, but a cookie with key=\"token\" is set with a time-expiring authentication token. Resupply in subsequent requests", responses = {@ApiResponse(description = "Sets cookie \"token\".", responseCode = "200"), @ApiResponse(description = "User credentials could not be confirmed.", responseCode = "401")})
    public Response authenticateUser(Credentials cred) {
    	Boolean valid = false;
        try {
            // Authenticate the user using the credentials provided
        	valid = dao.authenticate(cred);
        	if (valid) {
                // Issue a token for the user
                String token = dao.issueNewToken(cred);
                
                // Return the token on the response
                return Response.ok()
                		// THIS FOR DEPLOYMENT
//                		.header("Set-Cookie", "token=" + token + ";lang=en-US; Path=/; Domain="+Util.removeProtocol(servletRequest.getHeader("origin")))
                		// THIS FOR LOCALHOST
                		.header("Set-Cookie", "token=" + token + ";lang=en-US; Path=/; Domain=localhost")
                        .build();
        	} else {
        		return Response.status(Response.Status.UNAUTHORIZED).build();
        	}

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }      
    }
    
    @Secured
    @GET
    @Path("/checkAuth")
	@Operation(summary = "Checks if the User is Authenticated", tags = {"Authentication"}, description = "Used to test the @Secured decorator to protect endpoints, primarily used for debugging.", responses = {@ApiResponse(description = "User is authorized.", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
    public Response isAuthenticated() {
    	return Response.ok().build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
	@Operation(summary = "Register a new User", tags = {"Authentication"}, description = "Registers a new user with a JSON payload of username and password. Will always succeed if username is not taken.", responses = {@ApiResponse(description = "User successfully created", responseCode = "201"), @ApiResponse(description = "User not created, likely username taken", responseCode = "400"), @ApiResponse(description = "Internal server error, indicative of more problematic issue.", responseCode = "500")})
    public Response registerUser(Credentials credentials) {
    	Boolean status;
		try {
			status = dao.createUser(credentials);
		} catch (SQLException e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		if (status) {
			return Response.status(Response.Status.CREATED).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
    }
}