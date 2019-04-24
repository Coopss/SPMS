package com.spms.api;

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
	private static SymbolDAO sdao;
	private static TickerNewsDAO ndao;
	private static StatsDAO stdao;
	
	static {
      	// Get authdao object
        try {
        	tdao = new TickerDAO();
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
	public Response get(@PathParam("symbol") String symbol) {
				
		try {
			Map<String, Object> response = new HashMap<String, Object>();
			Gson gson = new Gson();
			
			Symbol s = sdao.get(symbol);
			TickerController.reload(symbol, tdao);
			List<TickerData> todayData = tdao.getTodayData(symbol);
			Stats statistics = stdao.get(symbol);
			List<NewsArticle> articles = ndao.getNews(symbol);
			TickerData yesterdayClose = tdao.getYesterdayClose(symbol);
			
			response.put("company", s.Name);
			response.put("symbol", s.Symbol);
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
