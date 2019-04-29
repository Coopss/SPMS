package com.spms.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.spms.Util;
import com.spms.api.annotations.Secured;
import com.spms.auth.AuthDAO;
import com.spms.auth.AuthUtil;
import com.spms.portfolio.PortfolioDAO;
import com.spms.portfolio.PortfolioValue;
import com.spms.portfolio.Transaction;
import com.spms.ticker.history.TickerHistoryDAO;
import com.spms.ticker.history.TickerHistoryData;
import com.spms.ticker.live.TickerDAO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/portfolio")
public class PortfolioService {
	
	private static final Logger log = LogManager.getLogger(PortfolioService.class);
	private static AuthDAO adao;
	private static PortfolioDAO pdao;
	private static TickerDAO tdao;
	private static TickerHistoryDAO thdao;
	
	static {
      	// Get authdao object
        try {
        	pdao = new PortfolioDAO();
        	adao = new AuthDAO();
        	tdao = new TickerDAO();
        	thdao = new TickerHistoryDAO();
        } catch (Exception e) {
        	e.printStackTrace();
        }        
	}
	
	@Context
	private HttpServletRequest servletRequest;
	
	@POST
	@Secured
	@Path("/add/{symbol}")
	@Operation(summary = "Add (or remove using negative shares) to add to portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response subscribe(@PathParam("symbol") String symbol, @QueryParam("date") String date, @QueryParam("shares") String shares) {
		try {
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			TickerHistoryData thd = thdao.getTickerAtDate(symbol, date);
			Transaction t = new Transaction(user, symbol, date, shares, thd.Close);
			
			pdao.addTransaction(t);
			
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.ok().build();
	}

	@GET
	@Secured
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get a timeseries graph of the portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "JSON timeseries", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getPortfolioSymbols() {
		try {
			Map<String, Object> response = new HashMap<String, Object>();
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			Gson gson = new Gson();
			
			List<PortfolioValue> portfolioValue = pdao.getUserValueOverTime(user);
			Float value = pdao.getUserPortfolio(user).getValue();
			
			response.put("username", user);
			response.put("value", value);
			response.put("timeseries", portfolioValue);
			
			
			return Response.ok(gson.toJson(response)).build();
			
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
//	@GET
//	@Secured
//	@Path("/news")
//	@Operation(summary = "Get relevent news for a user's portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
//	public Response getPortfolioNews() {
//		return Response.ok().build();
//	}
//	
//	@GET
//	@Secured
//	@Path("/getWatchlist")
//	@Operation(summary = "Get relevent news for a user's portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
//	public Response getNews(@PathParam("symbol") String symbol) {
//		return Response.ok().build();
//	}
//	

	
}
