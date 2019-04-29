package com.spms.portfolio;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.spms.ticker.live.TickerDAO;
import com.spms.ticker.live.TickerData;

public class Portfolio {

	public Set<String> watchlist;
	public Float value = null;
	public Map<String, Integer> portfolio;
	private TickerDAO tdao;
	private List<Transaction> transactions;
	
	
	public Portfolio(List<Transaction> transactions, TickerDAO tdao) throws PortfolioConstraintException {
		this.tdao = tdao;
		this.portfolio = new HashMap<String, Integer>();
		this.watchlist = new HashSet<String>();
		this.transactions = transactions;
		
		for (Transaction t  : this.transactions) {
			if (!portfolio.containsKey(t.symbol)) {
				portfolio.put(t.symbol, t.shares);
			} else {
				portfolio.put(t.symbol, portfolio.get(t.symbol) + t.shares);
			}
			
			if (portfolio.get(t.symbol) < 0) {
				throw new PortfolioConstraintException();
			}
			
		}		
	
		return;
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
