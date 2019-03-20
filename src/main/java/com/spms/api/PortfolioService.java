package com.spms.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/portfolio")
public class PortfolioService {

	@GET
	@Path("/subscribe/{symbol}")
	public Response subscribe(@PathParam("symbol") String symbol) {
		return Response.ok().build();
	}
	
	@GET
	@Path("/{user}")
	public Response getPortfolioSymbols(@PathParam("user") String user) {
		return Response.ok().build();
	}
	
}
