package com.n26.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener{

	private ScheduledExecutorService scheduler;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		scheduler.shutdown();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		scheduler = Executors.newSingleThreadScheduledExecutor();
		   // scheduler.scheduleAtFixedRate(new DailyJob(), 0, 1, TimeUnit.DAYS);
		scheduler.scheduleAtFixedRate(new PartitionMaintainJob(), 0, 1, TimeUnit.SECONDS);
		
		
		
		
		
	}

}
