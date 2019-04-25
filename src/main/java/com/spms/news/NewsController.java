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
	
	private static ArrayList<Symbol> allSyms;
	
	public NewsController() throws SQLException {
		tnd = new TickerNewsDAO();
		dao = new SymbolDAO();
	}
	
	public static JSONArray getArticles(String ticker) throws MalformedURLException, ParseException {
		return (JSONArray) Requests.get("/stock/" + ticker + "/news", Requests.ReturnType.array);
	}
	
	public void addNews() throws MalformedURLException, ParseException, SQLException, java.text.ParseException {
		for (Symbol sym : dao.getAll()) {
			JSONArray symArticles = NewsController.getArticles(sym.Symbol);
			if (symArticles != null)
				for (int i = 0; i < symArticles.size(); i++) {
					log.info(symArticles.get(i));
					tnd.insertNews(sym, (JSONObject)symArticles.get(i));
			}
		}
	}
	
	public static ArrayList<Symbol> getSyms() {
		return allSyms;
	}
	
	public boolean reload() {
		try {
			tnd.createTickerNewsTable();
			addNews();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) throws MalformedURLException, ParseException, SQLException {
		NewsController na = new NewsController();
		na.reload();
	}

}