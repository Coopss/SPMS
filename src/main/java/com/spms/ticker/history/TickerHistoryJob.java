package com.spms.ticker.history;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;

public class TickerHistoryJob implements Runnable {
	private static final Logger log = LogManager.getLogger(TickerHistoryJob.class);
	
	@Override
	public void run() {
		log.info("TickerHistoryJob started " + this);
		while (true) {
			timeout();
			
			log.info("Started " + this);
			TickerHistoryController controller;
			try {
				controller = new TickerHistoryController();
				controller.reload();
				controller = null;
				
			} catch (SQLException e) {
				log.error("Failed to reload historical data -- Critical");
				log.error(Util.stackTraceToString(e));
			}	
		}		
	}
	
	public void timeout() {
		try {
			Thread.sleep(Util.getTimeout(5));
		} catch (InterruptedException e) {
			log.error("Failed to sleep.");
			log.error(Util.stackTraceToString(e));
		}
	}
}
