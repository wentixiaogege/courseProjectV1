package edu.itu.course.dropwizard.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.itu.course.dropwizard.MyApplication;
import edu.itu.course.dropwizard.api.DeviceResource;
import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDAO;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDataDAO;

public class DeviceResourceImpl implements DeviceResource {

	private final DeviceDAO deviceDAO;
	private final DeviceDataDAO deviceDataDAO;

	private static Logger logger = LoggerFactory.getLogger(DeviceResourceImpl.class);
	public DeviceResourceImpl(DeviceDAO deviceDAO, DeviceDataDAO deviceDataDAO) {
		super();
		this.deviceDAO = deviceDAO;
		this.deviceDataDAO = deviceDataDAO;
	}

	public void addDevice(Device device) {
		// TODO Auto-generated method stub
		this.deviceDAO.insert(device);
	}

	public Device getDeviceById(int deviceId) {
		// TODO Auto-generated method stub
		return this.deviceDAO.findDeviceById(deviceId);
	}

	public Device updateDeviceById(int deviceId) {
		// TODO Auto-generated method stub
		return this.deviceDAO.updateDevicebyId(deviceId);
	}

	public void removeDevice(int deviceId) {
		// TODO Auto-generated method stub
		this.deviceDAO.removeDevice(deviceId);
	}

	public Response getDeviceDataById(int deviceId) {
		// TODO Auto-generated method stub

		List<DeviceData> list = this.deviceDataDAO.getDeviceDataByDeviceId(deviceId);
		return Response.status(200).entity(list).build();
	}

	private Date getDateFromString(String dateString) {
	    try {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        Date date = df.parse(dateString);
	        return date;
	    } catch (ParseException e) {
	        //WebApplicationException ...("Date format should be yyyy-MM-dd'T'HH:mm:ss", Status.BAD_REQUEST);
	      e.printStackTrace();
	    }
		return null;
	}
	private List<DeviceData> getAvg(List<DeviceData> list,int intervals) {
		
		List<DeviceData> newdevicedatalist = new ArrayList<DeviceData>();
		
		int intervalnum = 0;
		float avgDouble =0.00f;
		if (intervals > 0 && list.size() > 0) {
			// for every device
				for (int j = 0; j < list.size(); j++) {

					long iterateDateTime = DateUtils.toUnixTime(list.get(j).getTimestamp())+ intervals;
					avgDouble =  0.0f;
					intervalnum = 0;
					
					if (DateUtils.toUnixTime(list.get(j).getTimestamp()) < iterateDateTime) {
						intervalnum ++;
						avgDouble+=list.get(j).getData();
					}else if (intervalnum > 0) 
							
						avgDouble /= intervalnum;
					    DeviceData tmpData = new DeviceData(list.get(j-1).getId(), list.get(j-1).getDeviceId(), avgDouble, list.get(j-1).getTimestamp());
					    newdevicedatalist.add(tmpData);
					}

		}else {
			logger.info("no intevals  the intevals data is" + intervals);
			newdevicedatalist = list;
		}
		
		
		return newdevicedatalist;
		
	}
	@Override
	public Response getDevicePeroidDataById(int deviceId, JSONObject t) {
		// TODO Auto-generated method stub

		int intervals = 0;
		Date starttime = null, endtime = null;
		try {
			if (t.has("intervals")) {
				intervals = Integer.parseInt(t.getString("intervals"));
			}
			if (t.has("starttime")) {
				starttime = getDateFromString(t.getString("starttime"));
			}
			if (t.has("endtime")) {
				endtime = getDateFromString(t.getString("endtime"));
			}
			
			//change data into right format!

			List<DeviceData> list = this.deviceDataDAO.getDevicePeriodDataByDeviceId(deviceId, starttime, endtime);//(deviceId);
			
			return Response.status(200).entity(getAvg(list,intervals)).build();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Response queryDevicePeroidDataById(int deviceId, String starttime, String endtime, String intervals) {
		//change data into right format!

		List<DeviceData> list = this.deviceDataDAO.getDevicePeriodDataByDeviceId(deviceId, getDateFromString(starttime), getDateFromString(endtime));//(deviceId);
		
		return Response.status(200).entity(getAvg(list,Integer.parseInt(intervals))).build();
		
	}

	@Override
	public Device relayDeviceById(int deviceId, JSONObject t) {
		// TODO Auto-generated method stub
		
		//using future to control the led 
		try {
			int relayState = t.getInt("relayState");
			
			logger.info("relayState is " + relayState);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Device relayDeviceByParamAndId(int deviceId, int relayState) {
		// TODO Auto-generated method stub
		//using future to control the led 
		logger.info("relayState is " + relayState);
		
		return null;
	}
}
