package com.spms.tops;

import java.net.MalformedURLException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Controller;
import com.spms.Util;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class TopMoversController implements Controller {
	
	private TopMoversDAO tmd;
	private SymbolDAO dao;
	private static final Logger log = LogManager.getLogger(TopMoversController.class);
	
	TopMoversController() throws SQLException {
		dao = new SymbolDAO();
		tmd = new TopMoversDAO();
	}
	
	public static void main(String[] args) throws SQLException {
		TopMoversController tmc = new TopMoversController();
		tmc.reload();
	}
	
	public boolean reload() {
		try {
			tmd.createTopMoversTable();
			
			for (Symbol s : dao.getAll()) {
				JSONObject chg = (JSONObject)Requests.get(getEndpoint(s.Symbol), Requests.ReturnType.object);
				tmd.insert(s.Symbol, Util.objectToString(chg.get("change")), Util.objectToString(chg.get("changePercent")));
			}
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return false;
		}
		
		return true;
	}
	
	private static String getEndpoint(String sym) {
		return "/stock/" + sym.toLowerCase() + "/quote";
	}
}
