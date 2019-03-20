package com.spms.ticker.history;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.spms.database.SPMSDB;

public class TickerHistoryDAO {
	private Connection conn;
	
	public TickerHistoryDAO () throws SQLException {
		conn = SPMSDB.getConnection();
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
	
}
