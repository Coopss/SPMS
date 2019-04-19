package com.spms.news;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.database.SPMSDB;

public class TickerNewsDAO extends Thread {
	
	public static String tableName = "ticker.news";
	public static String tableNameSym = "ticker.news.symbols";
	
	private static Connection conn;
	private JSONArray articles;
	private String ticker;
	
	TickerNewsDAO(JSONArray jsons, String tickername, String threadname, ThreadGroup tgob) throws SQLException { 
        super(tgob, threadname);  
		this.articles = jsons;
		this.ticker = tickername;
		conn  = SPMSDB.getConnection();
        start();
	}
	
	public static boolean createTickerNewsTable() throws SQLException {
		conn  = SPMSDB.getConnection();
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}

		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Date] [datetime] NOT NULL,\n" + 
				"	[Headline] [nvarchar](max) NULL,\n" + 
				"	[Source] [nvarchar](max) NULL,\n" + 
				"	[URL] [nvarchar](4000) NULL UNIQUE,\n" + 
				"	[Summary] [nvarchar](max) NULL,\n" + 
				"	[Image] [nvarchar](max) NULL,\n" + 
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);
	}
	
	public static boolean createTickerNewsSymTable() throws SQLException {
		conn  = SPMSDB.getConnection();
		if (SPMSDB.tableExists(conn, tableNameSym)) {
			return false;
		}
		
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableNameSym + "](\n" + 
				"	[Symbol] [char](255) NULL,\n" + 
				"	[URL] [char](255) NULL,\n" + 
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableNameSym);
	}

	@Override
	public void run() {
		for (int i = 0; i < articles.size(); i++) {
			  try {
				insertNews((JSONObject)articles.get(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * forces a string to be under a certain character limit and replaces
	 * single quotation with double quotation for SQL
	 * @param s is the string to manipulate
	 * @return the newly edited string
	 */
	public static String Trim(String s) {
		if (s.length() > 255)
			return s.replace("'", "''").substring(0, 255);
		else
			return s.replace("'", "''");
	}
	
	void insertNews(JSONObject tickerNews) throws SQLException, java.text.ParseException {
		if (tickerNews != null) {
			String command = "INSERT INTO [" + tableName + "] ([Date], [Headline], [Source], [URL], [Summary], [Image]) VALUES ";
			command += "('" + SPMSDB.getMSSQLDatetime(tickerNews.get("datetime").toString()) + "','" + Trim(tickerNews.get("headline").toString()) + "','" + Trim(tickerNews.get("source").toString()) + "','" + Trim(tickerNews.get("url").toString()) + "','" + Trim(tickerNews.get("summary").toString()) + "','" + Trim(tickerNews.get("image").toString()) + "');";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(command);
			command =  "INSERT INTO [" + tableNameSym + "] ([Symbol], [URL]) VALUES ";
			command += "('" + ticker + "','" + Trim(tickerNews.get("url").toString()) + "');";
			stmt.executeUpdate(command);
			String[] syms = tickerNews.get("related").toString().split(",");
			for (String sym : syms) {
				command =  "INSERT INTO [" + tableNameSym + "] ([Symbol], [URL]) VALUES ";
				command += "('" + sym + "','" + Trim(tickerNews.get("url").toString()) + "');";
				stmt.executeUpdate(command);
			}
		}
	}
	
	public static ArrayList<NewsArticle> getNews(String tickerName) throws SQLException {
		conn  = SPMSDB.getConnection();
		ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();
		String command = "SELECT DISTINCT TOP(12) * FROM dbo.[ticker.news.symbols] AS syms, dbo.[ticker.news] AS news WHERE syms.[URL]=news.[URL] AND syms.Symbol='" + tickerName + "' ORDER BY [Date] DESC;";
		PreparedStatement stmt = conn.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
			newsArticles.add(new NewsArticle(rs.getDate("Date"), rs.getString("Headline"), rs.getString("Source"), rs.getString("URL"), rs.getString("Summary"), rs.getString("Image")));
		
		return newsArticles;
	}

}
