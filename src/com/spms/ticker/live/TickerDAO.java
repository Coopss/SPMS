package com.spms.ticker.live;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.spms.database.SPMSDB;

public class TickerDAO {
	private Connection conn;
	
	public TickerDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	public boolean createTickerTable(String tickerName) throws SQLException {
		String tableName = "ticker." + tickerName;
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Date] [datetime] NOT NULL,\n" + 
				"	[Price] [numeric](18, 0) NULL,\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);	
	}
	
}
