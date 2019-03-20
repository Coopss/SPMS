package com.spms.api;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/")
public class SwaggerRedirectService {
	
	@GET
	@Operation(summary = "Redirect Service for Swagger-UI", tags = {"API"}, description = "Redirects incoming requests to /SPMS/api to /SPMS/swagger-ui", responses = {@ApiResponse(description = "Redirect to /SPMS/swagger-ui.", responseCode = "301")})
	public Response get() throws URISyntaxException {
		return Response.temporaryRedirect(new URI("/SPMS/swagger-ui")).build();
	}
	
}
