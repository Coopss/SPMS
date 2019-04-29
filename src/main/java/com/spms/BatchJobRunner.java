 package com.spms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.news.NewsJob;
import com.spms.portfolio.PortfolioJob;
import com.spms.ticker.history.TickerHistoryJob;
import com.spms.ticker.live.TickerJob;
import com.spms.ticker.stats.StatsJob;
import com.spms.tops.TopMoversJob;

public class BatchJobRunner extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Map<BatchJobs, Boolean> enabled;
	private static final List<Thread> batchJobThreads;
	private static final Logger log = LogManager.getLogger(BatchJobRunner.class);
	
	static {
		enabled = new HashMap<BatchJobs, Boolean>();
		batchJobThreads = new ArrayList<Thread>();
		
		enabled.put(BatchJobs.tickerJob, true);
		enabled.put(BatchJobs.tickerHistoryJob, true);
		enabled.put(BatchJobs.topMoversJob, false);
		enabled.put(BatchJobs.newsJob, false);
		enabled.put(BatchJobs.statsJob, true);
		enabled.put(BatchJobs.portfolioJob, true);
	}
	
	
	public void init() throws ServletException {	
		// live ticker job (every 20 min)
		if (enabled.get(BatchJobs.tickerJob)) {		
			Thread tickerJob;
			try {
				tickerJob = new Thread(new TickerJob());
				tickerJob.start();
				batchJobThreads.add(tickerJob);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init TickerJob()");
				System.exit(1);
			}
		}
		
		// portfolio value job (every 20 min)
		if (enabled.get(BatchJobs.portfolioJob)) {		
			Thread portfolioJob;
			try {
				portfolioJob = new Thread(new PortfolioJob());
				portfolioJob.start();
				batchJobThreads.add(portfolioJob);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init PortfolioJob()");
				System.exit(1);
			}
		}
		
		
		// ticker history (5 am daily)
		if (enabled.get(BatchJobs.tickerHistoryJob)) {	
			Thread tickerHistoryJob;
			try {
				tickerHistoryJob = new Thread(new TickerHistoryJob());
				tickerHistoryJob.start();
				batchJobThreads.add(tickerHistoryJob);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init TickerHistoryJob()");
				System.exit(1);
			}
		}
			
		// top movers (4 am daily)
		if (enabled.get(BatchJobs.topMoversJob)) {	
			Thread topMoversJob;
			try {
				topMoversJob = new Thread(new TopMoversJob());
				topMoversJob.start();
				batchJobThreads.add(topMoversJob);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init TopMoversJob()");
				System.exit(1);
			}
		}
		
		// get news (every 15 min)
		if (enabled.get(BatchJobs.newsJob)) {	
			Thread newsJob;
			try {
				newsJob = new Thread(new NewsJob());
				newsJob.start();
				batchJobThreads.add(newsJob);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init NewsJob()");
				System.exit(1);
			}				
		}
		
		// get stats (4 am daily)
		if (enabled.get(BatchJobs.statsJob)) {	
			Thread statsJob;
			try {
				statsJob = new Thread(new StatsJob());
				statsJob.start();
				batchJobThreads.add(statsJob);
			} catch (Exception e) {
				log.error(Util.stackTraceToString(e));
				log.error("UNRECOVERABLE ERROR: Could not init NewsJob()");
				System.exit(1);
			}			
			
		}
		log.info("----------");
		log.info("----------  Initialized successfully ----------");
		log.info("----------");
	}
}
