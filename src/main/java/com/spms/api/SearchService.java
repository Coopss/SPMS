package com.spms.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.spms.api.annotations.Secured;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/search")
public class SearchService {
	
	@GET
	@Secured
	@Path("/{term}")
	@Operation(summary = "Search for a term. Returns list of possible matches", tags = {"Search"}, description = "Search for a term, my be a sector, industry, company name, ticker, or any other potential search term", responses = {@ApiResponse(description = "JSON Array of results {[{result1}, {result2}, ... ]}", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response search(@PathParam("term") String symbol) {
		return Response.ok().build();
	}
		
}
