package com.spms.ticker.history;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.spms.Controller;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class TickerHistoryController implements Controller {
	public TickerHistoryDAO dao;
	public SymbolDAO sdao;
	private static final Integer numWorkers = 16;
	private static final Logger log = LogManager.getLogger(TickerHistoryController.class);
	
	public List<Symbol> los;
	
	public TickerHistoryController() throws SQLException {
		dao = new TickerHistoryDAO();
		sdao = new SymbolDAO();
		
		los = sdao.getAll();
		
	}
	
	public static String buildExt(String ticker) {
		return "/stock/" + ticker + "/chart/1w";
//		return "/stock/" + ticker + "/chart/5y";
	}
	
	public boolean reload() {
		List<Thread> workers = new ArrayList<Thread>();
		
		// create runners
		for (List<Symbol> part : Lists.partition(los, Util.ceil(los.size()/ (Double.parseDouble(numWorkers.toString()))))) {
			try {
				Thread runner = new Thread(new TickerHistoryJobWorker(part));
				workers.add(runner);
				runner.start();
			} catch (SQLException e) {
				log.error("Failed to create runner to update -- critical");
				log.error(Util.stackTraceToString(e));
				return false;
			}
		}
		
		// rejoin all threads
		for (Thread t : workers) {
			try {
				t.join();
			} catch (InterruptedException e) {
				log.error("Failed to stop a runner, possible memory leak");
				log.error(Util.stackTraceToString(e));
			}
		}
	
		return true;
	}
	
	public static void main(String[] args) throws SQLException, MalformedURLException, ParseException, java.text.ParseException {
		TickerHistoryController thc = new TickerHistoryController();
		thc.reload();
	}
	
	
}
