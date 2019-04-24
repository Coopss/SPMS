 package com.spms;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.ticker.history.TickerHistoryJob;
import com.spms.ticker.live.TickerJob;


public class BatchJobRunner extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Boolean isProd = false;
	private static final Logger log = LogManager.getLogger(BatchJobRunner.class);
	
	public void init() throws ServletException {
		log.info("----------");
		log.info("----------  Initialized successfully ----------");
		log.info("----------");
		
		
		if (isProd) {
			
			// live ticker job
			Thread tickerJob;
			try {
				tickerJob = new Thread(new TickerJob());
				tickerJob.start();
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init TickerJob()");
				System.exit(1);
			}
			
			// ticker history
			Thread tickerHistoryJob;
			try {
				tickerHistoryJob = new Thread(new TickerHistoryJob());
				tickerHistoryJob.start();
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init TickerHistoryJob()");
				System.exit(1);
			}
			
		}
    }
}
