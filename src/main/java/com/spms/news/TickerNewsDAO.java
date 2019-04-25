package com.spms.news;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;

public class TickerNewsDAO {

	public static String tableName = "internal.news";
	public static String tableNameSym = "internal.news.symbols";

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

	public boolean createTickerNewsTableSym() throws SQLException {
		if (SPMSDB.tableExists(conn, tableNameSym)) {
			return false;
		}

		String makeTableCommand =
				"CREATE TABLE [dbo].[" + tableNameSym + "](\n" +
				"	[Symbol] [nvarchar](255) NULL,\n" +
				"	[URL] [nvarchar](max) NULL,\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);

		return SPMSDB.tableExists(conn, tableNameSym);
	}

	/*
	 * replaces single quotation with double quotation for SQL
	 * @param s is the string to manipulate
	 * @return the newly edited string
	 */
	public static String Trim(String s) {
		return s.replace("'", "''");
	}

	public String formatDate(String date) throws java.text.ParseException {
		if (date.equals("0")) {
			return null;
		}
		Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(date);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String msSqlDate = sdf.format(date1).trim();
		return msSqlDate.replace(" ","T").toString();
	}

	public boolean insertNews(Symbol sym, JSONObject tickerNews) throws SQLException, java.text.ParseException {
		String url = Trim(objectToString(tickerNews.get("url")));
		HashSet<String> syms = new HashSet<String>();
		
		if (tickerNews != null && !exists(url)) {
			// inserts news article of selected stock
			String command = "INSERT INTO [" + tableName + "] ([Date], [Headline], [Source], [URL], [Summary], [Image]) VALUES ";
			command += "('" + formatDate(tickerNews.get("datetime").toString()) + "','" + Trim(objectToString(tickerNews.get("headline"))) + "','" + Trim(objectToString(tickerNews.get("source"))) + "','" + url + "','" + Trim(objectToString(tickerNews.get("summary"))) + "','" + Trim(objectToString(tickerNews.get("image"))) + "');";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(command);

			
			syms.add(sym.Symbol.toUpperCase());
			syms.addAll(getRelatedSymbols(Trim(objectToString(tickerNews.get("related")))));
			stmt.executeUpdate(makeCommand(syms, url));
			
			return true;
		}
		
		return false;
	}

	private String makeCommand(Set<String> relatedSymbols, String url) {
		StringBuilder cmd = new StringBuilder();
		cmd.append("INSERT INTO [" + tableNameSym + "] ([Symbol], [URL]) VALUES ");
		for (String sym : relatedSymbols) {
			cmd.append("('" + sym + "','" + url + "'),");
		}
		cmd.setLength(cmd.length() - 1);
		cmd.append(";");
		return cmd.toString();
	}

	private ArrayList<String> getRelatedSymbols(String o) throws SQLException {

		ArrayList<String> relatedSyms = new ArrayList<String>();
		String[] relatives = o.split(",");

		for (String s : relatives) {
			if (NewsController.isValidSymbol(s))
				relatedSyms.add(s);
		}

		return relatedSyms;
	}

	public ArrayList<NewsArticle> getNews(String tickerName) throws SQLException {
		ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();
		String command = "SELECT DISTINCT TOP(12) * FROM dbo.[" + tableName + "] as news, dbo.[" + tableNameSym + "] as syms WHERE syms.URL=news.URL AND syms.Symbol='" + tickerName + "' ORDER BY [Date] DESC;";
		PreparedStatement stmt = conn.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();

		while (rs.next())
			newsArticles.add(new NewsArticle(rs.getDate("Date"), rs.getString("Symbol"), rs.getString("Headline"), rs.getString("Source"), rs.getString("URL"), rs.getString("Summary"), rs.getString("Image")));

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
