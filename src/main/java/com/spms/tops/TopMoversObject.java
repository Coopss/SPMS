package com.spms.tops;

public class TopMoversObject {
	public String symbol;
	public String change;
	public String changePercent;
	public String currentPrice;
	
	TopMoversObject(String s, String ch, String chp) {
		this.symbol = s;
		this.change = ch;
		this.changePercent = chp;
	}
	
	@Override
	public String toString() {
		return "{" + this.symbol + "," + this.change + "," + this.changePercent + "}";
	}
}
