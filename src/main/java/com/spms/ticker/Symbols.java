package com.spms.ticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;
import com.spms.config.Config;
import com.spms.database.SPMSDB;
import com.spms.ticker.tools.Requests;


public class Symbols {

	private static final Logger log = LogManager.getLogger(Symbols.class);
	private static HashMap<String, String> testMap = new HashMap<String, String>();
	private final static String tableName = "internal.los";
	private static Connection conn;
	
	static {
		try {
			conn = SPMSDB.getConnection();
		} catch (SQLException e) {
			log.error(Util.stackTraceToString(e));
			System.exit(1);
		}
	}
	
	private static boolean createTable() throws SQLException {				
		if (SPMSDB.tableExists(conn, tableName)) {
			return false;
		}
				
		String makeTableCommand = "CREATE TABLE [dbo].[" + tableName + "]( "
				+ "[Symbol] [nvarchar](max) NOT NULL, "
				+ "[Name] [nvarchar](max) NOT NULL,"
				+ "[MarketCap] [numeric] NOT NULL,"
				+ "[Sector] [nvarchar](max) NOT NULL,"
				+ "[Industry] [nvarchar](max) NOT NULL,"
				+ ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, tableName);	
		
	}

	private static boolean populateTable() {
		String csvContent = Requests.followRedirect(Config.endpoint);
		String lines[] = csvContent.split("\\r?\\n");
		
		
		for (String line : lines) {
			
		}
		return false;
		
	}
	
	private static Map<String, String> rowToMap(String header, String row) {
		HashMap<String, String> map = new HashMap<String, String>();
		String headerArr[] = header.split(",");		
		return null;
	}
	
	
	public static void main(String[] args) throws SQLException, IOException {
		String csvContent = Requests.followRedirect(Config.endpoint);
		String lines[] = csvContent.split("\\r?\\n");
		
		log.info("Beginning symbol table reload");
		log.info("Dropping old symbol table: " + SPMSDB.dropTable(conn, tableName));
		log.info("Creating new symbol table: " + createTable());
		
		
		
//		String header = lines[0];
		
//		System.out.println(header);
		
		
//		for (String line : Arrays.copyOfRange(lines, 1, lines.length)) {
//			System.out.println(line);
			
			
//		}
		
//		System.out.println(csvContent);
//		testMap.values().stream().map(Object::toString).collect(Collectors.joining(","));
	}
	
	
}
