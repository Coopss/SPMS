package com.spms.ticker.history;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.StockDataRetriever;

public class TickerHistoryController {
	public TickerHistoryDAO dao;
	public SymbolDAO sdao;
	private static final Integer numberOfChunksPerCommit = 5;
	private static final Logger log = LogManager.getLogger(TickerHistoryController.class);
	
	public List<Symbol> los;
	
	public TickerHistoryController() throws SQLException {
		dao = new TickerHistoryDAO();
		sdao = new SymbolDAO();
		
		los = sdao.getAll();
		
	}
	
	private static String buildExt(String ticker) {
		return "/stock/" + ticker + "/chart/5y";
	}
	
	public boolean getAllTickers() {
		for (Symbol sym : los) {
			try {
				JSONArray r = StockDataRetriever.get(buildExt(sym.Symbol));
				List<List<Object>> chunks = Lists.partition(r, r.size() / numberOfChunksPerCommit);
				
				for (List<Object> chunk : chunks) {
					dao.insertTicker(chunk, sym.Symbol);
					log.info("Finished chunk: " + sym.Symbol);
				}
			} catch (Exception e) {
				log.error("Failed to insert " + sym.Symbol);
				log.error(Util.stackTraceToString(e));
			}
		}

		
		return true;
	}	
	
	public static void main(String[] args) throws SQLException, MalformedURLException, ParseException, java.text.ParseException {
		TickerHistoryController thc = new TickerHistoryController();
		thc.getAllTickers();
	}
	
	
}
