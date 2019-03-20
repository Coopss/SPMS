package com.spms.api;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

 
@Path("/ping")
public class APIPingService {
	@GET
	@Operation(summary = "Checks if the API is online.", tags = {"API"}, description = "Will fail to connect if service is down.", responses = {@ApiResponse(description = "string: \"ok\"", responseCode = "200")})
	public Response ping() {
        return Response.ok("ok").build();
    }
 
}