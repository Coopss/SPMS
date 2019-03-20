package com.spms.ticker.live;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.BatchJobRunner;
import com.spms.Util;
import com.spms.ticker.tools.StockDataRetriever;

public class LiveFetchJob implements Runnable {

	private TickerDAO dao;
	private Boolean running;
	private Integer msTimeout = 30000;
	private static final Logger log = LogManager.getLogger(LiveFetchJob.class);
	
	private String endpoint(String symbol) {
		return "/stock/" + symbol + "/price";
	}
	
	public LiveFetchJob() throws SQLException {
		dao = new TickerDAO();
		running = true;
	}
	
	private Float getSymbolCurrentPrice(String symbol) throws MalformedURLException {
		return Float.parseFloat(StockDataRetriever.getResponse(endpoint(symbol.toLowerCase())));
	}
	
	public boolean fetch() throws SQLException, MalformedURLException {
		Float p;
		for (String sym : dao.getAllSubscribedSymbols()) {
			try {
				p = getSymbolCurrentPrice(sym);
				dao.insertPrice(sym, p);
				log.info("Fetched: " + sym);
			// case where symbol that is subscribed to does not exist in iex
			} catch (NullPointerException | NumberFormatException E) {
				log.warn("Warning: failed to fetch live price update for symbol: " + sym);
				continue;
			}
		}
		return true;
	}

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(msTimeout);
			} catch (InterruptedException e) {
				continue;
			}
			log.info("Running live fetch batch job...");
			try {
				fetch();
			} catch (MalformedURLException e) {
				log.error(Util.stackTraceToString(e));
			} catch (SQLException e) {
				log.error(Util.stackTraceToString(e));
			}	
		}
	}

	
}
