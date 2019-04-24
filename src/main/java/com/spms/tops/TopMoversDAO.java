package com.spms.tops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;

public class TopMoversDAO {
	private Connection conn;
	protected static String tableName = "internal.tops";
	
	TopMoversDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	public boolean createTopMoversTable() throws SQLException {
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Symbol] [nvarchar](255) NOT NULL UNIQUE,\n" + 
				"	[Change] [nvarchar](max) NULL,\n" + 
				"	[ChangePercent] [nvarchar](max) NULL\n" + 
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);
	}
	
	public void insert(String sym, String change, String changePercent) throws SQLException {		
		Statement stmt = conn.createStatement();
		
		if (exists(sym)) {
			updateTable(sym, change, changePercent);
		} else {
			stmt.executeUpdate("INSERT INTO [" + tableName + "] (Symbol, Change, ChangePercent) VALUES ('" + sym + "','" + change + "','" + changePercent + "');");
		}
	}
	
	private void updateTable(String sym, String change, String changePercent) throws SQLException {
		Statement stmt = conn.createStatement();
		
		// if ticker does not exist, add it to the table
		stmt.executeUpdate("UPDATE [" + tableName + "] SET Change='" + change + "', ChangePercent='" + changePercent + "' WHERE Symbol=" + "'" + sym + "'" + ";");
	}
	
	private boolean exists(String sym) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT Symbol from [dbo].[" + tableName + "] WHERE Symbol='" + sym + "';");
        
        String symbol = "";
        ResultSet rs = stmt.executeQuery();        
        
        while(rs.next()) {
            symbol = rs.getString(1);
        }
        
        if (symbol.equals(""))
        	return false;
        else
        	return true;
	}
	
}
