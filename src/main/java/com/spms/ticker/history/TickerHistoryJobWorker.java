package com.spms.ticker.history;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

import com.google.common.collect.Lists;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.tools.Requests;

public class TickerHistoryJobWorker implements Runnable {
	public TickerHistoryDAO dao;
	private static final Integer numberOfChunksPerCommit = 5;
	private static final Logger log = LogManager.getLogger(TickerHistoryJobWorker.class);
	
	public List<Symbol> los;
	
	public TickerHistoryJobWorker(List<Symbol> part) throws SQLException {
		dao = new TickerHistoryDAO();	
		los = part;
	}
	
	@Override
	public void run() {
		for (Symbol sym : los) {
			try {
				dao.createTickerHistoryTable(sym.Symbol);
				JSONArray r = (JSONArray) Requests.get(TickerHistoryController.buildExt(sym.Symbol), Requests.ReturnType.array);
				List<List<Object>> chunks = Lists.partition(r, r.size() / numberOfChunksPerCommit);
				
				for (List<Object> chunk : chunks) {
					dao.insertTicker(chunk, sym.Symbol);
				}
			} catch (Exception e) {
				log.error("Failed to insert " + sym.Symbol);
				log.error(Util.stackTraceToString(e));
			}
			
			log.info("Finished updating history: " + sym.Symbol);
		}
		
	}
}
