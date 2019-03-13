package com.spms.ticker.live;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.spms.api.auth.AuthUtil;
import com.spms.database.SPMSDB;

public class TickerDAO {
	private Connection conn;
	protected static String trackingListTableName = "internal.liveTrackingList";
	
	public TickerDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	/* Ticker table methods */
	public boolean createTickerTable(String tickerName) throws SQLException {
		String tableName = "ticker." + tickerName;
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Date] [datetime] NOT NULL,\n" + 
				"	[Price] [decimal](18, 5) NULL,\n" +
				") ON [PRIMARY]\n";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);	
	}
	
	public boolean insertPrice(String symbol, Float price) throws SQLException {
		String tableName = "ticker." + symbol;
		
		if (!SPMSDB.tableExists(conn, tableName)) {
			createTickerTable(symbol);
		}
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("INSERT INTO [" + tableName + "] (Date, Price) VALUES ('" + SPMSDB.getMSSQLDatetime() + "', " + price + ");");
		
		return true;
		
	}
	
	/* Track table methods */
	public boolean createTrackTable() throws SQLException {
		if (SPMSDB.tableExists(conn, trackingListTableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + trackingListTableName + "](\n" + 
				"	[Date] [datetime] NOT NULL,\n" + 
				"	[Symbol] [nvarchar](50) NULL,\n" +
				"	[User] [nvarchar](50) NULL,\n" +
				") ON [PRIMARY]\n";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, trackingListTableName);	
	}
	
	protected boolean addToTrackList(String symbol, String user) throws SQLException {
		if (isTracked(symbol, user)) {
			return false;
		}
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("INSERT INTO [" + trackingListTableName + "] (Date, Symbol, [User]) VALUES ('" + SPMSDB.getMSSQLDatetime() + "', '" + symbol + "', '" + user + "');");
			
		return true;
	}
	
	private boolean isTracked(String symbol, String user) throws SQLException {
	    PreparedStatement stmt = conn.prepareStatement("select * from [" + trackingListTableName + "] where Symbol = '" + symbol + "' and [User] = '" + user + "'");
	    ResultSet rs = stmt.executeQuery();
		
	    while(!rs.next()) {
	    	return false;
	    }
	        
	    return true;
	}

	public boolean subscribe(String symbol, String user) throws SQLException {
		if (!isTracked(symbol, user)) {
			addToTrackList(symbol, user);
			return true;
		}
		
	    return true;
	}

	public boolean unsubscribe(String symbol, String user) throws SQLException {
		Integer count = 0;
		Statement stmt;
		
		// unsubscribing from untracked symbol (this is an error)
		if (!isTracked(symbol, user)) {
			return true;
		}

		stmt = conn.createStatement();
		stmt.executeUpdate("delete from [" + trackingListTableName + "] " + " where Symbol = '" + symbol + "' and [User] = '" + user + "';");
			
		
	    return true;
	}
	
	public ArrayList<String> getAllSubscribedSymbols() throws SQLException {
		ArrayList<String> li = new ArrayList<String>();
		
		PreparedStatement stmt = conn.prepareStatement("select distinct symbol from [" + trackingListTableName + "];");
		
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	li.add(rs.getString(1));
	    }
	    
	    return li;
	}
	
}
