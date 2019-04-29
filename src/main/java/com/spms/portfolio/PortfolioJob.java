package com.spms.portfolio;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;


public class PortfolioJob implements Runnable {
	private static final Logger log = LogManager.getLogger(PortfolioJob.class);
	private static final Integer TIMEOUT = 1200000; // 20 min
	
	@Override
	public void run() {
		log.info("PortfolioJob started " + this);
		while (true) {
			timeout(); 
			PortfolioController pc;
			try {
				pc = new PortfolioController();
				log.info("Started " + pc);
				pc.reload();
				log.info("Finished " + pc);
				pc = null;
			} catch (SQLException e) {
				log.error("Failed to reload portfolio data -- Critical");
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
