package com.spms.ticker.stats;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.live.TickerData;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class StatsDAO {
	private String tableName = "ticker.stats";
	private static final Logger log = LogManager.getLogger(StatsDAO.class);
	private Connection conn;
	private SymbolDAO sdao;
	
	public StatsDAO() {
		try {
			conn = SPMSDB.getConnection();
		} catch (SQLException e) {
			log.error(Util.stackTraceToString(e));
		}
		
		sdao = new SymbolDAO();
		
	}
	
	private String getURLExt(String symbol) {
		return "/stock/" + symbol + "/stats";
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
	
	public boolean insertRow(Stats s) throws SQLException, java.text.ParseException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("INSERT INTO [" + tableName + "] ([companyName],[marketcap],[beta],[week52high],[week52low],[week52change],[shortInterest],[shortDate],[dividendRate],[dividendYield],[exDividendDate],[latestEPS],[latestEPSDate],[sharesOutstanding],[float],[returnOnEquity],[consensusEPS],[numberOfEstimates],[symbol],[EBITDA],[revenue],[grossProfit],[cash],[debt],[ttmEPS],[revenuePerShare],[revenuePerEmployee],[peRatioHigh],[peRatioLow],[EPSSurpriseDollar],[EPSSurprisePercent],[returnOnAssets],[returnOnCapital],[profitMargin],[priceToSales],[priceToBook],[day200MovingAvg],[day50MovingAvg],[institutionPercent],[insiderPercent],[shortRatio],[year5ChangePercent],[year2ChangePercent],[year1ChangePercent],[ytdChangePercent],[month6ChangePercent],[month3ChangePercent],[month1ChangePercent],[day5ChangePercent]) VALUES " + "('" +  s.companyName + "', '" + s.marketcap + "', '" + s.beta + "', '" + s.week52high+ "', '" + s.week52low + "', '" + s.week52change + "', '" + s.shortInterest + "', " + dateWrapper(s.shortDate) + ", '" + s.dividendRate + "', '" + s.dividendYield + "', " + dateWrapper(s.exDividendDate) + ", '" + s.latestEPS + "', " + dateWrapper(s.latestEPSDate) + ", '" + s.sharesOutstanding + "', '" + s.Float + "', '" + s.returnOnEquity+ "', '" + s.consensusEPS + "', '" + s.numberOfEstimates + "', '" + s.symbol + "', '" + s.EBITDA + "', '" + s.revenue + "', '" + s.grossProfit + "', '" + s.cash + "', '" + s.debt + "', '" + s.ttmEPS + "', '" + s.revenuePerShare + "', '" + s.revenuePerEmployee + "', '" + s.peRatioHigh + "', '" + s.peRatioLow + "', '" + s.EPSSurpriseDollar + "', '" + s.EPSSurprisePercent + "', '" + s.returnOnAssets + "', '" + s.returnOnCapital + "', '" + s.profitMargin + "', '" + s.priceToSales + "', '" + s.priceToBook + "', '" + s.day200MovingAvg + "', '" + s.day50MovingAvg + "', '" + s.institutionPercent + "', '" + s.insiderPercent+ "', '" + s.shortRatio + "', '" + s.year5ChangePercent + "', '" + s.year2ChangePercent+ "', '" + s.year1ChangePercent+ "', '" + s.ytdChangePercent+ "', '" + s.month6ChangePercent+ "', '" + s.month3ChangePercent+ "', '" + s.month1ChangePercent+ "', '" + s.day5ChangePercent + "')");
		
		return false;
	}
	
	public void reload() throws SQLException, MalformedURLException, ParseException, java.text.ParseException {
		log.info("Beginning ticker statistics table reload");
		log.info("Dropping old table: " + SPMSDB.dropTable(conn, tableName));
		log.info("Creating new table: " + createTable());
		log.info("Beginning insertion of data");
		
		for (Symbol sym : sdao.getAll()) {
			try {
				JSONObject jobj = (JSONObject) Requests.get(getURLExt(sym.Symbol), Requests.ReturnType.object);
				Stats s = new Stats();
				for (Object key : jobj.keySet()) {
				
					if (jobj.get(key) == null) {
						continue;
					}
					
					switch ((String) key) {
					  	case "companyName":
		                    s.companyName = (String) jobj.get("companyName").toString();
		                    if (s.companyName.equals("0")) {
		                    	s.companyName = null;
		                    }
		                    break;
		                case "marketcap":
		                    s.marketcap = (String) jobj.get("marketcap").toString();
		                    break;
		                case "beta":
		                    s.beta = (String) jobj.get("beta").toString();
		                    break;
		                case "week52high":
		                    s.week52high = (String) jobj.get("week52high").toString();
		                    break;
		                case "week52low":
		                    s.week52low = (String) jobj.get("week52low").toString();
		                    break;
		                case "week52change":
		                    s.week52change = (String) jobj.get("week52change").toString();
		                    break;
		                case "shortInterest":
		                    s.shortInterest = (String) jobj.get("shortInterest").toString();
		                    break;
		                case "shortDate":
		                    s.shortDate = (String) jobj.get("shortDate").toString();
		                    break;
		                case "dividendRate":
		                    s.dividendRate = (String) jobj.get("dividendRate").toString();
		                    break;
		                case "dividendYield":
		                    s.dividendYield = (String) jobj.get("dividendYield").toString();
		                    break;
		                case "exDividendDate":
		                    s.exDividendDate = (String) jobj.get("exDividendDate").toString();
		                    break;
		                case "latestEPS":
		                    s.latestEPS = (String) jobj.get("latestEPS").toString();
		                    break;
		                case "latestEPSDate":
		                    s.latestEPSDate = (String) jobj.get("latestEPSDate").toString();
		                    break;
		                case "sharesOutstanding":
		                    s.sharesOutstanding = (String) jobj.get("sharesOutstanding").toString();
		                    break;
		                case "float":
		                	if (jobj.get("Float") != null) {
		                        s.Float = (String) jobj.get("Float").toString();
		                	} else {
		                		s.Float = null;
		                	}
		                	break;
		
		                case "returnOnEquity":
		                    s.returnOnEquity = (String) jobj.get("returnOnEquity").toString();
		                    break;
		                case "consensusEPS":
		                    s.consensusEPS = (String) jobj.get("consensusEPS").toString();
		                    break;
		                case "numberOfEstimates":
		                    s.numberOfEstimates = (String) jobj.get("numberOfEstimates").toString();
		                    break;
		                case "symbol":
		                    s.symbol = (String) jobj.get("symbol").toString();
		                    break;
		                case "EBITDA":
		                    s.EBITDA = (String) jobj.get("EBITDA").toString();
		                    break;
		                case "revenue":
		                    s.revenue = (String) jobj.get("revenue").toString();
		                    break;
		                case "grossProfit":
		                    s.grossProfit = (String) jobj.get("grossProfit").toString();
		                    break;
		                case "cash":
		                    s.cash = (String) jobj.get("cash").toString();
		                    break;
		                case "debt":
		                    s.debt = (String) jobj.get("debt").toString();
		                    break;
		                case "ttmEPS":
		                    s.ttmEPS = (String) jobj.get("ttmEPS").toString();
		                    break;
		                case "revenuePerShare":
		                    s.revenuePerShare = (String) jobj.get("revenuePerShare").toString();
		                    break;
		                case "revenuePerEmployee":
		                    s.revenuePerEmployee = (String) jobj.get("revenuePerEmployee").toString();
		                    break;
		                case "peRatioHigh":
		                    s.peRatioHigh = (String) jobj.get("peRatioHigh").toString();
		                    break;
		                case "peRatioLow":
		                    s.peRatioLow = (String) jobj.get("peRatioLow").toString();
		                    break;
		                case "EPSSurpriseDollar":
		                    s.EPSSurpriseDollar = (String) jobj.get("EPSSurpriseDollar").toString();
		                    break;
		                case "EPSSurprisePercent":
		                    s.EPSSurprisePercent = (String) jobj.get("EPSSurprisePercent").toString();
		                    break;
		                case "returnOnAssets":
		                    s.returnOnAssets = (String) jobj.get("returnOnAssets").toString();
		                    break;
		                case "returnOnCapital":
		                    s.returnOnCapital = (String) jobj.get("returnOnCapital").toString();
		                    break;
		                case "profitMargin":
		                    s.profitMargin = (String) jobj.get("profitMargin").toString();
		                    break;
		                case "priceToSales":
		                    s.priceToSales = (String) jobj.get("priceToSales").toString();
		                    break;
		                case "priceToBook":
		                    s.priceToBook = (String) jobj.get("priceToBook").toString();
		                    break;
		                case "day200MovingAvg":
		                    s.day200MovingAvg = (String) jobj.get("day200MovingAvg").toString();
		                    break;
		                case "day50MovingAvg":
		                    s.day50MovingAvg = (String) jobj.get("day50MovingAvg").toString();
		                    break;
		                case "institutionPercent":
		                    s.institutionPercent = (String) jobj.get("institutionPercent").toString();
		                    break;
		                case "insiderPercent":
		                    s.insiderPercent = (String) jobj.get("insiderPercent").toString();
		                    break;
		                case "shortRatio":
		                    s.shortRatio = (String) jobj.get("shortRatio").toString();
		                    break;
		                case "year5ChangePercent":
		                    s.year5ChangePercent = (String) jobj.get("year5ChangePercent").toString();
		                    break;
		                case "year2ChangePercent":
		                    s.year2ChangePercent = (String) jobj.get("year2ChangePercent").toString();
		                    break;
		                case "year1ChangePercent":
		                    s.year1ChangePercent = (String) jobj.get("year1ChangePercent").toString();
		                    break;
		                case "ytdChangePercent":
		                    s.ytdChangePercent = (String) jobj.get("ytdChangePercent").toString();
		                    break;
		                case "month6ChangePercent":
		                    s.month6ChangePercent = (String) jobj.get("month6ChangePercent").toString();
		                    break;
		                case "month3ChangePercent":
		                    s.month3ChangePercent = (String) jobj.get("month3ChangePercent").toString();
		                    break;
		                case "month1ChangePercent":
		                    s.month1ChangePercent = (String) jobj.get("month1ChangePercent").toString();
		                    break;
		                case "day5ChangePercent":
		                    s.day5ChangePercent = (String) jobj.get("day5ChangePercent").toString();
		                    break;
					}
				}
				
				insertRow(s);
				log.info("Inserted statistics for " + s.symbol);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
			}
		}
	}
	
	public Stats get(String ticker) throws SQLException {		
		PreparedStatement stmt = conn.prepareStatement("Select [symbol],[marketcap],[beta],[week52high],[week52low],[week52change],[latestEPS],[latestEPSDate],[sharesOutstanding],[returnOnEquity],[peRatioHigh],[peRatioLow],[priceToSales],[priceToBook],[day200MovingAvg],[day50MovingAvg],[year5ChangePercent],[year2ChangePercent],[year1ChangePercent],[ytdChangePercent],[month6ChangePercent],[month3ChangePercent],[month1ChangePercent],[day5ChangePercent] from [dbo].[" + tableName + "] where symbol = '" + ticker + "';");
		
		ResultSet rs = stmt.executeQuery();		
		Stats s = new Stats();
		
		while(rs.next()) {
			s.symbol = rs.getString(1);
			s.marketcap = rs.getString(2);
			s.beta = rs.getString(3);
			s.week52high = rs.getString(4);
			s.week52low = rs.getString(5);
			s.week52change = rs.getString(6);
			s.latestEPS = rs.getString(7);
			s.latestEPSDate = rs.getString(8);
			s.sharesOutstanding = rs.getString(9);
			s.returnOnEquity = rs.getString(10);
			s.peRatioHigh = rs.getString(11);
			s.peRatioLow = rs.getString(12);
			s.priceToSales = rs.getString(13);
			s.priceToBook = rs.getString(14);
			s.day200MovingAvg = rs.getString(15);
			s.day50MovingAvg = rs.getString(16);
			s.year5ChangePercent = rs.getString(17);
			s.year2ChangePercent = rs.getString(18);
			s.year1ChangePercent = rs.getString(19);
			s.ytdChangePercent = rs.getString(20);
			s.month6ChangePercent = rs.getString(21);
			s.month3ChangePercent = rs.getString(22);
			s.month1ChangePercent = rs.getString(23);
			s.day5ChangePercent = rs.getString(24);
	    }
		
		return s;
		
	}
	
	public static void main(String[] args) throws SQLException, IOException, ParseException, java.text.ParseException {
		StatsDAO sdao = new StatsDAO();
		sdao.reload();
	}
	
}
