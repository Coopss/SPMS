package com.spms.tops;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.common.collect.Lists;
import com.spms.Controller;
import com.spms.Util;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class TopMoversController implements Controller {
	
	private TopMoversDAO tmd;
	private SymbolDAO dao;
	private static final Logger log = LogManager.getLogger(TopMoversController.class);
	private List<Symbol> los;
	private static final Integer numWorkers = 16;
	
	TopMoversController() throws SQLException {
		dao = new SymbolDAO();
		tmd = new TopMoversDAO();
		los = dao.getAll();
	}
	
	public boolean reload() {
		List<Thread> workers = new ArrayList<Thread>();
		
		// create runners
		for (List<Symbol> part : Lists.partition(los, Util.ceil(los.size()/ (Double.parseDouble(numWorkers.toString()))))) {
			try {
				Thread runner = new Thread(new TopMoversJobWorker(part));
				workers.add(runner);
				runner.start();
			} catch (SQLException e) {
				log.error("Failed to create runner to update tops -- critical");
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
	
	public static String getEndpoint(String sym) {
		return "/stock/" + sym.toLowerCase() + "/quote";
	}
	
	
	public static void main(String[] args) throws SQLException {
		TopMoversController tmc = new TopMoversController();
		tmc.reload();
	}
	
	
}
