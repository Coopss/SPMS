package com.spms.news;

import java.awt.List;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class NewsAggregator implements Runnable {
	
	private TickerNewsDAO tnd;
	private Boolean running;
	private Integer msTimeout = 900000;
	private static final Logger log = LogManager.getLogger(NewsAggregator.class);
	private int gSize = 5;
	
	public NewsAggregator() {
		this.running = true;
	}
	
	public static JSONArray getArticles(String ticker) throws MalformedURLException, ParseException {
		return (JSONArray) Requests.get("/stock/" + ticker + "/news", Requests.ReturnType.array);
	}
	
	public void addNews() throws MalformedURLException, ParseException, SQLException {
		SymbolDAO dao = new SymbolDAO();
		tnd = new TickerNewsDAO();
		ArrayList<Symbol> allSyms = (ArrayList<Symbol>) dao.getAll();
		for (int i = 0; i < allSyms.size(); i+=gSize) {
			String symList = "";
			for (int j = 0; j < gSize; j++) {
				if (i+j < allSyms.size()) {
					symList += allSyms.get(i+j).Symbol + ",";
				}
			}
			symList = symList.substring(0, symList.length()-1);
			System.out.println(symList);
			NewsJob nj = new NewsJob(symList, tnd);
			nj.start();
		}
		tnd = new TickerNewsDAO();
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(msTimeout);
			} catch (InterruptedException e) {
				continue;
			}
			log.info("Running news fetch batch job...");
			try {
				addNews();
			} catch (MalformedURLException e) {
				log.error(Util.stackTraceToString(e));
			} catch (SQLException e) {
				log.error(Util.stackTraceToString(e));
			} catch (ParseException e) {
				log.error(Util.stackTraceToString(e));
			}
		}
	}

	public static void main(String[] args) throws MalformedURLException, ParseException, SQLException {
		NewsAggregator ng = new NewsAggregator();
		ng.addNews();
	}

}