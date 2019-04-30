package com.spms.portfolio;

import java.io.Serializable;

public class PurchaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String symbol;
	private String date;
	private String shares;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getShares() {
		return shares;
	}
	public void setShares(String shares) {
		this.shares = shares;
	}


}
