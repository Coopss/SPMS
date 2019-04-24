package com.spms.news;

import java.sql.Date;

public class NewsArticle {
	Date date;
	String headline;
	String source;
	String url;
	String summary;
	String image;
	
	NewsArticle(Date d, String h, String s, String u, String sum, String im) {
		this.date = d;
		this.headline = h;
		this.source = s;
		this.url = u;
		this.summary = sum;
		this.image = im;
	}

}
