package com.spms.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.spms.Util;
import com.spms.api.annotations.Secured;
import com.spms.news.NewsArticle;
import com.spms.news.TickerNewsDAO;
import com.spms.ticker.history.DateRange;
import com.spms.ticker.history.TickerHistoryDAO;
import com.spms.ticker.history.TickerHistoryData;
import com.spms.ticker.live.TickerController;
import com.spms.ticker.live.TickerDAO;
import com.spms.ticker.live.TickerData;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.stats.Stats;
import com.spms.ticker.stats.StatsDAO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/symbol")
public class DataService {
	private static final Logger log = LogManager.getLogger(DataService.class);
	private static TickerDAO tdao;
	private static TickerHistoryDAO thdao;
	private static SymbolDAO sdao;
	private static TickerNewsDAO ndao;
	private static StatsDAO stdao;
	
	static {
      	// Get authdao object
        try {
        	tdao = new TickerDAO();
        	thdao = new TickerHistoryDAO();
        	sdao = new SymbolDAO();
        	ndao = new TickerNewsDAO();
        	stdao = new StatsDAO();
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }        
	}
	
	@Context
	private HttpServletRequest servletRequest;
	
	
	@GET
//	@Secured
	@Path("/{symbol}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get overview (descriptive) details of a symbol", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getSymbolOverview(@PathParam("symbol") String symbol) {
				
		try {
			Map<String, Object> response = new HashMap<String, Object>();
			Gson gson = new Gson();
			
			Symbol s = sdao.get(symbol);
			TickerController.reload(symbol, tdao);
			List<TickerData> todayData = tdao.getTodayData(symbol);
			Stats statistics = stdao.get(symbol);
			List<NewsArticle> articles = ndao.getNews(symbol);
			TickerData yesterdayClose = tdao.getYesterdayClose(symbol);
			Float recentPrice = Float.parseFloat(tdao.getMostRecentPrice(symbol).marketAverage);
			Float priceChange = recentPrice - Float.parseFloat(yesterdayClose.marketAverage);
			Float percentChange = priceChange / Float.parseFloat(yesterdayClose.marketAverage);
			
			
			response.put("company", s.Name);
			response.put("symbol", s.Symbol);
			response.put("currentPrice", recentPrice);
			response.put("priceChange", priceChange);
			response.put("percentChange", percentChange);
			response.put("about", s.Description);
			response.put("yesterdayClose", yesterdayClose);
			response.put("todayData", todayData);
			response.put("statistics", statistics);
			response.put("articles", articles);
			
			return Response.ok(gson.toJson(response)).build();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
//	@Secured
	@Path("/{symbol}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get data for a stock on a specific date", tags = {"Data"}, description = "Date MUST be of the format yyyy-MM-dd. If the specified day is not a valid market day, it will return the closest day before.", responses = {@ApiResponse(description = "{\"symbol\":\"AAPL\",\"data\":{\"Date\":\"2016-07-01 00:00:00.0\",\"Open\":\"91.2280\",\"High\":\"92.1595\",\"Low\":\"91.0752\",\"Close\":\"91.6102\",\"Volume\":\"26026540\",\"UnadjustedVolume\":\"26026540\",\"ChangeOverTime\":\"0.2286\",\"Change\":\"0.2771\",\"Vwap\":\"91.7112\",\"ChangePercent\":\"0.3030\"}}", responseCode = "200"), @ApiResponse(description = "Bad date or symbol doesnt exist {\"error\":\"bad date\"} OR {\"error\":\"bad symbol\"}", responseCode = "400"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getSymbolDate(@PathParam("symbol") String symbol, @PathParam("date") String date) {
		Map<String, Object> response = new HashMap<String, Object>();
		Gson gson = new Gson();
		try {
			TickerHistoryData thd = thdao.getTickerAtDate(symbol.toUpperCase(), date);

			response.put("data", thd);
			response.put("symbol", symbol.toUpperCase());
			
			return Response.ok(gson.toJson(response)).build();
		} catch (ParseException e) {
			response.put("error", "bad date");
			return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).build();
		} catch (com.microsoft.sqlserver.jdbc.SQLServerException e) {
			response.put("error", "bad symbol");
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).build();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@GET
//	@Secured
	@Path("/{symbol}/history/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get data for a stock over a specific date granularity (1w, 1m, 3m, 1y, 5y, max)", tags = {"Data"}, description = ".", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "", responseCode = "400"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
	public Response getSymbolDateRange(@PathParam("symbol") String symbol, @PathParam("granularity") String granularity) {
		Map<String, Object> response = new HashMap<String, Object>();
		Gson gson = new Gson();
		try {
			DateRange dr = DateRange.get(granularity);
			if (dr == null) {
				response.put("error", "valid granularities are of (1w, 1m, 3m, 1y, 5y, max)");
				return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).build();
			}
			
			List<TickerHistoryData> thd = thdao.getTickerOverRange(symbol, dr);

			response.put("data", thd);
			response.put("granularity", granularity.toLowerCase());
			response.put("symbol", symbol.toUpperCase());
			
			return Response.ok(gson.toJson(response)).build();
		} catch (com.microsoft.sqlserver.jdbc.SQLServerException e) {
			log.error(Util.stackTraceToString(e));
			response.put("error", "bad symbol");
			return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

	}
	
//	@GET
//	@Secured
//	@Path("/{symbol}/news")
//	@Operation(summary = "Get relevent news for a symbol", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
//	public Response getNews(@PathParam("symbol") String symbol) {
//		return Response.ok().build();
//	}
//	
//	@GET
//	@Secured
//	@Path("/{symbol}/metrics")
//	@Operation(summary = "Get performance metrics for a symbol", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
//	public Response getMetrics(@PathParam("symbol") String symbol) {
//		return Response.ok().build();
//	}
//	
//	@GET
//	@Secured
//	@Path("/topmovers")
//	@Operation(summary = "Get top movers of day", tags = {"Data"}, description = "", responses = {@ApiResponse(description = "", responseCode = "200"), @ApiResponse(description = "User is not authorized", responseCode = "401")})
//	public Response topMovers(@PathParam("granularity") String symbol) {
//		return Response.ok().build();
//	}
	
}
