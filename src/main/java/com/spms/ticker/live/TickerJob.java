package com.spms.ticker.live;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;


public class TickerJob implements Runnable {
	private static final Logger log = LogManager.getLogger(TickerJob.class);
	private static final Integer TIMEOUT = 1200000; // 20 min
	
	@Override
	public void run() {
		while (true) {
			log.info("Started " + this);
			TickerController tc;
			try {
				tc = new TickerController();
				tc.reload();
				tc = null;
				
			} catch (SQLException e) {
				log.error("Failed to reload live data -- Critical");
				log.error(Util.stackTraceToString(e));
			}	
		}
		
	}
	
	public void timeout() {
		try {
			Thread.sleep(TIMEOUT);
		} catch (InterruptedException e) {
			log.error("Failed to sleep.");
			log.error(Util.stackTraceToString(e));
		}
	}

	
}
