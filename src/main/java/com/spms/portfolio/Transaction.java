package com.spms.portfolio;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transaction {
	
	private final Logger log = LogManager.getLogger(PortfolioController.class);
	
	String user;
	String symbol;
	String date;
	Integer shares;
	Float sharePrice;
	
	public Transaction() {
		
	}	
	
	public Transaction(String user, String symbol, Date date, String shares, String sharePrice) {
		this.user = user;
		this.symbol = symbol.toUpperCase();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String msSqlDate = sdf.format(date).trim();
		this.date = msSqlDate;
		this.shares = Integer.parseInt(shares);
		this.sharePrice = Float.parseFloat(sharePrice);		
	}
	
	
	public Transaction(String user, String symbol, String date, String shares, String sharePrice) {
		this.user = user;
		this.symbol = symbol.toUpperCase();
		this.date = date;
		this.shares = Integer.parseInt(shares);
		this.sharePrice = Float.parseFloat(sharePrice);		
	}
	
	public Transaction(String user, String symbol, String date, Integer shares, Float sharePrice) {
		this.user = user;
		this.symbol = symbol.toUpperCase();
		this.date = date;
		this.shares = shares;
		this.sharePrice = sharePrice;
	}
	
	
	@Override
	public String toString() {
		return this.date + " " + this.user + " " + this.symbol + " " + this.shares.toString() + " " + this.sharePrice.toString();
	}
	
}
