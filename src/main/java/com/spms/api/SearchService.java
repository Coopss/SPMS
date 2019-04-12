package com.spms.api;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;
import com.spms.api.annotations.Secured;
import com.spms.auth.AuthDAO;
import com.spms.ticker.Symbol;
import com.spms.ticker.SymbolDAO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/search")
public class SearchService {
	private static final Logger log = LogManager.getLogger(SearchService.class);
	private static SymbolDAO dao;
	static {
      	// Get authdao object
        try {
             dao = new SymbolDAO();
        } catch (Exception e) {
        	e.printStackTrace();
        }        
	}
		
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Search for a term. Returns list of possible matches", tags = {"Search"}, description = "Search for a term, my be a sector, industry, company name, ticker, or any other potential search term", responses = {@ApiResponse(description = "JSON Array of results [[ticker, name, sector], [ticker, name, sector], ... ]]", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response search(@QueryParam("q") String query) {
		List<Symbol> matchedSyms;
		StringBuilder sb = new StringBuilder();
		try {
			 matchedSyms = dao.search(query);
		} catch (SQLException e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		log.info("Search for query '" + query + "' returned " + matchedSyms.size() + " results");
		sb.append("[");
		
		for (Symbol sym : matchedSyms) {
			sb.append("[\"" + sym.Symbol + "\", \"" + sym.Name + "\", \"" + sym.Sector + "\"],");
		}
		sb.setLength(sb.length() - 1); // remove last character
		sb.append("]");
		
		return Response.ok(sb.toString()).build();
	}
		
}
