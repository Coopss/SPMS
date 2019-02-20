package com.spms;

import java.sql.SQLException;

import com.spms.config.ConfigLoader;
import com.spms.database.Database;

public class Test {

	public static void main(String[] args) throws SQLException {
		String version = ConfigLoader.getProp("spms.version");
		Database.getConnection();
		System.out.println(version);
		
	}

}
