package com.spms.ticker.los;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.spms.Util;
import com.spms.config.Config;
import com.spms.database.SPMSDB;
import com.spms.ticker.tools.Requests;


public class SymbolDAO {

	private static final Logger log = LogManager.getLogger(SymbolDAO.class);
	private final static String tableName = "internal.los";
	private Connection conn;
	
	{
		try {
			conn = SPMSDB.getConnection();
		} catch (SQLException e) {
			log.error(Util.stackTraceToString(e));
			System.exit(1);
		}
	}
	
	private static void addAdditionalInfo(Symbol s) throws MalformedURLException, ParseException {
		String urlExt = "/stock/" + s.Symbol.toLowerCase() + "/company";
		JSONObject jobj = (JSONObject) Requests.get(urlExt, Requests.ReturnType.object);
		
		if (jobj.containsKey("description")) {
			s.Description = ((String) jobj.get("description"));
		}
		if (jobj.containsKey("CEO")) {
			s.CEO = (String) jobj.get("CEO");
		}
		if (jobj.containsKey("website")) {
			s.URL = (String) jobj.get("website");
		}
	}
	
	private boolean createTable() throws SQLException {				
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = "CREATE TABLE [dbo].[" + tableName + "]( "
				+ "[Symbol] [nvarchar](max) NOT NULL, "
				+ "[Name] [nvarchar](max) NOT NULL,"
				+ "[MarketCap] [nvarchar](max) NOT NULL,"
				+ "[Sector] [nvarchar](max) NOT NULL,"
				+ "[Industry] [nvarchar](max) NOT NULL,"
				+ "[CEO] [nvarchar](max) NOT NULL,"
				+ "[Description] [nvarchar](max) NOT NULL,"
				+ "[Website] [nvarchar](max) NOT NULL,"
				+ ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);	
		
	}

	private boolean populateTable() {
		String csvContent = Requests.followRedirect(Config.endpoint);
		String lines[] = csvContent.split("\\r?\\n");
		String header = lines[0];	
		
		for (String line : Arrays.copyOfRange(lines, 1, lines.length)) {
			try {
				log.info("Inserting " + line);
				insertRow(rowToSymbol(header, line));
			} catch (SQLException e) {
				log.error("Failed to insert into " + tableName + " : " + line);
				log.error(Util.stackTraceToString(e));
				return false;
			}
		}
	
		return true;
		
	}
	
	private Boolean insertRow(Symbol sym) throws SQLException {
		Statement stmt = conn.createStatement();
		
		if (sym.Description == null) {
			sym.Description = "";
		}
		if (sym.CEO == null) {
			sym.CEO = "";
		}
		stmt.executeUpdate("INSERT INTO [" + tableName + "] (Symbol, Name, MarketCap, Sector, Industry, CEO, Description, Website) VALUES ('" + sym.Symbol + "', '" + sym.Name + "', '" + sym.MarketCap + "', '" + sym.Sector + "', '"  + sym.Industry + "', '"  + sym.CEO.replaceAll("'", "") + "', '"  + sym.Description.replaceAll("'", "") + "', '"  + sym.URL + "');");
			
		return true;
	}
	
	private static Symbol rowToSymbol(String header, String row) {
		Symbol sym = new Symbol();
		List<String> headerArr = Arrays.asList(header.split(","));	
		List<String> rowArr = Util.parseLine(row, ',', '"');
				
		for (int i = 0 ; i < headerArr.size(); i++) {	
			switch(headerArr.get(i).trim().replaceAll("\"", "")) {
			case "Symbol":
				sym.Symbol = rowArr.get(i);
				break;
			case "Name":
				sym.Name = rowArr.get(i);
				break;
			case "MarketCap":
				sym.MarketCap = rowArr.get(i);
				break;
			case "Sector":
				sym.Sector = rowArr.get(i);
				break;
			case "industry":
				sym.Industry = rowArr.get(i);
				break;
			}
		}
		try {
			addAdditionalInfo(sym);
		} catch (MalformedURLException | ParseException | java.lang.NullPointerException e) {
			log.error(Util.stackTraceToString(e));
		}
		return sym;
	}
	
	public Boolean reloadSymbols() throws SQLException {
		log.info("Beginning symbol table reload");
		log.info("Dropping old symbol table: " + SPMSDB.dropTable(conn, tableName));
		log.info("Creating new symbol table: " + createTable());
		log.info("Populate symbol table from NASDAQ data: " + populateTable());
		return true;
	}
	
	public List<Symbol> search(String query) throws SQLException {
		List<Symbol> syms = new ArrayList<Symbol>();
		PreparedStatement stmt = conn.prepareStatement("select Symbol,Name,Sector from [internal.los] where [Name] like '%" + query + "%' OR [Symbol] like '%" + query + "%' ORDER BY 5 * Difference([Symbol], '" + query + "') + Difference([Name], '" + query + "') DESC");
				
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next() && syms.size() < 10) {
	    	Symbol s = new Symbol();
	    	s.Symbol = rs.getString(1);
	    	s.Name = rs.getString(2);
	    	s.Sector = rs.getString(3);
	    	syms.add(s);
	    }
	    return syms;
	}
	
	public List<Symbol> getAll() throws SQLException {
		List<Symbol> syms = new ArrayList<Symbol>();
		PreparedStatement stmt = conn.prepareStatement("select Symbol from [internal.los]");
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	Symbol s = new Symbol();
	    	s.Symbol = rs.getString(1);
	    	syms.add(s);
	    }
	    return syms;
	}
	
	public Symbol get(String ticker) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("select [Symbol],[Name],[MarketCap],[Sector],[Industry],[CEO],[Description],[Website] from [internal.los] where Symbol = '" + ticker.toUpperCase() + "';");
		ResultSet rs = stmt.executeQuery();		
		Symbol s = new Symbol();
	    while(rs.next()) {
	    	s.Symbol = rs.getString(1);
	    	s.Name = rs.getString(2);
	    	s.MarketCap = rs.getString(3);
	    	s.Sector = rs.getString(4);
	    	s.Industry = rs.getString(5);
	    	s.CEO = rs.getString(6);
	    	s.Description = rs.getString(7);
	    	s.URL = rs.getString(8);
	    }
	    
	    return s;
	}
	
	public static void main(String[] args) throws SQLException, IOException, ParseException {
		SymbolDAO s = new SymbolDAO();
		s.reloadSymbols();
		
	}
	
	
}
