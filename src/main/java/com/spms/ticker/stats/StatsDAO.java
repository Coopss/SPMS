package com.spms.ticker.stats;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;
import com.spms.tops.TopMoversController;

public class StatsDAO {
	private String tableName = "internal.stats";
	private static final Logger log = LogManager.getLogger(StatsDAO.class);
	private Connection conn;
	
	public StatsDAO() {
		try {
			conn = SPMSDB.getConnection();
		} catch (SQLException e) {
			log.error(Util.stackTraceToString(e));
		}		
	}
	
	public boolean dropTable() throws SQLException {
		return SPMSDB.dropTable(conn, tableName);
	}
	
	public static String quote(String s) {
		return "'" + s + "',";
	}
	
	public static String makeInsertCommand(String tableName, String keys, String values) {
		String[] keysArr = keys.split(",");
		String[] valsArr = values.split(",");
		
		String command = "INSERT INTO [" + tableName + "] (";
		
		for (int i = 0; i < keysArr.length; i++) {
			command += "[" + keysArr[i] + "]";
			
			if (i < keysArr.length - 1)
				command += ",";
		}
		
		command += ") VALUES (";
		
		for (int i = 0; i < valsArr.length; i++) {
			command += valsArr[i];
			
			if (i < valsArr.length - 1)
				command += ",";
		}
		
		command += ");";
		
		return command;
	}
	
	public static String makeUpdateCommand(String tableName, String keys, String values) {
		String[] keysArr = keys.split(",");
		String[] valsArr = values.split(",");
		
		String command = "UPDATE [" + tableName + "] SET ";
		
		for (int i = 0; i < keysArr.length; i++) {
			command += keysArr[i].substring(1,keysArr[i].length()-1) + "=" + valsArr[i];
			
			if (i < keysArr.length - 1)
				command += ", ";
		}
		command += "' WHERE companyName=" + valsArr[0] + ";";
		
		return command;		
	}
	
	public boolean createTable() throws SQLException {
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
		
		String makeTableCommand = "CREATE TABLE [dbo].[" + tableName + "]( "
				 + "[symbol] [nvarchar](450) NULL UNIQUE,"
				 + "[companyName] [nvarchar](max) NULL, "
				 + "[marketcap] [nvarchar](max) NULL,"
				 + "[beta] [nvarchar](max) NULL,"
				 + "[week52high] [nvarchar](max) NULL,"
				 + "[week52low] [nvarchar](max) NULL,"
				 + "[week52change] [nvarchar](max) NULL,"
				 + "[shortInterest] [nvarchar](max) NULL,"
				 + "[shortDate] [datetime] NULL,"
				 + "[dividendRate] [nvarchar](max) NULL,"
				 + "[dividendYield] [nvarchar](max) NULL,"
				 + "[exDividendDate] [datetime] NULL,"
				 + "[latestEPS] [nvarchar](max) NULL,"
				 + "[latestEPSDate] [datetime] NULL,"
				 + "[sharesOutstanding] [nvarchar](max) NULL,"
				 + "[float] [nvarchar](max) NULL,"
				 + "[returnOnEquity] [nvarchar](max) NULL, "
				 + "[consensusEPS] [nvarchar](max) NULL,"
				 + "[numberOfEstimates] [nvarchar](max) NULL,"
				 + "[EBITDA] [nvarchar](max) NULL,"
				 + "[revenue] [nvarchar](max) NULL,"
				 + "[grossProfit] [nvarchar](max) NULL,"
				 + "[cash] [nvarchar](max) NULL,"
				 + "[debt] [nvarchar](max) NULL,"
				 + "[ttmEPS] [nvarchar](max) NULL,"
				 + "[revenuePerShare] [nvarchar](max) NULL,"
				 + "[revenuePerEmployee] [nvarchar](max) NULL,"
				 + "[peRatioHigh] [nvarchar](max) NULL,"
				 + "[peRatioLow] [nvarchar](max) NULL,"
				 + "[EPSSurpriseDollar] [nvarchar](max) NULL,"
				 + "[EPSSurprisePercent] [nvarchar](max) NULL, "
				 + "[returnOnAssets] [nvarchar](max) NULL,"
				 + "[returnOnCapital] [nvarchar](max) NULL,"
				 + "[profitMargin] [nvarchar](max) NULL,"
				 + "[priceToSales] [nvarchar](max) NULL,"
				 + "[priceToBook] [nvarchar](max) NULL,"
				 + "[day200MovingAvg] [nvarchar](max) NULL,"
				 + "[day50MovingAvg] [nvarchar](max) NULL,"
				 + "[institutionPercent] [nvarchar](max) NULL,"
				 + "[insiderPercent] [nvarchar](max) NULL,"
				 + "[shortRatio] [nvarchar](max) NULL,"
				 + "[year5ChangePercent] [nvarchar](max) NULL,"
				 + "[year2ChangePercent] [nvarchar](max) NULL,"
				 + "[year1ChangePercent] [nvarchar](max) NULL,"
				 + "[ytdChangePercent] [nvarchar](max) NULL,"
				 + "[month6ChangePercent] [nvarchar](max) NULL,"
				 + "[month3ChangePercent] [nvarchar](max) NULL,"
				 + "[month1ChangePercent] [nvarchar](max) NULL,"
				 + "[day5ChangePercent] [nvarchar](max) NULL,"
				 + ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]";
		
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);	
	}
	
	private static String dateWrapper(String date) throws java.text.ParseException {
		String formatted = SPMSDB.getMSSQLDatetime(date);
		if (formatted == null) {
			return null;
		}
		return "'" + formatted + "'";
	}
	
	private void updateRow(Stats s) throws SQLException, java.text.ParseException {
		Statement stmt = conn.createStatement();
		stmt.execute(makeUpdateCommand(tableName, "[companyName],[marketcap],[beta],[week52high],[week52low],[week52change],[shortInterest],[shortDate],[dividendRate],[dividendYield],[exDividendDate],[latestEPS],[latestEPSDate],[sharesOutstanding],[float],[returnOnEquity],[consensusEPS],[numberOfEstimates],[EBITDA],[revenue],[grossProfit],[cash],[debt],[ttmEPS],[revenuePerShare],[revenuePerEmployee],[peRatioHigh],[peRatioLow],[EPSSurpriseDollar],[EPSSurprisePercent],[returnOnAssets],[returnOnCapital],[profitMargin],[priceToSales],[priceToBook],[day200MovingAvg],[day50MovingAvg],[institutionPercent],[insiderPercent],[shortRatio],[year5ChangePercent],[year2ChangePercent],[year1ChangePercent],[ytdChangePercent],[month6ChangePercent],[month3ChangePercent],[month1ChangePercent],[day5ChangePercent]", quote(fixQ(s.companyName)) + quote(s.marketcap) + quote(s.beta) + quote(s.week52high) + quote(s.week52low) + quote(s.week52change) + quote(s.shortInterest) + dateWrapper(s.shortDate) + "," + quote(s.dividendRate) + quote(s.dividendYield) + dateWrapper(s.exDividendDate) + "," + quote(s.latestEPS) + dateWrapper(s.latestEPSDate) + "," +  quote(s.sharesOutstanding) + quote(s.Float) + quote(s.returnOnEquity) + quote(s.consensusEPS) + quote(s.numberOfEstimates) + quote(s.EBITDA) + quote(s.revenue) + quote(s.grossProfit) + quote(s.cash) + quote(s.debt) + quote(s.ttmEPS) + quote(s.revenuePerShare) + quote(s.revenuePerEmployee) + quote(s.peRatioHigh) + quote(s.peRatioLow) + quote(s.EPSSurpriseDollar) + quote(s.EPSSurprisePercent) + quote(s.returnOnAssets) + quote(s.returnOnCapital) + quote(s.profitMargin) + quote(s.priceToSales) + quote(s.priceToBook) + quote(s.day200MovingAvg) + quote(s.day50MovingAvg) + quote(s.institutionPercent) + quote(s.insiderPercent) + quote(s.shortRatio) + quote(s.year5ChangePercent) + quote(s.year2ChangePercent) + quote(s.year1ChangePercent) + quote(s.ytdChangePercent) + quote(s.month6ChangePercent) + quote(s.month3ChangePercent) + quote(s.month1ChangePercent) + quote(s.day5ChangePercent).substring(0,s.day5ChangePercent.length()-1)));
	}
	
	public static String fixQ(String s) {
		if (s == null)
			return "";
		else
			return s.replace("'", "''");
	}
	
	public void insertRow(Stats s) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO [" + tableName + "] ([companyName],[marketcap],[beta],[week52high],[week52low],[week52change],[shortInterest],[shortDate],[dividendRate],[dividendYield],[exDividendDate],[latestEPS],[latestEPSDate],[sharesOutstanding],[float],[returnOnEquity],[consensusEPS],[numberOfEstimates],[symbol],[EBITDA],[revenue],[grossProfit],[cash],[debt],[ttmEPS],[revenuePerShare],[revenuePerEmployee],[peRatioHigh],[peRatioLow],[EPSSurpriseDollar],[EPSSurprisePercent],[returnOnAssets],[returnOnCapital],[profitMargin],[priceToSales],[priceToBook],[day200MovingAvg],[day50MovingAvg],[institutionPercent],[insiderPercent],[shortRatio],[year5ChangePercent],[year2ChangePercent],[year1ChangePercent],[ytdChangePercent],[month6ChangePercent],[month3ChangePercent],[month1ChangePercent],[day5ChangePercent]) VALUES " + "('" +  fixQ(s.companyName) + "', '" + s.marketcap + "', '" + s.beta + "', '" + s.week52high+ "', '" + s.week52low + "', '" + s.week52change + "', '" + s.shortInterest + "', " + dateWrapper(s.shortDate) + ", '" + s.dividendRate + "', '" + s.dividendYield + "', " + dateWrapper(s.exDividendDate) + ", '" + s.latestEPS + "', " + dateWrapper(s.latestEPSDate) + ", '" + s.sharesOutstanding + "', '" + s.Float + "', '" + s.returnOnEquity+ "', '" + s.consensusEPS + "', '" + s.numberOfEstimates + "', '" + s.symbol + "', '" + s.EBITDA + "', '" + s.revenue + "', '" + s.grossProfit + "', '" + s.cash + "', '" + s.debt + "', '" + s.ttmEPS + "', '" + s.revenuePerShare + "', '" + s.revenuePerEmployee + "', '" + s.peRatioHigh + "', '" + s.peRatioLow + "', '" + s.EPSSurpriseDollar + "', '" + s.EPSSurprisePercent + "', '" + s.returnOnAssets + "', '" + s.returnOnCapital + "', '" + s.profitMargin + "', '" + s.priceToSales + "', '" + s.priceToBook + "', '" + s.day200MovingAvg + "', '" + s.day50MovingAvg + "', '" + s.institutionPercent + "', '" + s.insiderPercent+ "', '" + s.shortRatio + "', '" + s.year5ChangePercent + "', '" + s.year2ChangePercent+ "', '" + s.year1ChangePercent+ "', '" + s.ytdChangePercent+ "', '" + s.month6ChangePercent+ "', '" + s.month3ChangePercent+ "', '" + s.month1ChangePercent+ "', '" + s.day5ChangePercent + "')");
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
		}
	}
	
	public static void main(String[] args) throws SQLException {
		
	}
	
}
