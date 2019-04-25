package com.spms.ticker.stats;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Controller;
import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;
import com.spms.tops.TopMoversController;
import com.spms.tops.TopMoversDAO;

public class StatsController implements Controller {
	private StatsDAO sdao;
	private SymbolDAO dao;
	private static final Logger log = LogManager.getLogger(StatsController.class);
	
	StatsController() throws SQLException {
		sdao = new StatsDAO();
		dao = new SymbolDAO();
	}
	
	public boolean reload() {
		ArrayList<Symbol> allSyms;
		
		try {
			allSyms = (ArrayList<Symbol>) dao.getAll();
		} catch (Exception e) {
			log.error(Util.stackTraceToString(e));
			return false;
		}
		
		log.info("Beginning ticker statistics table reload");
		log.info("Beginning insertion of data");
		
		for (Symbol sym : allSyms) {
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
				
				sdao.insertRow(s);
				log.info("Inserted statistics for " + s.symbol);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				return false;
			}
		}
		
		return true;
	}
	
	private String getURLExt(String symbol) {
		return "/stock/" + symbol + "/stats";
	}
	
	public static void main(String[] args) throws SQLException, IOException, ParseException, java.text.ParseException {
		StatsController sc = new StatsController();
		StatsDAO stdao = new StatsDAO();
		stdao.dropTable();
		stdao.createTable();
		sc.reload();
	}
	
}
