package com.spms.ticker.history;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.spms.database.SPMSDB;
import com.spms.ticker.tools.JSONObject;
import com.spms.ticker.tools.StockDataRetriever;

public class TickerHistoryDAO {
	private Connection conn;
	
	public TickerHistoryDAO () throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	public boolean updateTickerHistoryTable() {
		try {
			ArrayList<String> symbols = getAllTableSymbols();
			String[] jsons;
			
			for (String sym : symbols) {
				JSONObject tmp;
				String[] values = {"date", "open", "high", "low", "close", "volume", "notional", "numberOfTrades", "marktOpen", "marktHigh", "marktLow", "marktClose", "marktVolume"}; 
				String tableName = "ticker." + sym + ".history";
				String s = StockDataRetriever.getResponse("/stock/" + sym + "/chart/5y");
				
				s = s.substring(1, s.length() - 1);
				jsons = s.split(",");
				
				for (String js : jsons) {
					tmp = StockDataRetriever.stringToJSON(js);
					insertIntoTable(tableName, tmp.getValues(values));
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void insertIntoTable(String tableName, String values) throws SQLException {
		String command;
		
		command = "INSERT INTO [ticker." + tableName + ".history] (Date,[Open],High,Low,[Close],Volume,Ex-Dividend,[Split Ratio],[Adj. Open],[Adj. High], [Adj. Low],[Adj. Close],[Adj. Volume]) VALUES " + values + ";";
		System.out.println(command);
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(command);
	}
	
	public boolean createTickerHistoryTable(String tickerName) throws SQLException {
		String tableName = "ticker." + tickerName + ".history";
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Date] [datetime] NOT NULL,\n" + 
				"	[Open] [numeric](18, 0) NULL,\n" + 
				"	[High] [numeric](18, 0) NULL,\n" + 
				"	[Low] [numeric](18, 0) NULL,\n" + 
				"	[Close] [numeric](18, 0) NULL,\n" + 
				"	[Volume] [numeric](18, 0) NULL,\n" + 
				"	[Ex-Dividend] [numeric](18, 0) NULL,\n" + 
				"	[Split Ratio] [numeric](18, 0) NULL,\n" + 
				"	[Adj. Open] [numeric](18, 0) NULL,\n" + 
				"	[Adj. High] [numeric](18, 0) NULL,\n" + 
				"	[Adj. Low] [numeric](18, 0) NULL,\n" + 
				"	[Adj. Close] [numeric](18, 0) NULL,\n" + 
				"	[Adj. Volume] [numeric](18, 0) NULL\n" + 
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);
	}
	
	public ArrayList<String> getAllTableSymbols() throws SQLException {
		ArrayList<String> li = new ArrayList<String>();
		
		PreparedStatement stmt = conn.prepareStatement("select distinct symbol from *;");
		
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	li.add(rs.getString(1));
	    }
	    
	    return li;
	}
	
}