package com.spms.tops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.database.SPMSDB;
import com.spms.news.NewsArticle;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;

public class TopMoversDAO {
	private Connection conn;
	protected static String tableName = "internal.tops";
	private static final Logger log = LogManager.getLogger(TopMoversDAO.class);
	
	public TopMoversDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	public boolean createTopMoversTable() throws SQLException {
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Symbol] [nvarchar](255) NOT NULL UNIQUE,\n" + 
				"	[Change] [float] NULL,\n" + 
				"	[ChangePercent] [float] NULL\n" + 
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);
	}
	
	private String fixNull(String s) {
		if (s == "null")
			return "0";
		else
			return s;
	}
	
	public void insert(String sym, String change, String changePercent) throws SQLException {		
		Statement stmt = conn.createStatement();
		
		// if ticker exists, update existing values
		if (exists(sym)) {
			updateTable(sym, change, changePercent);
			log.info("Updated " + sym);
		// if ticker does not exist, add it to the table
		} else {
			stmt.executeUpdate("INSERT INTO [" + tableName + "] (Symbol, Change, ChangePercent) VALUES ('" + sym + "'," + fixNull(change) + "," + fixNull(changePercent) + ");");
			log.info("Added " + sym);
		}
	}
	
	private void updateTable(String sym, String change, String changePercent) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("UPDATE [" + tableName + "] SET Change=" + fixNull(change) + ", ChangePercent=" + fixNull(changePercent) + " WHERE Symbol=" + "'" + sym + "'" + ";");
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
	
	public ArrayList<TopMoversObject> getTopMovers() throws SQLException {
		ArrayList<TopMoversObject> tops = new ArrayList<TopMoversObject>();
		String command = "SELECT TOP(5) * FROM [dbo].[" + tableName + "] WHERE [Change] IS NOT NULL AND [ChangePercent] IS NOT NULL ORDER BY [ChangePercent] DESC;";
		PreparedStatement stmt = conn.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();

		while (rs.next())
			tops.add(new TopMoversObject(rs.getString("Symbol"), Float.toString(rs.getFloat("Change")), Float.toString(rs.getFloat("ChangePercent"))));

		return tops;
	}
}
