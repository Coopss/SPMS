package com.spms.ticker.history;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.spms.database.SPMSDB;

public class TickerHistoryDAO {
	private Connection conn;
	private static final Logger log = LogManager.getLogger(TickerHistoryDAO.class);
	
	public TickerHistoryDAO () throws SQLException {
		conn = SPMSDB.getConnection();
	}
	
	private String generateTickerTableName(String tickerName) {
		return "ticker." + tickerName.toUpperCase() + ".history";	
	}
	
	private String buildValueString(JSONObject data) throws ParseException {
//		return "('" + SPMSDB.getMSSQLDatetime(data.get("date").toString()) + "', '" + data.get("open") + "', '"+ data.get("high") + "', '"+ data.get("low") + "', '"+ data.get("close") + "', '"+ data.get("volume") + "', '"+ data.get("unadjustedVolume") + "', '"+ data.get("changeOverTime") + "', '"+ data.get("change") + "', '"+ data.get("vwap") + "', '"+ data.get("changePercent") + "')";
		return "('" + SPMSDB.getMSSQLDatetime(data.get("date").toString()) + "', " + data.get("open") + ", "+ data.get("high") + ", "+ data.get("low") + ", "+ data.get("close") + ", "+ data.get("volume") + ", "+ data.get("unadjustedVolume") + ", "+ data.get("changeOverTime") + ", "+ data.get("change") + ", "+ data.get("vwap") + ", "+ data.get("changePercent") + ")";
	}
	
	public boolean createTickerHistoryTable(String tickerName) throws SQLException {
		String tableName = generateTickerTableName(tickerName);
		
		if (SPMSDB.tableExists(conn, tableName)) {
//			return false;
			SPMSDB.dropTable(conn, tableName);
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[Date] [datetime] NOT NULL UNIQUE,\n" + 
				"	[Open] [decimal](18, 4) NULL,\n" + 
				"	[High] [decimal](18, 4) NULL,\n" + 
				"	[Low] [decimal](18, 4) NULL,\n" + 
				"	[Close] [decimal](18, 4) NULL,\n" + 
				"	[Volume] [numeric](18, 0) NULL,\n" + 
				"	[UnadjustedVolume] [numeric](18, 0) NULL,\n" + 
				"	[ChangeOverTime] [decimal](18, 4) NULL,\n" + 
				"	[Change] [decimal](18, 4) NULL,\n" + 
				"	[Vwap] [decimal](18, 4) NULL,\n" + 
				"	[ChangePercent] [decimal](18, 4) NULL\n" + 
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);
	}

	public boolean insertTicker(List<Object> data, String tickerName) throws SQLException, ParseException {
		String tableName = generateTickerTableName(tickerName);
		StringBuilder command = new StringBuilder();
		Statement stmt = conn.createStatement();
		
		// add header
		command.append("INSERT INTO [" + tableName + "] ([Date], [Open], [High], [Low], [Close], [Volume], [UnadjustedVolume], [ChangeOverTime], [Change], [Vwap], [ChangePercent]) VALUES ");
		
		for (Object obj : data) {
			command.append(buildValueString((JSONObject) obj));
			command.append(",");
		}
		
		command.setLength(command.length() - 1);
		command.append(";");
		
		try {
			stmt.executeUpdate(command.toString());
			
		// likely a dupe, insert one by one to be sure
		} catch (SQLServerException e) {
			for (Object obj : data) {
				try {
					insertTicker((JSONObject) obj, tickerName);
				} catch (SQLServerException e2) {
					log.warn("Duplicate value not added");
				}
			}
		}
	
		return true;	
	}
	
	public boolean insertTicker(JSONObject data, String tickerName) throws SQLException, ParseException {
		String tableName = generateTickerTableName(tickerName);
		
		Statement stmt = conn.createStatement();
		
//		stmt.executeUpdate("INSERT INTO [" + tableName + "] ([Date], [Open], [High], [Low], [Close], [Volume], [UnadjustedVolume], [ChangeOverTime], [Change], [Vwap], [ChangePercent]) VALUES ('" + SPMSDB.getMSSQLDatetime(data.get("date").toString()) + "', '" + data.get("open") + "', '"+ data.get("high") + "', '"+ data.get("low") + "', '"+ data.get("close") + "', '"+ data.get("volume") + "', '"+ data.get("unadjustedVolume") + "', '"+ data.get("changeOverTime") + "', '"+ data.get("change") + "', '"+ data.get("vwap") + "', '"+ data.get("changePercent") + "');");
		
		stmt.executeUpdate("INSERT INTO [" + tableName + "] ([Date], [Open], [High], [Low], [Close], [Volume], [UnadjustedVolume], [ChangeOverTime], [Change], [Vwap], [ChangePercent]) VALUES " + buildValueString(data));
		
		return true;	
		
	}
	
}
