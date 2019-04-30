package com.spms.tops;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spms.Util;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.tools.Requests;

public class TopMoversJobWorker implements Runnable {
	
	private List<Symbol> los;
	private TopMoversDAO tdao = new TopMoversDAO();
	private Logger log = LogManager.getLogger(TopMoversJobWorker.class);
	
	public TopMoversJobWorker(List<Symbol> partition) throws SQLException {
		los = partition;
		tdao = new TopMoversDAO();
	}
	
	@Override
	public void run() {
		try {
			tdao.createTopMoversTable();
			for (Symbol sym : los) {
				JSONObject chg = (JSONObject)Requests.get(TopMoversController.getEndpoint(sym.Symbol), Requests.ReturnType.object);
				tdao.insert(sym.Symbol, Util.objectToString(chg.get("change")), Util.objectToString(chg.get("changePercent")));
			}
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
		}
		
		log.info(this + " Finished");

	}

}
