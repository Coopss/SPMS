package com.spms;

import java.io.PrintWriter;
import java.io.StringWriter;

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
	
}
