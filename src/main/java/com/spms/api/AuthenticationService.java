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

import io.swagger.v3.oas.annotations.info.Info;

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
	
	@GET
	@Path("/ping")
	public Response ping() {
        return Response.ok("ok").build();
    }
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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
                        .cookie(new NewCookie("token", token))
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
    public Response isAuthenticated() {
    	return Response.ok().build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
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