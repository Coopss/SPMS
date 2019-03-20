package com.spms.ticker.live;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.spms.ticker.tools.StockDataRetriever;

public class LiveFetch implements Runnable {

	private TickerDAO dao;
	private Boolean running;
	private Integer msTimeout = 5000;
	
	private String endpoint(String symbol) {
		return "/stock/" + symbol + "/price";
	}
	
	public LiveFetch() throws SQLException {
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
				System.out.println("Fetched: " + sym);
			// case where symbol that is subscribed to does not exist in iex
			} catch (NullPointerException | NumberFormatException E) {
				System.err.println("Warning: failed to fetch live price update for symbol: " + sym);
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
			
			try {
				fetch();
			} catch (MalformedURLException e) {
				// TODO add logger
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO add logger
				e.printStackTrace();
			}
			
		}
		
	}
	
	
}
