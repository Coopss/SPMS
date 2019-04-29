package com.spms.tops;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

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
	private static ArrayList<TopMoversObject> topMovers;
	
	TopMoversController() throws SQLException {
		dao = new SymbolDAO();
		tmd = new TopMoversDAO();
	}
	
	public static void main(String[] args) throws SQLException {
		TopMoversController tmc = new TopMoversController();
		TopMoversDAO dao2 = new TopMoversDAO();
		
		for (TopMoversObject tbo : dao2.getTopMovers()) {
			System.out.println(tbo.symbol);
		}
		
		//tmc.reload();
	}
	
	public boolean reload() {
		try {
			for (Symbol s : dao.getAll()) {
				JSONObject chg = (JSONObject)Requests.get(getEndpoint(s.Symbol), Requests.ReturnType.object);
				tmd.insert(s.Symbol, Util.objectToString(chg.get("change")), Util.objectToString(chg.get("changePercent")));
				
			}
			topMovers = tmd.getTopMovers();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return false;
		}
		
		return true;
	}
	
	private static String getEndpoint(String sym) {
		return "/stock/" + sym.toLowerCase() + "/quote";
	}
	
	public static ArrayList<TopMoversObject> getTops() {
		return TopMoversController.topMovers;
	}
}
