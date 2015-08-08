package edu.itu.course.dropwizard.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import edu.itu.course.dropwizard.api.DeviceResource;
import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.QueryDeviceData;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceResourceClient.
 */
public class DeviceResourceClient implements DeviceResource{

	/** The client. */
	private final Client client = ClientBuilder.newClient();
	
	/** The resource url. */
	private final String resourceUrl;
	
	
	/**
	 * Instantiates a new device resource client.
	 *
	 * @param resourceUrl the resource url
	 */
	public DeviceResourceClient(String resourceUrl) {
		super();
		this.resourceUrl = resourceUrl;
	}

	/* (non Javadoc) 
	* <p>Title: addDevice</p> 
	* <p>Description: </p> 
	* @param device 
	* @see edu.itu.course.dropwizard.api.DeviceResource#addDevice(edu.itu.course.dropwizard.api.beans.Device) 
	*/ 
	public void addDevice(Device device) {
		// TODO Auto-generated method stub
		client.target(resourceUrl)
    	.request()
    	.put(Entity.entity(device, MediaType.APPLICATION_JSON_TYPE));
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
		Response clientResponse = client.target(resourceUrl + "/" + deviceId)
    			.request()
    			.accept(MediaType.APPLICATION_JSON_TYPE)
    			.get();
    	return clientResponse.readEntity(Device.class);
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
		return null;
	}

	/* (non Javadoc) 
	* <p>Title: removeDevice</p> 
	* <p>Description: </p> 
	* @param deviceId 
	* @see edu.itu.course.dropwizard.api.DeviceResource#removeDevice(int) 
	*/ 
	public void removeDevice(int deviceId) {
		// TODO Auto-generated method stub
		client.target(resourceUrl + "/" + deviceId)
    	.request()
    	.accept(MediaType.APPLICATION_JSON_TYPE)
    	.delete();
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
		return null;
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
		return null;
	}

	/* (non Javadoc) 
	* <p>Title: queryDevicePeroidDataById</p> 
	* <p>Description: </p> 
	* @param deviceId
	* @param starttime
	* @param endtime
	* @param intevals
	* @return 
	* @see edu.itu.course.dropwizard.api.DeviceResource#queryDevicePeroidDataById(int, java.lang.String, java.lang.String, java.lang.String) 
	*/ 
	@Override
	public Response queryDevicePeroidDataById(int deviceId, String starttime, String endtime, String intevals) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
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
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}


	

}
