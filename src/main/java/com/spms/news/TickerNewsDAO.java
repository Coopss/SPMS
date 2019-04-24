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

public class TickerNewsDAO {
	
	public static String tableName = "internal.news";
	
	private Connection conn;
	private JSONArray articles;
	private String ticker;
	
	public TickerNewsDAO() throws SQLException {
		conn  = SPMSDB.getConnection();
	}
	
	TickerNewsDAO(JSONArray jsons, String tickername) throws SQLException { 
		conn  = SPMSDB.getConnection();
		this.articles = jsons;
		this.ticker = tickername;
	}
	
	public boolean createTickerNewsTable() throws SQLException {
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
	
	/*
	 * replaces single quotation with double quotation for SQL
	 * @param s is the string to manipulate
	 * @return the newly edited string
	 */
	public static String Trim(String s) {
		return s.replace("'", "''");
	}
	
	public void insertNews(JSONObject tickerNews) throws SQLException, java.text.ParseException {
		if (tickerNews != null && !exists(Trim(tickerNews.get("url").toString()))) {
			// inserts news article of selected stock
			String command = "INSERT INTO [" + tableName + "] ([Date], [Headline], [Source], [URL], [Summary], [Image]) VALUES ";
			command += "('" + SPMSDB.getMSSQLDatetime(tickerNews.get("datetime").toString()) + "','" + Trim(objectToString(tickerNews.get("headline"))) + "','" + Trim(objectToString(tickerNews.get("source"))) + "','" + Trim(objectToString(tickerNews.get("url"))) + "','" + Trim(objectToString(tickerNews.get("summary"))) + "','" + Trim(objectToString(tickerNews.get("image"))) + "');";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(command);
		}
	}
	
	public ArrayList<NewsArticle> getNews(String tickerName) throws SQLException {
		ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();
		String command = "SELECT DISTINCT TOP(12) * FROM dbo.[" + tableName +  "] ORDER BY [Date] DESC;";
		PreparedStatement stmt = conn.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
			newsArticles.add(new NewsArticle(rs.getDate("Date"), rs.getString("Headline"), rs.getString("Source"), rs.getString("URL"), rs.getString("Summary"), rs.getString("Image")));
		
		return newsArticles;
	}
	
	private static String objectToString(Object o) {
		if (o == null)
			return "";
		else
			return o.toString();
	}
	
	private boolean exists(String url) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT URL from [dbo].[" + tableName + "] WHERE URL='" + url + "';");
        
        String link = "";
        ResultSet rs = stmt.executeQuery();        
        
        while(rs.next()) {
            link = rs.getString(1);
        }
        
        if (link.equals(""))
        	return false;
        else
        	return true;
	}

}
