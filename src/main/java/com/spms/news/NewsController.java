package com.spms.news;

import java.util.List;
import java.util.Set;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.common.collect.Lists;
import com.spms.Controller;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class NewsController implements Controller {
	
	private TickerNewsDAO tnd;
	private SymbolDAO dao;
	private static final Logger log = LogManager.getLogger(NewsController.class);
	private List<Symbol> los;
	private static final Integer numWorkers = 8;
	private static Set<String> allSyms;
	
	public NewsController() throws SQLException {
		tnd = new TickerNewsDAO();
		dao = new SymbolDAO();
		
		los = dao.getAll();
		
		allSyms = new HashSet<String>();
		for (Symbol s : los) {
			allSyms.add(s.Symbol.toUpperCase());
		}
		
	}
	
	public static JSONArray getArticles(String ticker) throws MalformedURLException, ParseException {
		return (JSONArray) Requests.get("/stock/" + ticker + "/news", Requests.ReturnType.array);
	}

	
	public static boolean isValidSymbol(String sym) {
		return allSyms.contains(sym.toUpperCase());
	}
	
	public boolean reload() {
		List<Thread> workers = new ArrayList<Thread>();
		
		// create runners
		for (List<Symbol> part : Lists.partition(los, Util.ceil(los.size()/ (Double.parseDouble(numWorkers.toString()))))) {
			try {
				Thread runner = new Thread(new NewsJobWorker(part));
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
	
	public static void main(String[] args) throws MalformedURLException, ParseException, SQLException {
		NewsController na = new NewsController();
		na.reload();
	}

}