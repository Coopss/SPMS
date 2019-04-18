package com.spms.news;

import java.awt.List;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.StockDataRetriever;

public class NewsAggregator {
	
	public static JSONArray getArticles(String ticker) throws MalformedURLException, ParseException {
		return StockDataRetriever.get("/stock/" + ticker + "/news");
	}
	
	public static void addNews() throws MalformedURLException, ParseException, SQLException {
		SymbolDAO dao = new SymbolDAO();
		ArrayList<Symbol> symbols = (ArrayList<Symbol>) dao.getAll(); 
		ThreadGroup tdg = new ThreadGroup("News Symbols Thread Group");
		for (Symbol sym : symbols) {
			TickerNewsDAO tnd = new TickerNewsDAO(getArticles(sym.Symbol), sym.Symbol, sym.Symbol + "Thread", tdg);
		}
	}

	public static void main(String[] args) throws MalformedURLException, ParseException, SQLException {
		//TickerNewsDAO.createTickerNewsTable();
		//TickerNewsDAO.createTickerNewsSymTable();
		//addNews();
		//System.out.println(TickerNewsDAO.Trim("Meme's review"));
		ArrayList<NewsArticle> tmp = TickerNewsDAO.getNews("AAPL");
		
		for (NewsArticle na : tmp) {
			System.out.println(na.headline);
		}
	}

}