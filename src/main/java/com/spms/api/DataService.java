package com.spms.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.spms.api.annotations.Secured;
import com.spms.ticker.tools.Requests;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/symbol")
public class DataService {
	@GET
	@Secured
	@Path("/{symbol}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get overview (descriptive) details of a symbol", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response get(@PathParam("symbol") String symbol) {
		String resp = Requests.followRedirect("https://api.iextrading.com/1.0/stock/"+symbol+"/chart/1d");
		return Response.ok(resp).build();
	}
	
	@GET
	@Secured
	@Path("/{symbol}/news")
	@Operation(summary = "Get relevent news for a symbol", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getNews(@PathParam("symbol") String symbol) {
		return Response.ok().build();
	}
	
	@GET
	@Secured
	@Path("/{symbol}/metrics")
	@Operation(summary = "Get performance metrics for a symbol", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getMetrics(@PathParam("symbol") String symbol) {
		return Response.ok().build();
	}
	
	@GET
	@Secured
	@Path("/topmovers/{granularity}")
	@Operation(summary = "Get top movers over a given time granularity", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response topMovers(@PathParam("granularity") String symbol) {
		return Response.ok().build();
	}
	
}
