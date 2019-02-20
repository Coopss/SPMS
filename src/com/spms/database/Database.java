package com.spms.database;

import java.sql.Connection;
import java.sql.Statement;

import com.spms.config.ConfigLoader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Database {
	
	/**
	 * Open a new connection to the Azure SQL server. 
	 * Note: Closing the returned object is left up to the caller.
	 * @return Open connection to the database
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		 // Connect to database
        String hostName = ConfigLoader.getProp("spms.db.hostName");
        String dbName = ConfigLoader.getProp("spms.db.tickerPriceDbName");
        String user = ConfigLoader.getProp("spms.db.user");
        String password = ConfigLoader.getProp("spms.db.password");
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;
        
        System.out.println(url);
        
        connection = DriverManager.getConnection(url);
        String schema = connection.getSchema();
        System.out.println("Successful connection - Schema: " + schema);
        
        return connection;
	}
}
