package com.spms.ticker.history;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.live.TickerData;

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
			return false;
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
//					log.debug("Duplicate value not added");
				}
			}
		}
	
		return true;	
	}
	
	public boolean insertTicker(JSONObject data, String tickerName) throws SQLException, ParseException {
		String tableName = generateTickerTableName(tickerName);
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + tableName + "] ([Date], [Open], [High], [Low], [Close], [Volume], [UnadjustedVolume], [ChangeOverTime], [Change], [Vwap], [ChangePercent]) VALUES " + buildValueString(data));
		
		return true;	
		
	}
	
	public TickerHistoryData getTickerAtDate(String ticker, String date) throws SQLException, ParseException {
		PreparedStatement stmt = conn.prepareStatement("SELECT TOP (1) [Date], [Open],[High],[Low],[Close],[Volume],[UnadjustedVolume],[ChangeOverTime],[Change],[Vwap],[ChangePercent] FROM [dbo].[ticker."+ticker.toUpperCase()+".history] WHERE [date] <= '" + SPMSDB.getMSSQLDatetime(date)+ "' ORDER BY [date] desc;");
			
			ResultSet rs = stmt.executeQuery();		
			TickerHistoryData thd = new TickerHistoryData();
			while(rs.next()) {
				thd.Date = rs.getString(1);
				thd.Open = rs.getString(2);
				thd.High = rs.getString(3);
				thd.Low = rs.getString(4);
				thd.Close = rs.getString(5);
				thd.Volume = rs.getString(6);
				thd.UnadjustedVolume = rs.getString(7);
				thd.ChangeOverTime = rs.getString(8);
				thd.Change = rs.getString(9);
				thd.Vwap = rs.getString(10);
				thd.ChangePercent = rs.getString(11);
		    }
			return thd;
	}
	
	public List<TickerHistoryData> getTickerOverRange(String ticker, DateRange range) throws SQLException, ParseException {
		Date minDate = DateRange.getDateRangeOffsetFromNow(range);
		List<TickerHistoryData> data = new ArrayList<TickerHistoryData>();
		
		PreparedStatement stmt = conn.prepareStatement("SELECT [Date],[Open],[High],[Low],[Close],[Volume],[UnadjustedVolume],[ChangeOverTime],[Change],[Vwap],[ChangePercent] FROM [dbo].[ticker."+ticker.toUpperCase()+".history] WHERE [date] >= '" + SPMSDB.getMSSQLDatetime(minDate)+ "' ORDER BY [date] desc;");
			
			ResultSet rs = stmt.executeQuery();		
			
			while(rs.next()) {
				TickerHistoryData thd = new TickerHistoryData();
				thd.Date = rs.getString(1);
				thd.Open = rs.getString(2);
				thd.High = rs.getString(3);
				thd.Low = rs.getString(4);
				thd.Close = rs.getString(5);
				thd.Volume = rs.getString(6);
				thd.UnadjustedVolume = rs.getString(7);
				thd.ChangeOverTime = rs.getString(8);
				thd.Change = rs.getString(9);
				thd.Vwap = rs.getString(10);
				thd.ChangePercent = rs.getString(11);
				data.add(thd);
		    }
			return data;
	}
	
	
	
}
