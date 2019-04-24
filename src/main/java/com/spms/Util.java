package com.spms;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {

	public static String stackTraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public static String removeProtocol(String url) {
		return url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
	}
	
	public static String capitalizeFirstLetter(String original) {
	    if (original == null || original.length() == 0) {
	        return original;
	    }
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {
        List<String> result = new ArrayList<>();
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = '"';
        }

        if (separators == ' ') {
            separators = ',';
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

	public static String objectToString(Object o) {
		if (o == null) {
			return null;
		} else {
			return o.toString();
		}
  }
	
	/**
	 * Get time until next X oclock
	 */
	public static Long getTimeout(Integer hour) {
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.HOUR_OF_DAY) >= hour) {
			cal.add(Calendar.DATE, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		Date nextRuntime = cal.getTime();
		Date now = new Date();
		
		return nextRuntime.getTime() - now.getTime();
	}
	
	public static Date getDateWithoutTimeUsingCalendar() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DATE, -1);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	 
	    return calendar.getTime();
	}
	
	public static Long howLongAgo(Date oldDate) {
		Date now = new Date();
		return now.getTime() - oldDate.getTime();
	}
}
