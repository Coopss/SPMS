package com.spms.database;

import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.api.AuthenticationService;
import com.spms.config.Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class SPMSDB {
	

	private static final Logger log = LogManager.getLogger(SPMSDB.class);
	
	/**
	 * Open a new connection to the Azure SQL server. 
	 * Note: Closing the returned object is left up to the caller.
	 * @param dbName name of the database to connect to
	 * @return Open connection to the database
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		 // Connect to database
        String hostName = Config.dbHostName;
        String user = Config.dbuser;
        String dbName = Config.masterDB;
        String password = Config.dbPassword;
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;
        
        try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        connection = DriverManager.getConnection(url);
        String schema = connection.getSchema();
        log.info("Successful connection: " + dbName);
        
        return connection;
	}
	
	public static boolean tableExists(Connection conn, String tableName) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("select * from sys.tables where name = '" + tableName + "';");
	    ResultSet rs = stmt.executeQuery();
		
	    while(!rs.next()) {
	    	return false;
	    }
	        
	    return true;
	}	
	
	
	public static boolean dropTable(Connection conn, String tableName) throws SQLException {
		if (!tableExists(conn, tableName)) {
			return false;
		}
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE [dbo].[" + tableName + "]");
			
		log.info("Dropped table: " + tableName);
		return true;	
		
	}

	public static String getMSSQLDatetime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date javaDate = new Date();
		String msSqlDate = sdf.format(javaDate).trim();
		return msSqlDate.replace(" ","T").toString();
	}
	
	public static String getMSSQLDatetime(String date) throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);  
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String msSqlDate = sdf.format(date1).trim();
		return msSqlDate.replace(" ","T").toString();
	}
}
