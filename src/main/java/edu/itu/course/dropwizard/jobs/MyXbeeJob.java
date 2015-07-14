package edu.itu.course.dropwizard.jobs;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.XBeeException;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.annotations.SimpleTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;

import edu.itu.course.XbeeEnum;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.api.beans.XbeeUtil;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;

@CronTrigger(cron = "0/5 * * * * ?")
public class MyXbeeJob extends Job {

	private final Logger logger = LoggerFactory.getLogger(MyXbeeJob.class);

	@Override
	public void doRun() throws JobInterruptException {
		XbeeUtil xbee = XbeeUtil.getInstance();

		//using future to test
		XbeeUtil xbeeUtil;
		Future<String> future = null;
		final ExecutorService executor;
		Callable<String> asyncTask;
		
		String futureResult = "reading error";
		
		DeviceResourceImpl deviceResourceImpl;// = (DeviceResourceImpl) SundialJobScheduler.getServletContext().getAttribute("MyKey");

		// using future to control the led
		try {
			xbeeUtil = XbeeUtil.getInstance();
			executor = (ExecutorService) SundialJobScheduler.getServletContext().getAttribute("ExecutorService");
			deviceResourceImpl = (DeviceResourceImpl) SundialJobScheduler.getServletContext().getAttribute("MyKey");

			xbeeUtil.open();
			
			asyncTask = new Callable<String>() {
				@Override
				public String call() throws Exception {
					
					
					logger.debug("coming inside the reading");
					// sending command reading temp data
					xbee.sendXbeeData(XbeeEnum.READING.getValue());

					// waiting for answer
					String receivedString;
					if ((receivedString = xbee.receiveXbeeData()) != null) {

						if (receivedString.equals(XbeeEnum.ERROR_RESPONSE.getValue())) {
							logger.error("error sendXbeeData(xbee,XbeeEnum.READING.getValue());");
						}
						logger.info("---> received Data is :" + receivedString);

						if (receivedString.equals(XbeeEnum.READING_DONE.getValue())) {
							logger.info("command reading data succuess!!");
						}

						//
						DeviceData currentDeviceData = new DeviceData(1, Float.parseFloat(receivedString), new Date());
						deviceResourceImpl.insertDeviceDataByDeviceId(currentDeviceData);

						logger.info("cron MyXbeeJob insert data is " + currentDeviceData);

					}else
					{
						logger.info("can't receive reading data from Xbee \n"
								+ "  Please check connection between server Xbee and ending Xbee!!");
						return "Failed";
					}
					
					return "Success";
					
				}
			};
			future = executor.submit(asyncTask);

			//can using timeout parameters
			//waiting for 5 seconds
			futureResult = future.get(5000, TimeUnit.SECONDS);
			
			logger.info("future reading xbee result is%s"  + futureResult);
//			executor.notify();
			//if the future fails will catch here
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.info("future reading  xbee result is%s"  + futureResult+"\n error message is --"+e.getMessage());
			future.cancel(true);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			logger.info("future reading timeout result is %s"  + futureResult+"\n error message is --"+e.getMessage());
			e.printStackTrace();
		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("XBeeException here"+ e.getMessage());
		}

		
		
		//using normal function works but conflict with future task ....
		/* try {
			//will check xbee is open or not
			xbee.open();

			try {
				// sending command reading temp data
				xbee.sendXbeeData(XbeeEnum.READING.getValue());

				// waiting for answer
				String receivedString;
				if ((receivedString = xbee.receiveXbeeData()) != null) {

					if (receivedString.equals(XbeeEnum.ERROR_RESPONSE.getValue())) {
						logger.error("error sendXbeeData(xbee,XbeeEnum.READING.getValue());");
					}
					logger.info("---> received Data is :" + receivedString);

					if (receivedString.equals(XbeeEnum.READING_DONE.getValue())) {
						logger.info("command reading data succuess!!");
					}

					//
					DeviceResourceImpl deviceResourceImpl = (DeviceResourceImpl) SundialJobScheduler.getServletContext().getAttribute("MyKey");
					DeviceData currentDeviceData = new DeviceData(1, Float.parseFloat(receivedString), new Date());
					deviceResourceImpl.insertDeviceDataByDeviceId(currentDeviceData);

					logger.info("cron MyXbeeJob insert data is " + currentDeviceData);

				}else
				{
					logger.info("can't receive reading data from Xbee \n"
							+ "  Please check connection between server Xbee and ending Xbee!!");
				}
				

			} catch (Exception e) {
				logger.error(e.toString());
			}

		} catch (XBeeException e1) {
			// TODO Auto-generated catch block
			System.out.println("coming XBeeException=========================");

			e1.printStackTrace();
			logger.error(e1.toString());
		} /*finally {
			System.out.println("coming XBeeException= finally========================");
			xbee.close();
		}*/
	}

}