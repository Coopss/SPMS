package com.spms.api;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
 
@Path("/ping")
public class APIPingService {
	@GET
	public Response ping() {
        return Response.ok("ok").build();
    }
 
}