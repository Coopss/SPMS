package com.spms.ticker.live;

import java.net.MalformedURLException;
import java.sql.SQLException;

import com.spms.database.SPMSDB;

public class TickerTest {

	public static void main(String[] args) throws SQLException, MalformedURLException, InterruptedException {
		TickerDAO dao = new TickerDAO();
//		SPMSDB.dropTable(SPMSDB.getConnection(), dao.trackingListTableName);
//		
//		System.out.println(dao.createTrackTable());
//
//		System.out.println(dao.subscribe("TSLA", "Ryan"));
//		System.out.println(dao.subscribe("GOOG", "Ryan"));
//		System.out.println(dao.subscribe("AAPL", "Ryan"));
//		System.out.println(dao.subscribe("X", "Ryan"));
//		System.out.println(dao.subscribe("EA", "Ryan"));
//		System.out.println(dao.subscribe("FOOBAR", "Ryan"));
//		System.out.println(dao.subscribe("AAPL", "Terence"));
//		System.out.println(dao.subscribe("AAPL", "Ryan"));
//		System.out.println(dao.unsubscribe("FOOBAR", "Ryan"));
//
//		System.out.println(dao.getAllSubscribedSymbols());
		
		Thread liveFetch = new Thread(new LiveFetch());
		liveFetch.start();
		
		
		Thread.sleep(15000);
		System.out.println(dao.subscribe("X", "Ryan"));
		Thread.sleep(15000);
		System.out.println(dao.subscribe("EA", "Ryan"));
		
	}

}
