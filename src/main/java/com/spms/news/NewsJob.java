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
	
	NewsJob() { 

	}
	
    /**
     * Get time until next 4 am 
     */
    public Long getTimeout() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) >= 4) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY,4);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        Date nextRuntime = cal.getTime();
        Date now = new Date();
        
        return nextRuntime.getTime() - now.getTime();
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
        while (true) {
            try {
                timeout();
                
                log.info("Started " + this);
                NewsAggregator controller;
                controller = new NewsAggregator();
                controller.reload();
                controller = null;
                
            } catch (Exception e) {
                log.error("Failed to reload news data -- Critical");
                log.error(Util.stackTraceToString(e));
            }    
        }
	}
}
