/*
 * Copyright 2015 Jingjie Li
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package edu.itu.course.dropwizard;

import java.util.concurrent.ExecutorService;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.dropwizard.sundial.SundialBundle;
import com.xeiam.dropwizard.sundial.SundialConfiguration;
import com.xeiam.sundial.SundialJobScheduler;

import edu.itu.course.dropwizard.health.DatabaseHealthCheck;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDAO;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDataDAO;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;

// TODO: Auto-generated Javadoc
/**
 * User: Jack Li
 * Date: 7/7/15.
 */
public class MyApplication extends Application<MyApplicationConfiguration> {
	
//  private static Logger logger = Logger.getLogger(MyApplication.class.getName());

	/** The logger. */
private static Logger logger = LoggerFactory.getLogger(MyApplication.class);
	
		/**
		 * The main method.
		 *
		 * @param args the arguments
		 * @throws Exception the exception
		 */
		public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }

    /* (non Javadoc) 
    * <p>Title: getName</p> 
    * <p>Description: </p> 
    * @return 
    * @see io.dropwizard.Application#getName() 
    */ 
    @Override
    public String getName() {
        return "My Application";
    }


    /* (non Javadoc) 
    * <p>Title: initialize</p> 
    * <p>Description: </p> 
    * @param bootstrap 
    * @see io.dropwizard.Application#initialize(io.dropwizard.setup.Bootstrap) 
    */ 
    @Override
    public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap) {
    	
      //adding the front page assets 
      //need add the assets depedency
      bootstrap.addBundle(new AssetsBundle("/webapp/", "/"));
    	
      bootstrap.addBundle(new SundialBundle<MyApplicationConfiguration>() {

    	      @Override
    	      public SundialConfiguration getSundialConfiguration(MyApplicationConfiguration configuration) {
    	        return configuration.getSundialConfiguration();
    	      }
    	    });
    }

    /* (non Javadoc) 
    * <p>Title: run</p> 
    * <p>Description: </p> 
    * @param configuration
    * @param environment
    * @throws ClassNotFoundException 
    * @see io.dropwizard.Application#run(io.dropwizard.Configuration, io.dropwizard.setup.Environment) 
    */ 
    public void run(MyApplicationConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {

        // create database connection using JDBI
        final DBIFactory factory = new DBIFactory();
        final DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
//        final DBI jdbi = factory.build(environment, dataSourceFactory, "derby");
        final DBI jdbi = factory.build(environment, dataSourceFactory, "mysql");

        // add resources
        final DeviceDAO dao = jdbi.onDemand(DeviceDAO.class);
        final DeviceDataDAO dataDAO = jdbi.onDemand(DeviceDataDAO.class);
        try {
            dao.createDeviceTable();
            logger.info("Device table created");
            dataDAO.createDeviceDataTable();
            logger.info("DeviceData table created");
        } catch (Exception e) {
            // probably the table already exists. Don't worry about it.
            if (e.getCause().getMessage().contains("already exists")) {
                logger.info("table already exists.");
            } else {
                logger.error("Device DB was not created", e);
            }

        }
        
        final ExecutorService executorService = environment.lifecycle().executorService("executorService").minThreads(4).maxThreads(10).build();
        
        environment.getApplicationContext().setAttribute("ExecutorService", executorService);
    
        
        DeviceResourceImpl deviceResourceImpl =  new DeviceResourceImpl(dao,dataDAO);
        //add health check
        
        // Add object to ServletContext for accessing from Sundial Jobs
        environment.getApplicationContext().setAttribute("MyKey", deviceResourceImpl);
        
        DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(jdbi, dataSourceFactory);
        
        environment.healthChecks().register("DatabaseHealthCheck", healthCheck);
        
        environment.jersey().register(deviceResourceImpl);
     
        
        
        //add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                  System.out.println("shutdown Hook called");
                  //database.close();
                  final ExecutorService executor = (ExecutorService) SundialJobScheduler.getServletContext().getAttribute("ExecutorService");
            	  executor.shutdown();
            }
          });

    }

}
