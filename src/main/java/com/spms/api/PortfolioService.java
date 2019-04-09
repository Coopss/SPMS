package com.spms.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.spms.api.annotations.Secured;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/portfolio")
public class PortfolioService {

	@POST
	@Secured
	@Path("/purchase/{symbol}")
	@Operation(summary = "Add a certain number of the symbol on a certain date to the users portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response subscribe(@PathParam("symbol") String symbol) {
		return Response.ok().build();
	}
	
	@POST
	@Secured
	@Path("/sell/{symbol}")
	@Operation(summary = "Sell a certain number of symbols on a certain date, update the user's portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401"), @ApiResponse(description = "User does not have specified amount in their portfolio to sell", responseCode = "400") })
	public Response unsubscribe(@PathParam("symbol") String symbol) {
		return Response.ok().build();
	}
	
	@GET
	@Secured
	@Path("/{granularity}")
	@Operation(summary = "Get time-series graph of the user's portfolio as a JSON payload", tags = {"Portfolio"}, description = "Granularity is of: hour, day, week, month, year", responses = {@ApiResponse(description = "JSON timeseries", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getPortfolioSymbols(@PathParam("granularity") String granularity) {
		return Response.ok().build();
	}
	
	@GET
	@Secured
	@Path("/news")
	@Operation(summary = "Get relevent news for a user's portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getPortfolioNews() {
		return Response.ok().build();
	}
	
//	@GET
//	@Secured
//	@Path("/getWatchlist")
//	@Operation(summary = "Get relevent news for a user's portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
//	public Response getNews(@PathParam("symbol") String symbol) {
//		return Response.ok().build();
//	}
//	

	
}
