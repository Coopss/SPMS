package com.spms.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.spms.news.NewsArticle;
import com.spms.news.TickerNewsDAO;
import com.spms.portfolio.Portfolio;
import com.spms.portfolio.PortfolioConstraintException;
import com.spms.portfolio.PortfolioDAO;
import com.spms.portfolio.PortfolioValue;
import com.spms.portfolio.PurchaseModel;
import com.spms.portfolio.Transaction;
import com.spms.ticker.history.TickerHistoryDAO;
import com.spms.ticker.history.TickerHistoryData;
import com.spms.ticker.live.TickerDAO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/portfolio")
public class PortfolioService {
	
	private static final Logger log = LogManager.getLogger(PortfolioService.class);
	private AuthDAO adao;
	private PortfolioDAO pdao;
	private TickerDAO tdao;
	private TickerHistoryDAO thdao;
	private TickerNewsDAO tndao;
	
	{
      	// Get authdao object
        try {
        	pdao = new PortfolioDAO();
        	adao = new AuthDAO();
        	tdao = new TickerDAO();
        	thdao = new TickerHistoryDAO();
        	tndao = new TickerNewsDAO();
        } catch (Exception e) {
        	e.printStackTrace();
        }        
	}
	
	@Context
	private HttpServletRequest servletRequest;
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	@Operation(summary = "Add (or remove using negative shares) to add to portfolio", tags = {"Portfolio"}, description = "Date in format dd/MM/yyyy, shares must be an integer", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response subscribe(PurchaseModel pm) {
		try {
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(pm.getDate());  
			TickerHistoryData thd = thdao.getTickerAtDate(pm.getSymbol(), date1);
			Transaction t = new Transaction(user, pm.getSymbol().toUpperCase(), date1, pm.getShares(), thd.Close);
			Portfolio p = pdao.getUserPortfolio(user);
			// check validity of transaction
			p.transactions.add(t);
			p.checkValidity();
			
			pdao.addTransaction(t);
		} catch (PortfolioConstraintException e) {
			Map<String, Object> response = new HashMap<String, Object>();
			Gson gson = new Gson();
			response.put("error", "not enough shares!");
			return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).build();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.ok().build();
	}

	@GET
	@Secured
//	@Path("/dashboard")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get a bundle of all items needed for portfolio page", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "JSON timeseries", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getPortfolioSymbols() {
		try {
			Map<String, Object> response = new HashMap<String, Object>();
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			Gson gson = new Gson();
			
			Portfolio p = pdao.getUserPortfolio(user);
			List<PortfolioValue> portfolioValue = pdao.getUserValueOverTime(user);
			Float value = pdao.getUserPortfolio(user).getValue();
			List<NewsArticle> news = pdao.getPortfolioArticles(p);
			
			if (news.size() == 0) {
				news = tndao.getRecent();
			}
			
			response.put("username", user);
			response.put("value", value);
			response.put("timeseries", portfolioValue);
			response.put("news", news);
			response.put("portfolio", p.portfolio);
			response.put("watchlist", p.watchlist);
			
			
			return Response.ok(gson.toJson(response)).build();
			
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Secured
	@Path("/timeseries")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get a timeseries graph of the portfolio", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "JSON timeseries", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getPortfolioTimeseries() {
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
	
	@POST
	@Secured
	@Path("/watchlist")
	@Operation(summary = "Add ticker to watchlist", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response addToWatchList(@QueryParam("symbol") String symbol) {
		try {
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			pdao.addToWatchlist(user, symbol);
			
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.ok().build();
	}
	
	@DELETE
	@Secured
	@Path("/watchlist")
	@Operation(summary = "Remove ticker from watchlist", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response removeFromWatchList(@QueryParam("symbol") String symbol) {
		try {
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			pdao.removeFromWatchlist(user, symbol);
			
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.ok().build();
	}

	@GET
	@Secured
	@Path("/watchlist")
	@Operation(summary = "Get current watchlist", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "Success", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getWatchlist() {
		try {
			Map<String, Object> response = new HashMap<String, Object>();
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			Gson gson = new Gson();
			
			response.put("watchlist", pdao.getWatchList(user));	
			return Response.ok(gson.toJson(response)).build();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		
	}

	@GET
	@Secured
	@Path("/transactions")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get list of all transactions as a table", tags = {"Portfolio"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getTransactions() {
		try {
//			Map<String, Object> response = new HashMap<String, Object>();
			javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
			String user = AuthUtil.getUsername(cookies, adao);
			ArrayList<ArrayList<String>> response = new ArrayList<ArrayList<String>>();
			Gson gson = new Gson();
			
			Portfolio p = pdao.getUserPortfolio(user);
			
			// add header row
			ArrayList<String> header = new ArrayList<String>();
			header.add("Date");
			header.add("Symbol");
			header.add("Shares");
			header.add("Price");
			header.add("Transaction Value");
			response.add(header);
			
			for (Transaction t : p.transactions) {
				ArrayList<String> row = new ArrayList<String>();
				row.add(t.date);
				row.add(t.symbol);
				row.add(t.shares.toString());
				row.add(t.sharePrice.toString());
				row.add(((Float) (t.shares * t.sharePrice)).toString());
				response.add(row);
			}

			return Response.ok(gson.toJson(response)).build();
			
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
