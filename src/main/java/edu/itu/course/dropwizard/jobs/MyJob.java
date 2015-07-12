package edu.itu.course.dropwizard.jobs;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.wpan.RxResponse16;
import com.rapplogic.xbee.api.wpan.TxRequest16;
import com.rapplogic.xbee.api.wpan.TxStatusResponse;
import com.rapplogic.xbee.util.ByteUtils;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;

import edu.itu.course.PropertyReading;
import edu.itu.course.XbeeEnum;
import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.resources.DeviceResourceImpl;

@CronTrigger(cron = "0/5 * * * * ?")
public class MyJob extends Job {

	private final Logger logger = LoggerFactory.getLogger(MyJob.class);

	final XBee xbee = new XBee();
	PropertyReading propertyReading = new PropertyReading();

	@Override
	public void doRun() throws JobInterruptException {

		try {

			logger.info("xbee opening---------");
			xbee.open(propertyReading.getXbeeDevice(), Integer.parseInt(propertyReading.getXbeeBaud()));
			// xbee.open("/dev/ttyUSB0",9600);
			logger.info("xbee opened---------");

			try {
				// sending command reading temp data
				sendXbeeData(xbee, XbeeEnum.READING.getValue());

				// waiting for answer
				String receivedString;
				if ((receivedString = receiveXbeeData(xbee)) != null) {

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
					
					System.out.println("cron job insert data is " + currentDeviceData);

				}

			} catch (Exception e) {
				logger.error(e.toString());
			}

		} catch (XBeeException e1) {
			// TODO Auto-generated catch block
			System.out.println("coming XBeeException=========================");

			e1.printStackTrace();
			logger.error(e1.toString());
		} finally {
			System.out.println("coming XBeeException= finally========================");
			if (xbee != null && xbee.isConnected()) {
				xbee.close();
			}
		}
	}

	public String receiveXbeeData(XBee xbee) throws XBeeException {

		try {
			// forever waiting here
			XBeeResponse response = xbee.getResponse();

			logger.debug("received response " + response.toString());

			if (response.getApiId() == ApiId.RX_16_RESPONSE) {
				// we received a packet from ZNetSenderTest.java
				RxResponse16 rx = (RxResponse16) response;

				logger.debug("Received RX packet, options is" + rx.getOptions() + ", sender address is " + rx.getRemoteAddress() + ", data is " + ByteUtils.toString(rx.getData()));

				return ByteUtils.toString(rx.getData());
			}
		} catch (XBeeTimeoutException timeout) {

			logger.info("server timeout" + timeout.getMessage());
			throw new XBeeTimeoutException();
		} catch (XBeeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error(e1.toString());
			throw new XBeeException(e1);
		}
		return null;

	}

	public void sendXbeeData(XBee xbee, String data) {
		// should add into the properties file
		PropertyReading propertyReading = new PropertyReading();

		int msb = DatatypeConverter.parseHexBinary(propertyReading.getServerBroadcastAddress())[0];
		int lsb = DatatypeConverter.parseHexBinary(propertyReading.getServerBroadcastAddress())[1];

		XBeeAddress16 address16 = new XBeeAddress16(msb, lsb);

		final int[] payload = data.chars().toArray();
		// final int[] payload = data.toCharArray();

		TxRequest16 request = new TxRequest16(address16, payload);

		logger.debug("sending tx packet: " + request.toString());

		try {
			TxStatusResponse response = (TxStatusResponse) xbee.sendSynchronous(request, 10000);

			request.setFrameId(xbee.getNextFrameId());

			logger.debug("received response " + response);

			if (response.isSuccess()) {
				logger.info("response is Success" + response.getStatus());
			} else {
				logger.error("response is Error" + response.getStatus());
			}
		} catch (XBeeTimeoutException e) {
			logger.warn("request timed out");
		} catch (XBeeException e) {
			e.printStackTrace();
		}

	}
}