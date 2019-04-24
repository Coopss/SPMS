package com.spms.ticker.live;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.spms.Util;
import com.spms.auth.AuthUtil;
import com.spms.database.SPMSDB;

public class TickerDAO {
	private Connection conn;
	
	private static final Logger log = LogManager.getLogger(TickerDAO.class);
	
	public TickerDAO() throws SQLException {
		conn = SPMSDB.getConnection();
	}

	private String generateTickerTableName(String tickerName) {
		return "ticker." + tickerName.toUpperCase();	
	}
	
	public boolean makeTableIfNotExist(String tickerName) throws SQLException {
		String tableName = generateTickerTableName(tickerName);
		if (!SPMSDB.tableExists(conn, tableName)) {
			createTickerTable(tickerName);
			return true;
		}
		return false;
	}
	
	/* Ticker table methods */
	public boolean createTickerTable(String tickerName) throws SQLException {
		String tableName = generateTickerTableName(tickerName);
		
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + tableName + "](\n" + 
				"	[date] [datetime] NOT NULL UNIQUE,\n" + 
				"	[high] [nvarchar](max) NULL,\n" +
				"	[low] [nvarchar](max) NULL,\n" +
				"	[average] [nvarchar](max) NULL,\n" +
				"	[volume] [nvarchar](max) NULL,\n" +
				"	[notional] [nvarchar](max) NULL,\n" +
				"	[numberOfTrades] [nvarchar](max) NULL,\n" +
				"	[marketHigh] [nvarchar](max) NULL,\n" +
				"	[marketLow] [nvarchar](max) NULL,\n" +
				"	[marketAverage] [nvarchar](max) NULL,\n" +
				"	[marketVolume] [nvarchar](max) NULL,\n" +
				"	[marketNotional] [nvarchar](max) NULL,\n" +
				"	[marketNumberOfTrades] [nvarchar](max) NULL,\n" +
				"	[open] [nvarchar](max) NULL,\n" +
				"	[close] [nvarchar](max) NULL,\n" +
				"	[marketOpen] [nvarchar](max) NULL,\n" +
				"	[marketClose] [nvarchar](max) NULL,\n" +
				"	[changeOverTime] [nvarchar](max) NULL,\n" +
				"	[marketChangeOverTime] [nvarchar](max) NULL\n" +
				") ON [PRIMARY]\n";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);	
	}

	public Date getMostRecent(String tickerName) throws SQLException {
		String tableName = generateTickerTableName(tickerName);
		
		if (!SPMSDB.tableExists(conn, tableName)) {
			return null;
		}
		
		PreparedStatement stmt = conn.prepareStatement("SELECT MAX(date) from [dbo].[" + tableName + "];");
		
		String date = "";
		ResultSet rs = stmt.executeQuery();		
	    
		while(rs.next()) {
	    	date = rs.getString(1);
	    }
		Date dateobj;
		try {
			SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			dateobj = parser.parse(date);
		} catch (Exception e) {
			return null;
		}  
		return dateobj;
	}
	
	public boolean insert(TickerData data) throws SQLException {
		String tableName = generateTickerTableName(data.ticker);
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [dbo].[" + tableName + "] ([date] ,[high] ,[low] ,[average] ,[volume] ,[notional] ,[numberOfTrades] ,[marketHigh] ,[marketLow] ,[marketAverage] ,[marketVolume] ,[marketNotional] ,[marketNumberOfTrades] ,[open] ,[close] ,[marketOpen] ,[marketClose] ,[changeOverTime] ,[marketChangeOverTime]) VALUES ('" + data.date + "', '" + data.high + "', '" + data.low + "', '" + data.average + "', '" + data.volume + "', '" + data.notional + "', '" + data.numberOfTrades + "', '" + data.marketHigh + "', '" + data.marketLow + "', '" + data.marketAverage + "', '" + data.marketVolume + "', '" + data.marketNotional + "', '" + data.marketNumberOfTrades + "', '" + data.open + "', '" + data.close + "', '" + data.marketOpen + "', '" + data.marketClose + "', '" + data.changeOverTime + "', '" + data.marketChangeOverTime + "');");
		return true;
		
	}
	
	public boolean insert(List<TickerData> l) throws SQLException {
		StringBuilder sb = new StringBuilder();
		String tableName = generateTickerTableName(l.get(0).ticker);
		
		Statement stmt = conn.createStatement();
		
		sb.append("INSERT INTO [dbo].[" + tableName + "] ([date] ,[high] ,[low] ,[average] ,[volume] ,[notional] ,[numberOfTrades] ,[marketHigh] ,[marketLow] ,[marketAverage] ,[marketVolume] ,[marketNotional] ,[marketNumberOfTrades] ,[open] ,[close] ,[marketOpen] ,[marketClose] ,[changeOverTime] ,[marketChangeOverTime]) VALUES ");
		
		for (TickerData data : l) {
			sb.append("('" + data.date + "', '" + data.high + "', '" + data.low + "', '" + data.average + "', '" + data.volume + "', '" + data.notional + "', '" + data.numberOfTrades + "', '" + data.marketHigh + "', '" + data.marketLow + "', '" + data.marketAverage + "', '" + data.marketVolume + "', '" + data.marketNotional + "', '" + data.marketNumberOfTrades + "', '" + data.open + "', '" + data.close + "', '" + data.marketOpen + "', '" + data.marketClose + "', '" + data.changeOverTime + "', '" + data.marketChangeOverTime + "'),");
		}
		sb.setLength(sb.length() - 1);
		sb.append(";");
		
//		log.info(sb.toString());
		
		stmt.executeUpdate(sb.toString());
		
		return true;
	}

	public List<TickerData> getTodayData(String tickerName) throws ParseException, SQLException {
		String tableName = generateTickerTableName(tickerName);
		String now = SPMSDB.getMSSQLDatetime(Util.getDateWithoutTimeUsingCalendar());
		List<TickerData> dat = new ArrayList<TickerData>();
		
		PreparedStatement stmt = conn.prepareStatement("SELECT [date],[marketAverage],[marketVolume],[marketNumberOfTrades] FROM [dbo].[" + tableName + "] where date > '" + now + "';");
		
		ResultSet rs = stmt.executeQuery();		
	    
		while(rs.next()) {
			TickerData td = new TickerData();
			td.date = rs.getString(1);
			td.marketAverage = rs.getString(2);
			td.marketVolume = rs.getString(3);
			td.marketNumberOfTrades = rs.getString(4);
			
			dat.add(td);
	    }
		
		return dat;
		
	}
	
}
