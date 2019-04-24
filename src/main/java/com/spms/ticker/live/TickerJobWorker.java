package com.spms.ticker.live;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.tools.Requests;

public class TickerJobWorker implements Runnable {

	private List<Symbol> los;
	private TickerDAO dao;
	private Map<String, Date> recentMap; // symbol to most recent date we have, prevents dupes
	private Logger log = LogManager.getLogger(TickerJobWorker.class);
	
	public TickerJobWorker(List<Symbol> partition) throws SQLException {
		los = partition;
		dao = new TickerDAO();
		recentMap = new HashMap<String, Date>();
	}
	
	@Override
	public void run() {
		for (Symbol sym : los) {
			List<TickerData> batch = new ArrayList<TickerData>();
			try {
				// make table if it does not exist
				dao.makeTableIfNotExist(sym.Symbol);
				// get data from iex
				JSONArray r = (JSONArray) Requests.get(TickerController.buildExt(sym.Symbol), Requests.ReturnType.array);
				
				// get most recent entry on our record
				Date mostRecent = null;
				if (!recentMap.containsKey(sym.Symbol)) {
					recentMap.put(sym.Symbol, dao.getMostRecent(sym.Symbol));
				} 
				mostRecent = recentMap.get(sym.Symbol);
				
				// iterate each entry
				for (Object obj : r) {
					try {
						// ensure valid data
						if (!((JSONObject) obj).containsKey("date")) {
							continue;
						}
						
						// convert to TickerData
						TickerData data = new TickerData((JSONObject) obj, sym.Symbol);
						
						// no shares purchased in delta t
						if (data.marketAverage.equals("-1")) {
							continue;
						}
			
						// check that it is after most recent (i.e. we should add it)
						if (mostRecent == null) {
							batch.add(data);
						} else if (data.dateobj != null && mostRecent != null && data.dateobj.compareTo(mostRecent) > 0) {
							batch.add(data);
						}
					} catch (Exception e) {
						log.error("Failed to insert: " + obj.toString());
						log.error(Util.stackTraceToString(e));
					}
				}
				
				if (batch.size() > 0) {
					dao.insert(batch);
					log.info("Finished updating " + sym.Symbol);
				} else {
					log.info("Nothing to update for " + sym.Symbol);
				}

			} catch (Exception e) {
				log.error("Something went wrong when updating live data for " + sym.Symbol);
				log.error(Util.stackTraceToString(e));
			}
		}
		
		log.info("Swarm worker " + this + " finished.");
	}
	
}
