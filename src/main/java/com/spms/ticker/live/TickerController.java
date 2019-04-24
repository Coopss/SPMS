package com.spms.ticker.live;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.spms.Controller;
import com.spms.Util;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;

public class TickerController implements Controller {

	private SymbolDAO sdao;
	private List<Symbol> los;
	private static final Logger log = LogManager.getLogger(TickerController.class);
	private static final Integer numWorkers = 16;
	
	public static String buildExt(String ticker) {
		return "/stock/" + ticker + "/chart/1d";
	}
	
	private static Integer ceil(Double d) {
		return (int) Math.round(Math.ceil(d));
	}
	
	public TickerController() throws SQLException {
		sdao = new SymbolDAO();		
		los = sdao.getAll();
	}
	
	public boolean reload() {
		List<Thread> workers = new ArrayList<Thread>();
		
		// create runners
		for (List<Symbol> part : Lists.partition(los, ceil(los.size()/ (Double.parseDouble(numWorkers.toString()))))) {
			try {
				Thread runner = new Thread(new TickerJobWorker(part));
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

	
	
	
	public static void main(String[] args) throws SQLException, MalformedURLException, InterruptedException {
		TickerController tc = new TickerController();
		tc.reload();
	}
	
	
}
