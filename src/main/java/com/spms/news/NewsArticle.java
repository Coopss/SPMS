package com.spms.news;

import java.sql.Date;

public class NewsArticle {
	Date date;
	String symbol;
	String headline;
	String source;
	String url;
	String summary;
	String image;
	
	NewsArticle(Date d, String sym, String h, String s, String u, String sum, String im) {
		this.date = d;
		this.symbol = sym;
		this.headline = h;
		this.source = s;
		this.url = u;
		this.summary = sum;
		this.image = im;
	}

}
