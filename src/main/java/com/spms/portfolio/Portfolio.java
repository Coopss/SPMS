package com.spms.portfolio;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.api.PortfolioService;
import com.spms.ticker.live.TickerDAO;
import com.spms.ticker.live.TickerData;

public class Portfolio {

	public Set<String> watchlist;
	public Float value = null;
	public Map<String, Integer> portfolio;
	private TickerDAO tdao;
	public List<Transaction> transactions;
	
	private static final Logger log = LogManager.getLogger(Portfolio.class);
	
	public Portfolio(List<Transaction> transactions, TickerDAO tdao) throws PortfolioConstraintException {
		this.tdao = tdao;
		this.portfolio = new HashMap<String, Integer>();
		this.watchlist = new HashSet<String>();
		this.transactions = transactions;
		
		checkValidity();
	
	
		return;
	}
	
	public Boolean checkValidity() throws PortfolioConstraintException {
		for (Transaction t  : this.transactions) {
			if (!portfolio.containsKey(t.symbol)) {
				portfolio.put(t.symbol, t.shares);
			} else {
				portfolio.put(t.symbol, portfolio.get(t.symbol) + t.shares);
			}
		}	
		
		for (String sym : this.portfolio.keySet()) {
			if (portfolio.get(sym) < 0) {
				throw new PortfolioConstraintException();
			}
		}
		
		return true;
	}
	
	
	public Float getValue() throws SQLException, ParseException {
		Float val = (float) 0.0;
		
		if (this.value == null) {
			for (String sym : portfolio.keySet()) {
				TickerData td = tdao.getMostRecentPrice(sym);
				val += portfolio.get(sym) * Float.parseFloat(td.marketAverage);
			}
					
			return val;
		} else {
			return this.value;
		}
		
	}
	
	
}
