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
		System.out.println(dao.createTickerHistoryTable("TESTTABLE"));
		//System.out.println(SPMSDB.dropTable(SPMSDB.getConnection(), "ticker.TESTTABLE.history"));
		
	}

}
