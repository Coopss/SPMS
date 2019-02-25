package com.spms.ticker.live;

import java.sql.SQLException;

import com.spms.database.SPMSDB;

public class TickerTest {

	public static void main(String[] args) throws SQLException {
		TickerDAO dao = new TickerDAO();
		System.out.println(dao.createTickerTable("TESTTABLE"));
		System.out.println(SPMSDB.dropTable(SPMSDB.getConnection(), "ticker.TESTTABLE"));
	}

}
