package edu.itu.course.dropwizard.api.beans;

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

import edu.itu.course.PropertyReading;

// TODO: Auto-generated Javadoc
/**
 * The Class XbeeUtil.
 */
public class XbeeUtil {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(XbeeUtil.class);
	
	/** The xbee. */
	XBee xbee;
	
	/** The property reading. */
	PropertyReading propertyReading;

	/** The instance. */
	private static XbeeUtil instance = null;

	/**
	 * Instantiates a new xbee util.
	 */
	protected XbeeUtil() {
		// Exists only to defeat instantiation.
		xbee = new XBee();
		propertyReading = new PropertyReading();
	}

	/**
	 * Gets the single instance of XbeeUtil.
	 *
	 * @return single instance of XbeeUtil
	 */
	public static XbeeUtil getInstance() {
		if (instance == null) {
			instance = new XbeeUtil();
		}
		return instance;
	}

	/**
	 * Open.
	 *
	 * @return true, if successful
	 * @throws XBeeException the x bee exception
	 */
	public boolean open() throws XBeeException {
		try {
			if (xbee != null && xbee.isConnected()) {
				logger.info("xbee is already opened---------");
				return true;
			}
			logger.info("xbee opening---------");
			xbee.open(propertyReading.getXbeeDevice(), Integer.parseInt(propertyReading.getXbeeBaud()));

			xbee.clearResponseQueue();
			return true;
		} catch (NumberFormatException | XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new XBeeException();
		}
	}
	
	/**
	 * Reopen.
	 *
	 * @return true, if successful
	 */
	public boolean reopen(){
		try {
			if (xbee != null && xbee.isConnected()) {
				logger.info("xbee is shutting down now ---------");
				xbee.close();
				
				logger.info("xbee is  opening---------");
				xbee.open(propertyReading.getXbeeDevice(), Integer.parseInt(propertyReading.getXbeeBaud()));

				return true;
			}
			logger.info("xbee opening---------");
			xbee.open(propertyReading.getXbeeDevice(), Integer.parseInt(propertyReading.getXbeeBaud()));

			xbee.clearResponseQueue();
			return true;
		} catch (NumberFormatException | XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Close.
	 */
	public void close() {
		if (xbee != null && xbee.isConnected()) {
			xbee.close();
		}
	}

	/**
	 * Receive xbee data.
	 *
	 * @param timeout the timeout
	 * @return the string
	 * @throws XBeeException the x bee exception
	 */
	public String receiveXbeeData(int timeout) throws XBeeException {

		try {
			// forever waiting here testing version
			// waiting most 10 seconds 
			
			
			XBeeResponse response = xbee.getResponse(timeout);

			logger.info("received response " + response.toString());

			if (response.getApiId() == ApiId.RX_16_RESPONSE) {
				// we received a packet from ZNetSenderTest.java
				RxResponse16 rx = (RxResponse16) response;

				logger.debug("Received RX packet, options is" + rx.getOptions() + ", sender address is " + rx.getRemoteAddress() + ", data is " + ByteUtils.toString(rx.getData()));
				
				return ByteUtils.toString(rx.getData());
			}
			
		} catch (XBeeTimeoutException t) {

			logger.info("xbee receiver timeout" + t.getMessage());
			return null;
		} catch (XBeeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error(e1.toString());
			return null;
		}
		return null;
	}

	/**
	 * Send xbee data.
	 *
	 * @param data the data
	 * @return the string
	 */
	public String sendXbeeData(String data) {
		// should add into the properties file
		PropertyReading propertyReading = new PropertyReading();

		int msb = DatatypeConverter.parseHexBinary(propertyReading.getServerBroadcastAddress())[0];
		int lsb = DatatypeConverter.parseHexBinary(propertyReading.getServerBroadcastAddress())[1];

		XBeeAddress16 address16 = new XBeeAddress16(msb, lsb);

		final int[] payload = data.chars().toArray();
		// final int[] payload = data.toCharArray();

		TxRequest16 request = new TxRequest16(address16, payload);

		logger.debug("sending tx packet: " + payload.toString());

		try {
			TxStatusResponse response = (TxStatusResponse) xbee.sendSynchronous(request, 10000);

			request.setFrameId(xbee.getNextFrameId());

			logger.debug("received response " + response);

			if (response.isSuccess()) {
				logger.info("response is " + response.getStatus());
				xbee.clearResponseQueue();
			} else {
				logger.error(" error response is " + response.getStatus());
			}
			logger.info("in send Xbee data queue size is "+xbee.getResponseQueueSize());

			return response.getStatus().toString();
		} catch (XBeeTimeoutException e) {
			logger.warn("request timed out");
		} catch (XBeeException e) {
			e.printStackTrace();
		}
		return null;

	}
}
