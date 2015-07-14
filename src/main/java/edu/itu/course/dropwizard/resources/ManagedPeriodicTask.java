package edu.itu.course.dropwizard.resources;

import io.dropwizard.lifecycle.Managed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractScheduledService;

public class ManagedPeriodicTask implements Managed {

    private final Logger LOGGER = LoggerFactory.getLogger(ManagedPeriodicTask.class);
    private final AbstractScheduledService periodicTask;

    
    public ManagedPeriodicTask(AbstractScheduledService periodicTask) {
        this.periodicTask = periodicTask;
    }

    @Override
    public void start() throws Exception {
    	
        periodicTask.startAsync().awaitRunning();
        
    }

    @Override
    public void stop() throws Exception {
        periodicTask.stopAsync().awaitTerminated();
    }
}