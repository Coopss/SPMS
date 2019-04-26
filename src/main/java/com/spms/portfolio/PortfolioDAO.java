package com.spms.portfolio;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.spms.database.SPMSDB;

public class PortfolioDAO {
	private Connection conn;
	protected static String tableName = "internal.portfolio";
	
	PortfolioDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	public boolean createTransHistTable() throws SQLException {
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[username] [nvarchar](50) NOT NULL\n" +
				"	[date] [datetime] NOT NULL UNIQUE,\n" +
				"	[symbol] [nvarchar](255) NOT NULL,\n" + 
				"	[shares] [int] NOT NULL\n" +
				"	[sharePrice] [float] NOT NULL\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);
	}
	
	public void insert(String user, String sym, String date, String share, String price) throws SQLException {		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + tableName + "] (user, symbol, date, shares, sharePrice) VALUES ('" + user + "','" + sym + "','" + date + "'," + share + "," + price + ");");
	}
	

}
