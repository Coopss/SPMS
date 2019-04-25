package com.spms.ticker.stats;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;
import com.spms.tops.TopMoversController;
import com.spms.tops.TopMoversJob;

public class StatsJob implements Runnable {
    private static final Logger log = LogManager.getLogger(StatsJob.class);
    
    @Override
    public void run() {
    	log.info("StatsJob started " + this);
        while (true) {
            timeout();
            
            log.info("Started " + this);
            StatsController controller;
            try {
                controller = new StatsController();
                controller.reload();
                controller = null;
                
            } catch (Exception e) {
                log.error("Failed to reload stats data -- Critical");
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
