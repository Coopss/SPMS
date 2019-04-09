 package com.spms;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.ticker.live.LiveFetchJob;


public class BatchJobRunner extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Boolean isProd = false;
	private static final Logger log = LogManager.getLogger(BatchJobRunner.class);
	
	public void init() throws ServletException {
		log.info("----------");
		log.info("----------  Initialized successfully ----------");
		log.info("----------");
		
		
		if (isProd) {
			Thread liveFetch;
			try {
				liveFetch = new Thread(new LiveFetchJob());
				liveFetch.start();
			} catch (SQLException e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init LiveFetchJob()");
				System.exit(1);
			}
		}
    }
}
