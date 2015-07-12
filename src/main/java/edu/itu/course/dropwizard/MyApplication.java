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

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.dropwizard.sundial.SundialBundle;
import com.xeiam.dropwizard.sundial.SundialConfiguration;

import de.spinscale.dropwizard.jobs.JobsBundle;
import edu.itu.course.dropwizard.health.DatabaseHealthCheck;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDAO;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDataDAO;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;
import edu.itu.course.dropwizard.resources.ManagedPeriodicTask;
import edu.itu.course.dropwizard.resources.TestTask;

/**
 * User: Jack Li
 * Date: 7/7/15
 */
public class MyApplication extends Application<MyApplicationConfiguration> {
	
//  private static Logger logger = Logger.getLogger(MyApplication.class.getName());

	private static Logger logger = LoggerFactory.getLogger(MyApplication.class);
	
		public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }

    @Override
    public String getName() {
        return "My Application";
    }


    @Override
    public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap) {
    	
	/*  GuiceBundle<MyApplicationConfiguration> guiceBundle = GuiceBundle.<MyApplicationConfiguration>newBuilder()
			    .addModule( new GuiceModule() )
			    .setConfigClass( MyApplicationConfiguration.class )
//			    .enableAutoConfig(getClass().getPackage().getName())
			    .build();
	  bootstrap.addBundle( guiceBundle );
	
	  GuiceJobsBundle guiceJobsBundle = new GuiceJobsBundle(
			  "edu.itu.course.dropwizard.jobs",
	    guiceBundle.getInjector() );
	  bootstrap.addBundle( guiceJobsBundle );*/
      bootstrap.addBundle(new SundialBundle<MyApplicationConfiguration>() {

    	      @Override
    	      public SundialConfiguration getSundialConfiguration(MyApplicationConfiguration configuration) {
    	        return configuration.getSundialConfiguration();
    	      }
    	    });
//      bootstrap.addBundle(new JobsBundle( "edu.itu.course.dropwizard.jobs"));
    }

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
        DeviceResourceImpl deviceResourceImpl =  new DeviceResourceImpl(dao,dataDAO);
        //add health check
        
        // Add object to ServletContext for accessing from Sundial Jobs
        environment.getApplicationContext().setAttribute("MyKey", deviceResourceImpl);
        
        DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(jdbi, dataSourceFactory);
        
        environment.healthChecks().register("DatabaseHealthCheck", healthCheck);
        
     
        
        environment.jersey().register(deviceResourceImpl);
        
//        environment.admin().addTask(new TestTask("Testtask",new DeviceResourceImpl(dao,dataDAO)));
        
//        environment.lifecycle().
/*        final Managed managedImplementer = new ManagedPeriodicTask(new TestTask(new DeviceResourceImpl(dao,dataDAO)));
        environment.lifecycle().manage(managedImplementer);*/
       

    }

}
