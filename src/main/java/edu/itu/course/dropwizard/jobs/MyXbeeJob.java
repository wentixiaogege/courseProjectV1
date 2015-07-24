package edu.itu.course.dropwizard.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.annotations.SimpleTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;

import edu.itu.course.XbeeEnum;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.api.beans.XbeeUtil;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;

@CronTrigger(cron = "0/10 * * * * ?")
public class MyXbeeJob extends Job {

	private final Logger logger = LoggerFactory.getLogger(MyXbeeJob.class);

	@Override
	public void doRun() throws JobInterruptException {
		XbeeUtil xbee = XbeeUtil.getInstance();
		/*
		 * //using future to test XbeeUtil xbeeUtil; Future<String> future =
		 * null; final ExecutorService executor; Callable<String> asyncTask;
		 * DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * String futureResult = "reading error";
		 * 
		 * DeviceResourceImpl deviceResourceImpl;// = (DeviceResourceImpl)
		 * SundialJobScheduler.getServletContext().getAttribute("MyKey");
		 * 
		 * // using future to control the led try { xbeeUtil =
		 * XbeeUtil.getInstance(); executor = (ExecutorService)
		 * SundialJobScheduler
		 * .getServletContext().getAttribute("ExecutorService");
		 * deviceResourceImpl = (DeviceResourceImpl)
		 * SundialJobScheduler.getServletContext().getAttribute("MyKey");
		 * 
		 * xbeeUtil.open();
		 * 
		 * asyncTask = new Callable<String>() {
		 * 
		 * @Override public String call() throws Exception {
		 * 
		 * 
		 * logger.debug("future coming inside the reading"); // sending command
		 * reading temp data xbee.sendXbeeData(XbeeEnum.READING.getValue());
		 * 
		 * // waiting for answer String receivedString; String[] receiveArray;
		 * if ((receivedString = xbee.receiveXbeeData()) != null) {
		 * 
		 * if (receivedString.equals(XbeeEnum.ERROR_RESPONSE.getValue())) {
		 * logger
		 * .error("error sendXbeeData(xbee,XbeeEnum.READING.getValue());");
		 * 
		 * return "Failed:receive data is error"; }
		 * logger.info("future ---> received Data is :" + receivedString);
		 * 
		 * // receiveArray = receivedString.split(",");
		 * 
		 * //changed to device time and using device id and device name
		 * DeviceData currentDeviceData = new
		 * DeviceData(Integer.parseInt(receiveArray[0]),
		 * Float.parseFloat(receiveArray[2]),
		 * dateFormat.parse(receiveArray[3]));
		 * deviceResourceImpl.insertDeviceDataByDeviceId(currentDeviceData);
		 * 
		 * logger.info("future cron MyXbeeJob insert data is " +
		 * currentDeviceData);
		 * 
		 * }else { logger.info("can't receive reading data from Xbee \n" +
		 * "  Please check connection between server Xbee and ending Xbee!!");
		 * return "Failed"; }
		 * 
		 * return "Success";
		 * 
		 * } }; future = executor.submit(asyncTask);
		 * 
		 * //can using timeout parameters //waiting for 5 seconds futureResult =
		 * future.get(4000, TimeUnit.SECONDS);
		 * 
		 * logger.info("future reading xbee result is%s" + futureResult); //
		 * executor.notify(); //if the future fails will catch here } catch
		 * (InterruptedException | ExecutionException e) { // TODO
		 * Auto-generated catch block // e.printStackTrace();
		 * logger.info("future reading  xbee result is%s" +
		 * futureResult+"\n error message is --"+e.getMessage());
		 * future.cancel(true); } catch (TimeoutException e) { // TODO
		 * Auto-generated catch block
		 * logger.info("future reading timeout result is %s" +
		 * futureResult+"\n error message is --"+e.getMessage());
		 * e.printStackTrace(); } catch (XBeeException e) { // TODO
		 * Auto-generated catch block e.printStackTrace();
		 * logger.info("XBeeException here"+ e.getMessage()); }
		 */

		// using normal function works but conflict with future task ....
		try {
			// will check xbee is open or not
			xbee.open();
			DeviceResourceImpl deviceResourceImpl = (DeviceResourceImpl) SundialJobScheduler
					.getServletContext().getAttribute("MyKey");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// sending command reading temp data
			xbee.sendXbeeData(XbeeEnum.READING.getValue());

			// waiting for answer
			String receivedString = xbee.receiveXbeeData(9000);
			String[] receiveArray;
			// can set the timeout here for better using 
			if (null != receivedString ) {

				if (receivedString.equals(XbeeEnum.ERROR_RESPONSE.getValue())) {
					logger.error("error sendXbeeData(xbee,XbeeEnum.READING.getValue());");
				}
				logger.info(" ---> received Data is :" + receivedString);

				//
				receiveArray = receivedString.split(",");

				// changed to device time and using device id and device name
				DeviceData currentDeviceData = new DeviceData(
						Integer.parseInt(receiveArray[0]),
						Float.parseFloat(receiveArray[2]),
						dateFormat.parse(receiveArray[3]));
				deviceResourceImpl
						.insertDeviceDataByDeviceId(currentDeviceData);

				logger.info(" cron MyXbeeJob insert data is "
						+ currentDeviceData);

			} else {
				logger.info("can't receive reading data from Xbee!!!!!!!!!!!1!!!!!!!!! \n"
						+ "  Please check connection between server Xbee and ending Xbee!!");
			}

		}
		catch (XBeeTimeoutException e1) {
			
			
			logger.error("-----XBeeTimeoutException----"+e1.toString());
			
			logger.info("-----try to reopen ----"+e1.toString());
			xbee.reopen();
		}
		catch (XBeeException e1) {
			// TODO Auto-generated catch block
			 if (e1.getCause().getMessage().contains("Input/output error in nativeavailable")) {
	                logger.info("connection error reconnect next time");
	                xbee.close();
	            } 
			e1.printStackTrace();
			logger.error("-----XBeeException----"+e1.toString());
		}
		catch (Exception e) {
			logger.error(e.toString());
			 if (e.getCause().getMessage().contains("Input/output error in nativeavailable")) {
	                logger.info("connection error reconnect next time");
	                xbee.close();
	            } 
		} /*
		 * finally { System.out.println(
		 * "coming XBeeException= finally========================");
		 * xbee.close(); }
		 */
	}

}