package com.spms.news;

import java.util.List;
import java.util.Set;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Controller;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class NewsController implements Controller {
	
	private TickerNewsDAO tnd;
	private SymbolDAO dao;
	private static final Logger log = LogManager.getLogger(NewsController.class);
	
	private static Set<String> allSyms;
	
	public NewsController() throws SQLException {
		tnd = new TickerNewsDAO();
		dao = new SymbolDAO();
		
		allSyms = new HashSet<String>();
		for (Symbol s : dao.getAll()) {
			allSyms.add(s.Symbol.toUpperCase());
		}
		
	}
	
	public static JSONArray getArticles(String ticker) throws MalformedURLException, ParseException {
		return (JSONArray) Requests.get("/stock/" + ticker + "/news", Requests.ReturnType.array);
	}

	
	public static boolean isValidSymbol(String sym) {
		return allSyms.contains(sym.toUpperCase());
	}
	
	public boolean reload() {
		try {
			tnd.createTickerNewsTable();
			tnd.createTickerNewsTableSym();
			for (Symbol sym : dao.getAll()) {
				try {
					JSONArray symArticles = NewsController.getArticles(sym.Symbol);
					if (symArticles != null)
						for (Object obj : symArticles) {
							JSONObject jobj = (JSONObject) obj;
//							log.info(symArticles.get(i));
							if (tnd.insertNews(sym, jobj)) {
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
		
		return true;
	}

	public static void main(String[] args) throws MalformedURLException, ParseException, SQLException {
		NewsController na = new NewsController();
		na.reload();
	}

}