package edu.itu.course.dropwizard.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import edu.itu.course.dropwizard.api.DeviceResource;
import edu.itu.course.dropwizard.api.beans.Device;

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

	public Device getDeviceDataById(int deviceId) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
