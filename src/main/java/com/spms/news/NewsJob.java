package com.spms.news;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.live.TickerDAO;
import com.spms.tops.TopMoversController;

public class NewsJob implements Runnable {
	private static final Logger log = LogManager.getLogger(NewsJob.class);
	private static final Long timeout = (long) 900000; // 15 min
	
	public NewsJob() {}
	
    public Long getTimeout() {        
        return timeout;
    }
    
    public void timeout() {
        try {
            Thread.sleep(getTimeout());
        } catch (InterruptedException e) {
            log.error("Failed to sleep.");
            log.error(Util.stackTraceToString(e));
        }
    }
	
	@Override
	public void run() {
		NewsController controller;
		log.info("NewsJob started " + this);
        while (true) {
            try {
                timeout();
                controller = new NewsController();
                log.info("Started " + controller);
                controller.reload();
                log.info("Finished " + controller);
                controller = null;
                
            } catch (Exception e) {
                log.error("Failed to reload news data -- Critical");
                log.error(Util.stackTraceToString(e));
            }    
        }
	}
}
