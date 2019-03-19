package com.spms.ticker.history;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.spms.api.auth.AuthDAO;
import com.spms.api.auth.AuthUtil;
import com.spms.api.auth.Credentials;
import com.spms.database.SPMSDB;

public class TickerHistoryTest {

	public static void main(String[] args) throws SQLException {
		TickerHistoryDAO dao = new TickerHistoryDAO();
		//System.out.println(dao.createTickerHistoryTable("TESTTABLE"));
		//dao.insertIntoTable("TESTTABLE", "('2017-12-15',143.98,143.98,143.775,143.775,3070)");
		//System.out.println(SPMSDB.dropTable(SPMSDB.getConnection(), "ticker.TESTTABLE.history"));
		dao.updateTickerHistoryTable();
	}

}
