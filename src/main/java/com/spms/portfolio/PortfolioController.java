package com.spms.portfolio;

import java.sql.SQLException;

import com.spms.Util;
import com.spms.database.SPMSDB;

public class PortfolioController {
	public static void main(String[] arg) throws SQLException {
		PortfolioDAO dao = new PortfolioDAO();
		dao.createTransHistTable();
		dao.insert("test", "aapl", "2019-04-24 11:37:00.000", "5", "75");
	}
}
