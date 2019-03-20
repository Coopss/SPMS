package com.spms.api.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.api.annotations.Secured;
import com.spms.auth.AuthDAO;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private static final Logger log = LogManager.getLogger(AuthenticationFilter.class);
	private static AuthDAO dao;
    private static final String TOKENCOOKIENAME = "token";
    
    @Context
    private HttpServletRequest servletRequest;
    
	static {
      	// Get authdao object
        try {
             dao = new AuthDAO();
        } catch (Exception e) {
        	e.printStackTrace();
        }        
	}
	

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	Map<String, Cookie> cookies = requestContext.getCookies();
    	
    	
    	if (!cookies.containsKey(TOKENCOOKIENAME)) {
    		log.info("Failed authentication from " + servletRequest.getRemoteAddr() + " - Token cookie not present");
            abortWithUnauthorized(requestContext);
            return;
    	}
    	
    	String token = cookies.get(TOKENCOOKIENAME).getValue();
    	
        try {
        	validateToken(token);
        	return; 
        } catch (Exception e) {
        	log.info("Failed authentication from " + servletRequest.getRemoteAddr() + " - Invalid token");
            abortWithUnauthorized(requestContext);
            return;
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        // Abort the filter chain with a 401 status code response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .build());
    }

    private void validateToken(String token) throws Exception {
		String username = dao.getUserForToken(token);
    	if (username == null) {
    		throw new Exception();
    	}

    	log.info("Validated user \"" + username + "\" with token: \"" + token + "\"");
		
    }
}