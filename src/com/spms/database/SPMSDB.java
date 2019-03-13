package com.spms.database;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.spms.config.ConfigLoader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class SPMSDB {
	
	/**
	 * Open a new connection to the Azure SQL server. 
	 * Note: Closing the returned object is left up to the caller.
	 * @param dbName name of the database to connect to
	 * @return Open connection to the database
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		 // Connect to database
        String hostName = ConfigLoader.getProp("spms.db.hostName");
        String user = ConfigLoader.getProp("spms.db.user");
        String dbName = ConfigLoader.getProp("spms.db.masterDB");
        String password = ConfigLoader.getProp("spms.db.password");
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;
        
        connection = DriverManager.getConnection(url);
        String schema = connection.getSchema();
        System.err.println("Successful connection: " + dbName);
        
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
			
		return true;	
		
	}

	public static String getMSSQLDatetime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date javaDate = new Date();
		String msSqlDate = sdf.format(javaDate).trim();
		return msSqlDate.replace(" ","T").toString();
	}
}
