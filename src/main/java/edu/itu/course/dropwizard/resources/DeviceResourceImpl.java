package edu.itu.course.dropwizard.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.XBeeException;
import com.xeiam.sundial.SundialJobScheduler;

import edu.itu.course.XbeeEnum;
import edu.itu.course.dropwizard.api.DeviceResource;
import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.api.beans.QueryDeviceData;
import edu.itu.course.dropwizard.api.beans.XbeeUtil;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDAO;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDataDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceResourceImpl.
 */
public class DeviceResourceImpl implements DeviceResource {

	/** The device dao. */
	private final DeviceDAO deviceDAO;
	
	/** The device data dao. */
	private final DeviceDataDAO deviceDataDAO;

	/** The logger. */
	private static Logger logger = LoggerFactory
			.getLogger(DeviceResourceImpl.class);

	/**
	 * Instantiates a new device resource impl.
	 *
	 * @param deviceDAO the device dao
	 * @param deviceDataDAO the device data dao
	 */
	public DeviceResourceImpl(DeviceDAO deviceDAO, DeviceDataDAO deviceDataDAO) {
		super();
		this.deviceDAO = deviceDAO;
		this.deviceDataDAO = deviceDataDAO;
	}

	/* (non Javadoc) 
	* <p>Title: addDevice</p> 
	* <p>Description: </p> 
	* @param device 
	* @see edu.itu.course.dropwizard.api.DeviceResource#addDevice(edu.itu.course.dropwizard.api.beans.Device) 
	*/ 
	public void addDevice(Device device) {
		// TODO Auto-generated method stub
		this.deviceDAO.insert(device);
	}
	
	/* (non Javadoc) 
	* <p>Title: getDevices</p> 
	* <p>Description: </p> 
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#getDevices() 
	*/ 
	@Override
	public Response getDevices() {
		// TODO Auto-generated method stub
		System.out.println("-------------==============="+this.deviceDAO.findDevices());
		return Response.status(200).entity(this.deviceDAO.findDevices()).build();
	}
	
	/* (non Javadoc) 
	* <p>Title: getDeviceById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#getDeviceById(int) 
	*/ 
	public Device getDeviceById(int deviceId) {
		// TODO Auto-generated method stub
		return this.deviceDAO.findDeviceById(deviceId);
	}

	/* (non Javadoc) 
	* <p>Title: updateDeviceById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#updateDeviceById(int) 
	*/ 
	public Device updateDeviceById(int deviceId) {
		// TODO Auto-generated method stub
		return this.deviceDAO.updateDevicebyId(deviceId);
	}

	/* (non Javadoc) 
	* <p>Title: removeDevice</p> 
	* <p>Description: </p> 
	* @param deviceId 
	* @see edu.itu.course.dropwizard.api.DeviceResource#removeDevice(int) 
	*/ 
	public void removeDevice(int deviceId) {
		// TODO Auto-generated method stub
		this.deviceDAO.removeDevice(deviceId);
	}

	/* (non Javadoc) 
	* <p>Title: getDeviceDataById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#getDeviceDataById(int) 
	*/ 
	public Response getDeviceDataById(int deviceId) {
		// TODO Auto-generated method stub

		List<DeviceData> list = this.deviceDataDAO
				.getDeviceDataByDeviceId(deviceId);
		return Response.status(200).entity(list).build();
	}

	/**
	 * Gets the date from string.
	 *
	 * @param dateString the date string
	 * @return the date from string
	 */
	private Date getDateFromString(String dateString) {
		
		
		DateFormat df = new SimpleDateFormat(DateUtils.FOMAT_DATE);
		Date date;
		try {

			date = df.parse(String.valueOf(dateString));

			return date;
		} catch (ParseException e) {
			// WebApplicationException
			// ...("Date format should be yyyy-MM-dd'T'HH:mm:ss",
			// Status.BAD_REQUEST);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the avg.
	 *
	 * @param list the list
	 * @param intervals the intervals
	 * @return the avg
	 */
	private List<DeviceData> getAvg(List<DeviceData> list, int intervals) {

		List<DeviceData> newdevicedatalist = new ArrayList<DeviceData>();

		int intervalnum = 0;
		float avgDouble = 0.00f;
		if (intervals > 0 && list.size() > 0) {
			// for every device
			
			long iterateDateTime = DateUtils.toUnixTime(list.get(0)
					.getTimestamp()) + intervals;
			logger.debug("first iterationTime is "+iterateDateTime);
			avgDouble = 0.0f;
			intervalnum = 0;
			for (int j = 0; j < list.size(); j++) {

				if (DateUtils.toUnixTime(list.get(j).getTimestamp()) < iterateDateTime) {
					
					intervalnum++;
					avgDouble += list.get(j).getData();
					logger.debug("next data is "+DateUtils.toUnixTime(list.get(j).getTimestamp()));
				} else if (intervalnum > 0)
				{
					logger.debug("next iterationTime is "+iterateDateTime+"j is"+j);

					avgDouble /= intervalnum;
					DeviceData tmpData = new DeviceData(list.get(j - 1).getId(),
														list.get(j - 1).getDeviceId(), avgDouble, list.get(
														j - 1).getTimestamp());
					newdevicedatalist.add(tmpData);
					
					// restore for next iteration
					iterateDateTime = DateUtils.toUnixTime(list.get(j)
							.getTimestamp()) + intervals;
					avgDouble = 0.0f;
					intervalnum = 0;
				}
			}

		} else {
			logger.info("no intevals  the intevals data is" + intervals);
			newdevicedatalist = list;
		}

		logger.info("new list size is ---------"+newdevicedatalist.size());
		return newdevicedatalist;

	}

	/* (non Javadoc) 
	* <p>Title: getDevicePeroidDataById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @param t
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#getDevicePeroidDataById(int, edu.itu.course.dropwizard.api.beans.QueryDeviceData) 
	*/ 
	@Override
	public Response getDevicePeroidDataById(int deviceId, QueryDeviceData t) {
		// TODO Auto-generated method stub

		int intervals = 0;
		Date starttime = null, endtime = null;
		/*
		 * if (t.has("intervals")) { intervals =
		 * Integer.parseInt(t.getString("intervals")); } if (t.has("starttime"))
		 * { starttime = getDateFromString(t.getString("starttime")); } if
		 * (t.has("endtime")) { endtime =
		 * getDateFromString(t.getString("endtime")); }
		
		 */
		
		intervals = t.getIntervals();
		starttime = getDateFromString(t.getStarttime());
		endtime = getDateFromString(t.getEndtime());

		logger.info("getDevicePeroidDataById--" + "deviceId" + deviceId
				+ "intervals" + intervals + "starttime" + t.getStarttime() + "endtime"
				+ t.getEndtime());
		// change data into right format!

		List<DeviceData> list = this.deviceDataDAO
				.getDevicePeriodDataByDeviceId(deviceId, starttime, endtime);// (deviceId);

		logger.info("list size is ---------"+list.size());
		return Response.status(200).entity(getAvg(list, intervals)).build();
	}

	/* (non Javadoc) 
	* <p>Title: queryDevicePeroidDataById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @param starttime
	* @param endtime
	* @param intervals
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#queryDevicePeroidDataById(int, java.lang.String, java.lang.String, java.lang.String) 
	*/ 
	@Override
	public Response queryDevicePeroidDataById(int deviceId, String starttime,
			String endtime, String intervals) {
		// change data into right format!

		List<DeviceData> list = this.deviceDataDAO
				.getDevicePeriodDataByDeviceId(deviceId,
						getDateFromString(starttime),
						getDateFromString(endtime));// (deviceId);

		return Response.status(200)
				.entity(getAvg(list, Integer.parseInt(intervals))).build();

	}

	/* (non Javadoc) 
	* <p>Title: relayDeviceById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @param t
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#relayDeviceById(int, org.codehaus.jettison.json.JSONObject) 
	*/ 
	@Override
	public String relayDeviceById(int deviceId, JSONObject t) {
		// TODO Auto-generated method stub

		XbeeUtil xbeeUtil;
		Future<String> future = null;
		final ExecutorService executor;
		Callable<String> asyncTask;

		String futureResult = "error";
		// using future to control the led
		try {
			int relayState = t.getInt("relayState");
			logger.info("using jsonobject relayState is " + relayState);
			xbeeUtil = XbeeUtil.getInstance();
			executor = (ExecutorService) SundialJobScheduler
					.getServletContext().getAttribute("ExecutorService");

			asyncTask = new Callable<String>() {
				@Override
				public String call() throws Exception {

					System.out.println("running here");

					// send command
					xbeeUtil.sendXbeeData(relayState > 0 ? XbeeEnum.RELAY_ON
							.getValue() : XbeeEnum.RELAY_OFF.getValue());
					// waiting for response
					String xbeeresponseString = xbeeUtil.receiveXbeeData(4000);

					String result = (relayState > 0) ? xbeeresponseString
							.equals(XbeeEnum.RELAY_ON_DONE.getValue()) ? "Success"
							: "Failed"
							: xbeeresponseString.equals(XbeeEnum.RELAY_ON_DONE
									.getValue()) ? "Success" : "Failed";

					logger.debug("relayState is %d xbee result is%s"
							+ relayState + result);

					return result;
				}
			};
			future = executor.submit(asyncTask);

			// can using timeout parameters
			futureResult = future.get();

			logger.info("future relayState is %d xbee result is%s" + relayState
					+ futureResult);

			return futureResult;
			// if the future fails will catch here
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.info("future  xbee result is%s" + futureResult);
			future.cancel(true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.info("future relay JSON format errors" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/* (non Javadoc) 
	* <p>Title: relayDeviceByParamAndId</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @param relayState
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#relayDeviceByParamAndId(int, int) 
	*/ 
	@Override
	public String relayDeviceByParamAndId(int deviceId, int relayState) {
		XbeeUtil xbeeUtil;
		Future<String> future = null;
		final ExecutorService executor;
		Callable<String> asyncTask;

		String futureResult = "error";
		// using future to control the led
		try {
			logger.info("using pathparam relayState is " + relayState);
			xbeeUtil = XbeeUtil.getInstance();
			executor = (ExecutorService) SundialJobScheduler
					.getServletContext().getAttribute("ExecutorService");

			// executor.wait();
			xbeeUtil.open();
			asyncTask = new Callable<String>() {
				@Override
				public String call() throws Exception {

					logger.debug("coming inside the relay");

					try {
						
					
						// send command
						String result = xbeeUtil
								.sendXbeeData(relayState > 0 ? XbeeEnum.RELAY_ON
										.getValue() : XbeeEnum.RELAY_OFF.getValue());
						// waiting for response
						// String xbeeresponseString = xbeeUtil.receiveXbeeData();
	
						// String result = (relayState > 0) ?
						// xbeeresponseString.equals(XbeeEnum.RELAY_ON_DONE.getValue())
						// ? "Success" : "Failed" :
						// xbeeresponseString.equals(XbeeEnum.RELAY_ON_DONE.getValue())
						// ? "Success" : "Failed";
	
						logger.debug("1111111111111111111relayState is %d xbee result is%s"
								+ relayState + result);
						
						deviceDAO.updateDeviceStatusbyId(deviceId, relayState);

					return result;
					} catch (Exception e) {
						// TODO: handle exception
						return e.getMessage();
					}
				}
			};
			future = executor.submit(asyncTask);

			// can using timeout parameters
			// waiting for 5 seconds
			futureResult = future.get(5000, TimeUnit.SECONDS);

			logger.info("future relayState is %d xbee result is%s" + relayState
					+ futureResult);

			return futureResult;
			// if the future fails will catch here
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.info("future  xbee result is%s" + futureResult
					+ "\n relay error here---" + e.getMessage());
			future.cancel(true);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			logger.info("future timeout result is %s" + futureResult
					+ "\n relay error here---" + e.getMessage());
			e.printStackTrace();
		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Insert device data by device id.
	 *
	 * @param deviceData the device data
	 */
	public void insertDeviceDataByDeviceId(DeviceData deviceData) {

		this.deviceDataDAO.insertDeviceDataById(deviceData);
	}

	/* (non Javadoc) 
	* <p>Title: postDevicePeroidDataById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @param t
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#postDevicePeroidDataById(int, edu.itu.course.dropwizard.api.beans.QueryDeviceData) 
	*/ 
	@Override
	public Response postDevicePeroidDataById(int deviceId, QueryDeviceData t) {
		logger.info("test string is ---"+t.toString());
		// TODO Auto-generated method stub

		int intervals = 0;
		Date starttime = null, endtime = null;
		/*
		 * if (t.has("intervals")) { intervals =
		 * Integer.parseInt(t.getString("intervals")); } if (t.has("starttime"))
		 * { starttime = getDateFromString(t.getString("starttime")); } if
		 * (t.has("endtime")) { endtime =
		 * getDateFromString(t.getString("endtime")); }
		
		 */
		
		intervals = t.getIntervals();
		starttime = getDateFromString(t.getStarttime());
		endtime = getDateFromString(t.getEndtime());

		logger.info("getDevicePeroidDataById--" + "deviceId" + deviceId
				+ "intervals" + intervals + "starttime" + t.getStarttime() + "endtime"
				+ t.getEndtime());
		// change data into right format!

		List<DeviceData> list = this.deviceDataDAO
				.getDevicePeriodDataByDeviceId(deviceId, starttime, endtime);// (deviceId);

		logger.info("list size is ---------"+list.size());
		return Response.status(200).entity(getAvg(list, intervals)).build();
	}

	

}
