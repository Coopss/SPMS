package com.spms.tops;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;

public class TopMoversJob implements Runnable {
    private static final Logger log = LogManager.getLogger(TopMoversJob.class);
    private static final Long timeout = (long) 1200000; //timeout
    
    @Override
    public void run() {
    	log.info("TopMoversJob started " + this);
        while (true) {
        	timeout();
            TopMoversController controller;
            try {
                controller = new TopMoversController();
                log.info("Started " + controller);
                controller.reload();
                log.info("Finished " + controller);
                controller = null;
                
            } catch (Exception e) {
                log.error("Failed to reload top movers data -- Critical");
                log.error(Util.stackTraceToString(e));
            }    
        }
        
    }
   
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
}