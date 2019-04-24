package com.spms.ticker.live;

import java.net.MalformedURLException;
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

import com.google.common.collect.Lists;
import com.spms.Controller;
import com.spms.Util;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class TickerController implements Controller {

	private SymbolDAO sdao;
	private List<Symbol> los;
	private static final Logger log = LogManager.getLogger(TickerController.class);
	private static final Integer numWorkers = 16;
	private static final Map<String, Date> lastUpdated = new HashMap<String, Date>();
	private static final Integer minReloadTime = 120000; // 2 min
	

	public static String buildExt(String ticker) {
		return "/stock/" + ticker + "/chart/1d";
	}
	
	public TickerController() throws SQLException {
		sdao = new SymbolDAO();		
		los = sdao.getAll();
	}
	
	public boolean reload() {
		List<Thread> workers = new ArrayList<Thread>();
		
		// create runners
		for (List<Symbol> part : Lists.partition(los, Util.ceil(los.size()/ (Double.parseDouble(numWorkers.toString()))))) {
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

	public static boolean reload(String ticker, TickerDAO dao) {
		if (lastUpdated.containsKey(ticker) && Util.howLongAgo(lastUpdated.get(ticker)) > minReloadTime || !lastUpdated.containsKey(ticker)) {		
			List<TickerData> batch = new ArrayList<TickerData>();
			log.info("Force updating " + ticker);
			try {
				// make table if it does not exist
				dao.makeTableIfNotExist(ticker);
				// get data from iex
				JSONArray r = (JSONArray) Requests.get(TickerController.buildExt(ticker), Requests.ReturnType.array);
				
				// get most recent entry on our record
				Date mostRecent = dao.getMostRecent(ticker);
				
				// iterate each entry
				for (Object obj : r) {
					try {
						// ensure valid data
						if (!((JSONObject) obj).containsKey("date")) {
							continue;
						}
						
						// convert to TickerData
						TickerData data = new TickerData((JSONObject) obj, ticker);
						
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
					log.info("Finished updating " + ticker);
				} else {
					log.info("Nothing to update for " + ticker);
				}
	
			} catch (Exception e) {
				log.error("Something went wrong when updating live data for " + ticker);
				log.error(Util.stackTraceToString(e));
				return false;
			}
			
			lastUpdated.put(ticker, new Date());
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) throws SQLException, MalformedURLException, InterruptedException {
		TickerController tc = new TickerController();
		tc.reload();
	}
	
	
}
