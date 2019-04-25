package com.spms.news;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spms.Util;
import com.spms.ticker.los.Symbol;

public class NewsJobWorker implements Runnable {
	
	private List<Symbol> los;
	private TickerNewsDAO dao;

	private Logger log = LogManager.getLogger(NewsJobWorker.class);
	
	public NewsJobWorker(List<Symbol> partition) throws SQLException {
		los = partition;
		dao = new TickerNewsDAO();
	}
	
	@Override
	public void run() {
		try {
			dao.createTickerNewsTable();
			dao.createTickerNewsTableSym();
			for (Symbol sym : los) {
				try {
					JSONArray symArticles = NewsController.getArticles(sym.Symbol);
					if (symArticles != null)
						for (Object obj : symArticles) {
							JSONObject jobj = (JSONObject) obj;
							if (dao.insertNews(sym, jobj)) {
								log.info("Added " + jobj.get("url"));
							} else {
								log.info("Already have " + jobj.get("url"));
							}
					} 
				} catch (Exception e) {
					log.error(Util.stackTraceToString(e));
				}
			}
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
		}
		
		log.info(this + " Finished");

	}

}
