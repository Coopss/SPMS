package com.spms.portfolio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.auth.AuthDAO;
import com.spms.database.SPMSDB;
import com.spms.news.NewsArticle;
import com.spms.ticker.live.TickerDAO;

public class PortfolioDAO {
	
	private Connection conn;
	private TickerDAO tdao;
	private final Logger log = LogManager.getLogger(PortfolioDAO.class);
	protected static final String transactionTableName = "internal.transactions";
	protected static final String portfolioTableName = "internal.portfolio";
	protected static final String watchlistTableName = "internal.watchlist";
	
	public PortfolioDAO() throws SQLException {
		conn = SPMSDB.getConnection();
		tdao = new TickerDAO();
	}
	
	public Boolean createTransactionTable() throws SQLException {
		
		if (SPMSDB.tableExists(conn, transactionTableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + transactionTableName + "](\n" + 
				"	[username] [nvarchar](50) NOT NULL,\n" +
				"	[date] [datetime] NOT NULL,\n" +
				"	[symbol] [nvarchar](255) NOT NULL,\n" + 
				"	[shares] [int] NOT NULL,\n" +
				"	[sharePrice] [float] NOT NULL\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, transactionTableName);
	}
	
	public Boolean createPortfolioValueTable() throws SQLException {
		if (SPMSDB.tableExists(conn, portfolioTableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + portfolioTableName + "](\n" + 
				"	[date] [datetime] NOT NULL,\n" +
				"	[username] [nvarchar](50) NOT NULL,\n" +
				"	[value] [float] NOT NULL\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, portfolioTableName);
	}
	
	public Boolean createWatchlistTable() throws SQLException {
		if (SPMSDB.tableExists(conn, watchlistTableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + watchlistTableName + "](\n" + 
				"	[username] [nvarchar](50) NOT NULL,\n" +
				"	[symbol] [nvarchar](50) NOT NULL\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, watchlistTableName);
	}
		
	private Boolean insertPortfolioValue(String user, Float value) throws SQLException {
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + portfolioTableName + "] ([date], [username], [value]) VALUES ('" + SPMSDB.getMSSQLDatetime() + "','" + user + "'," + value + ");");
		
		return true;
	}
	
	private Boolean insertTransaction(String user, String sym, String date, String share, String price) throws SQLException {		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + transactionTableName + "] ([username], [symbol], [date], [shares], [sharePrice]) VALUES ('" + user + "','" + sym.toUpperCase() + "','" + date + "'," + share + "," + price + ");");
		
		return true;
	}
	
	public List<Transaction> getAllUsersTransactions(String user) throws NumberFormatException, SQLException {
		List<Transaction> data = new ArrayList<Transaction>();
		
		PreparedStatement stmt = conn.prepareStatement("select [username], [symbol], [date], [shares], [sharePrice] from [" + transactionTableName + "] where username = '" + user + "' order by [date] asc");
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	Transaction row = new Transaction();
	    	row.user = rs.getString(1);
	    	row.symbol = rs.getString(2);
	    	row.date = rs.getString(3);
	    	row.shares = Integer.parseInt(rs.getString(4));
	    	row.sharePrice = Float.parseFloat(rs.getString(5));
	    	data.add(row);

	    }
	    
	    return data;
	}
	
	public Portfolio getUserPortfolio(String user) throws NumberFormatException, SQLException, PortfolioConstraintException {
		List<Transaction> transactions = getAllUsersTransactions(user);
		Portfolio portfolio = new Portfolio(transactions, tdao);
		portfolio.watchlist = getWatchList(user);
		return portfolio;
		
	}
	
	public Boolean addTransaction(Transaction t) throws SQLException {
		return insertTransaction(t.user, t.symbol, t.date, t.shares.toString(), t.sharePrice.toString());
	}
	
	public Boolean addTransaction(String user, String sym, String date, String share, String price) throws SQLException {
		return insertTransaction(user, sym, date, share, price);
	}

	public Boolean reloadPortfolio(String user) throws NumberFormatException, SQLException, PortfolioConstraintException, ParseException {
		Portfolio p = getUserPortfolio(user);
		insertPortfolioValue(user, p.getValue());
		log.info(user + " " + p.portfolio + " " + p.getValue().toString());
		
		return true;
	}
	
	public List<PortfolioValue> getUserValueOverTime(String user) throws NumberFormatException, SQLException {
		List<PortfolioValue> data = new ArrayList<PortfolioValue>();
		
		PreparedStatement stmt = conn.prepareStatement("select [date], [username], [value] from [" + portfolioTableName + "] where username = '" + user + "' order by [date] desc");
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	PortfolioValue row = new PortfolioValue();
	    	row.user = rs.getString(2);
	    	row.date = rs.getString(1);
	    	row.value = Float.parseFloat(rs.getString(3));
	    	data.add(row);

	    }
	    
	    return data;
	}
	
	public List<NewsArticle> getPortfolioArticles(Portfolio p) throws SQLException, ParseException {
		StringBuilder cmd = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		cmd.append("select top(12) [internal.news].Date, [internal.news.symbols].Symbol, [internal.news].Headline, [internal.news].Source, [internal.news].URL, [internal.news].Summary, [internal.news].Image from [internal.news.symbols] inner join [internal.news] on [internal.news].UID = [internal.news.symbols].UID where ");
		
		Set<String> ticker = new HashSet<String>();
		ticker.addAll(p.portfolio.keySet());
		ticker.addAll(p.watchlist);
		
		// return empty if nothing found
		if (ticker.isEmpty()) {
			return new ArrayList<NewsArticle>();
		}
		
		for (String sym : ticker) {
			cmd.append("[internal.news.symbols].Symbol = '" + sym + "' OR ");
		}
		cmd.setLength(cmd.length() - 3);
		cmd.append("order by [internal.news].Date desc");
		
		log.info(cmd.toString());
		List<NewsArticle> data = new ArrayList<NewsArticle>();
		
		PreparedStatement stmt = conn.prepareStatement(cmd.toString());
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	NewsArticle row = new NewsArticle();
	    	row.date = sdf.parse(rs.getString(1));
	    	row.symbol = rs.getString(2);
	    	row.headline = rs.getString(3);
	    	row.source = rs.getString(4);
	    	row.url = rs.getString(5);
	    	row.summary = rs.getString(6);
	    	row.image = rs.getString(7);
	    	data.add(row);
	    }	
	    
	    return data;
	}
	
	public Boolean addToWatchlist(String user, String symbol) throws SQLException {
		HashSet<String> watchlist = getWatchList(user);
		
		if (watchlist.contains(symbol.toUpperCase())) {
			return false;
		}
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + watchlistTableName + "] ([username], [symbol]) VALUES ('" + user + "','" + symbol.toUpperCase() + "');");
		
		return true;
		
	}
	
	public Boolean removeFromWatchlist(String user, String symbol) throws SQLException {
		HashSet<String> watchlist = getWatchList(user);
		
		if (watchlist.contains(symbol.toUpperCase())) {
			return false;
		}
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("DELETE FROM [dbo].[internal.watchlist] WHERE [username] = '" + user + "' AND [symbol] = '" + symbol.toUpperCase() + "';");
		
		return true;
		
	}
	
	public HashSet<String> getWatchList(String user) throws SQLException {
		HashSet<String> data = new HashSet<String>();
		
		PreparedStatement stmt = conn.prepareStatement("select [symbol] from [" + watchlistTableName + "] where username = '" + user + "';");
		ResultSet rs = stmt.executeQuery();		
	    
		while(rs.next()) {
	    	data.add(rs.getString(1));
	    }
	    
	    return data;
	}
	
}
