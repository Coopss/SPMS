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
	
	public NewsJob() { 

	}
	
    /**
     * Get time until next 4 am 
     */
    public Long getTimeout() {        
        return new Long(900000);
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
		log.info("NewsJob started " + this);
        while (true) {
            try {
                timeout();
                
                log.info("Started " + this);
                NewsController controller;
                controller = new NewsController();
                controller.reload();
                controller = null;
                
            } catch (Exception e) {
                log.error("Failed to reload news data -- Critical");
                log.error(Util.stackTraceToString(e));
            }    
        }
	}
}
