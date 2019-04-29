package com.spms.news;

import java.util.Date;

public class NewsArticle {
	public Date date;
	public String symbol;
	public String headline;
	public String source;
	public String url;
	public String summary;
	public String image;
	
	public NewsArticle() {}
	
	public NewsArticle(Date d, String sym, String h, String s, String u, String sum, String im) {
		this.date = d;
		this.symbol = sym;
		this.headline = h;
		this.source = s;
		this.url = u;
		this.summary = sum;
		this.image = im;
	}

}
