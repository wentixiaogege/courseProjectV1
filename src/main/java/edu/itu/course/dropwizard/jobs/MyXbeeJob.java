package edu.itu.course.dropwizard.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;

import edu.itu.course.XbeeEnum;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.api.beans.XbeeUtil;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class MyXbeeJob.
 */
//for 5 seconds
@CronTrigger(cron = "0/5 * * * * ?")
public class MyXbeeJob extends Job {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(MyXbeeJob.class);

	/* (non Javadoc) 
	* <p>Title: doRun</p> 
	* <p>Description: </p> 
	* @throws JobInterruptException 
	* @see com.xeiam.sundial.Job#doRun() 
	*/ 
	@Override
	public void doRun() throws JobInterruptException {
		XbeeUtil xbee = XbeeUtil.getInstance();

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