package com.spms.portfolio;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Controller;
import com.spms.Util;
import com.spms.auth.AuthDAO;
import com.spms.database.SPMSDB;
import com.spms.ticker.live.TickerDAO;

public class PortfolioController implements Controller{
	
	private final static Logger log = LogManager.getLogger(PortfolioController.class);
	

	private PortfolioDAO pdao;
	private AuthDAO adao;
	
	public PortfolioController() throws SQLException {
		pdao = new PortfolioDAO();
		adao = new AuthDAO();
		
		pdao.createPortfolioValueTable();
		pdao.createTransactionTable();
	}
	
	/**
	 * Reload all users portfolio values
	 */
	@Override
	public boolean reload() {
		try {
			for (String user : adao.getAllUsers()) {
				pdao.reloadPortfolio(user);
			}
		} catch (Exception e) {
			log.error("Failed to reload users portfolio data");
			log.error(Util.stackTraceToString(e));
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] arg) throws SQLException, NumberFormatException, PortfolioConstraintException, ParseException {
		PortfolioController pc = new PortfolioController();
		
//		pc.addTransaction(new Transaction("test", "aapl", "2019-04-24 11:37:00.000", "5", "75"));
//		pc.addTransaction(new Transaction("test", "aapl", "2019-04-24 11:42:00.000", "-3", "74.5"));
//		pc.addTransaction(new Transaction("test", "tsla", "2019-04-24 11:40:00.000", "1", "234.12"));

		pc.reload();

	}

}
