package edu.itu.course.dropwizard.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import edu.itu.course.dropwizard.api.DeviceResource;
import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.api.beans.QueryDeviceData;

public class DeviceResourceClient implements DeviceResource{

	private final Client client = ClientBuilder.newClient();
	private final String resourceUrl;
	
	
	public DeviceResourceClient(String resourceUrl) {
		super();
		this.resourceUrl = resourceUrl;
	}

	public void addDevice(Device device) {
		// TODO Auto-generated method stub
		client.target(resourceUrl)
    	.request()
    	.put(Entity.entity(device, MediaType.APPLICATION_JSON_TYPE));
	}

	public Device getDeviceById(int deviceId) {
		// TODO Auto-generated method stub
		Response clientResponse = client.target(resourceUrl + "/" + deviceId)
    			.request()
    			.accept(MediaType.APPLICATION_JSON_TYPE)
    			.get();
    	return clientResponse.readEntity(Device.class);
	}

	public Device updateDeviceById(int deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeDevice(int deviceId) {
		// TODO Auto-generated method stub
		client.target(resourceUrl + "/" + deviceId)
    	.request()
    	.accept(MediaType.APPLICATION_JSON_TYPE)
    	.delete();
	}

	public Response getDeviceDataById(int deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getDevicePeroidDataById(int deviceId, QueryDeviceData t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response queryDevicePeroidDataById(int deviceId, String starttime, String endtime, String intevals) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String relayDeviceByParamAndId(int deviceId, int relayState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String relayDeviceById(int deviceId, JSONObject t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postDevicePeroidDataById(int deviceId, JSONObject t) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
