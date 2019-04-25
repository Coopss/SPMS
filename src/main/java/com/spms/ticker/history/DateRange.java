package com.spms.ticker.history;

import java.util.Calendar;
import java.util.Date;

public enum DateRange {
	w,
	m,
	mmm,
	y,
	yyyyy,
	max;
	
	public static DateRange get(String s) {
		if (s.toLowerCase().equals("1w")) {
			return w;
		} else if (s.toLowerCase().equals("1m")) {
			return m;
		} else if (s.toLowerCase().equals("3m")) {
			return mmm;
		} else if (s.toLowerCase().equals("1y")) {
			return y;
		} else if (s.toLowerCase().equals("5y")) {
			return yyyyy;
		} else if (s.toLowerCase().equals("max")) {
			return max;
		} else {
			return null;
		}
	}
	
	public static Date getDateRangeOffsetFromNow(DateRange dr) {
	    Calendar calendar = Calendar.getInstance();    
	    switch (dr) {
	    case w:
	    	calendar.add(Calendar.DATE, -7);
	    	break;
	    case m:
	    	calendar.add(Calendar.MONTH, -1);
	    	break;
	    case mmm:
	    	calendar.add(Calendar.MONTH, -3);
	    	break;
	    case y:
	    	calendar.add(Calendar.YEAR, -1);
	    	break;
	    case yyyyy:
	    	calendar.add(Calendar.YEAR, -5);
	    	break;
	    case max:
	    	calendar.add(Calendar.YEAR, -20);
	    	break;
	    }
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	 
	    return calendar.getTime();
	}
	
}
