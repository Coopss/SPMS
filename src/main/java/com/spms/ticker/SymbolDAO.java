package com.spms.ticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
		stmt.executeUpdate("INSERT INTO [" + tableName + "] (Symbol, Name, MarketCap, Sector, Industry) VALUES ('" + sym.Symbol + "', '" + sym.Name + "', '" + sym.MarketCap + "', '" + sym.Sector + "', '"  + sym.Industry + "');");
			
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
		PreparedStatement stmt = conn.prepareStatement("select Symbol,Name,Sector from [internal.los] where [Name] like '%" + query + "%' OR [Symbol] like '%" + query + "%'");
				
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
	
	public static void main(String[] args) throws SQLException, IOException {
		SymbolDAO s = new SymbolDAO();
		s.reloadSymbols();
	}
	
	
}
