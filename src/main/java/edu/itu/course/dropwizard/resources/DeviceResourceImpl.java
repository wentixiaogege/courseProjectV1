package edu.itu.course.dropwizard.resources;

import java.util.List;

import javax.ws.rs.core.Response;

import edu.itu.course.dropwizard.api.DeviceResource;
import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDAO;
import edu.itu.course.dropwizard.jdbi.dao.DeviceDataDAO;

public class DeviceResourceImpl implements DeviceResource {

	private final DeviceDAO deviceDAO;
	private final DeviceDataDAO deviceDataDAO;


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
		Device getDevice = this.deviceDAO.findDeviceById(deviceId);
		
		getDevice.setDeviceDatas(this.deviceDataDAO.getDeviceDataByDeviceId(deviceId));
		
		System.out.println("returned data is "+getDevice);
//		return getDevice;
		List<DeviceData> list = this.deviceDataDAO.getDeviceDataByDeviceId(deviceId); 
//		return this.deviceDataDAO.getDeviceDataByDeviceId(deviceId);
		return Response.status(200).entity(list).build();
	}
}
