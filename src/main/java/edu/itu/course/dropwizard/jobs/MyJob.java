package edu.itu.course.dropwizard.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;

import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;

@CronTrigger(cron = "0/5 * * * * ?")
public class MyJob extends Job {

  private final Logger logger = LoggerFactory.getLogger(MyJob.class);
  static  int   deviceId =33;
  @Override
  public void doRun() throws JobInterruptException {

    // pull object from ServletContext, which was added in the pllication's run method
//    String myObject = (String) SundialJobScheduler.getServletContext().getAttribute("MyKey");

	  DeviceResourceImpl  deviceResourceImpl = (DeviceResourceImpl)SundialJobScheduler.getServletContext().getAttribute("MyKey");
	  
	  Device sampleDevice = new Device(deviceId, "test Device", 0, "lightData");
	 
	  deviceResourceImpl.addDevice(sampleDevice);
	  
	  System.out.println("cron job"+ deviceResourceImpl.getDeviceById(deviceId));
    
	  deviceId++;
    
    
  }
}
