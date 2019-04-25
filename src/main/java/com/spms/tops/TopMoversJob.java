package com.spms.tops;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.spms.Util;
import com.spms.news.TickerNewsDAO;
import com.spms.ticker.los.Symbol;
import com.spms.ticker.los.SymbolDAO;
import com.spms.ticker.tools.Requests;

public class TopMoversJob implements Runnable {
    private static final Logger log = LogManager.getLogger(TopMoversJob.class);
    
    @Override
    public void run() {
    	log.info("TopMoversJob started " + this);
        while (true) {
            timeout();
            
            log.info("Started " + this);
            TopMoversController controller;
            try {
                controller = new TopMoversController();
                controller.reload();
                controller = null;
                
            } catch (Exception e) {
                log.error("Failed to reload top movers data -- Critical");
                log.error(Util.stackTraceToString(e));
            }    
        }
        
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
}