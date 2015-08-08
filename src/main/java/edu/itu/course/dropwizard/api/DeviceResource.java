package edu.itu.course.dropwizard.api;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.QueryDeviceData;

/** 
* @ClassName: DeviceResource 
* @Description: TODO(functions of this type) 
* @author Jack Li E-mail:wentixiaogege@gmail.com
* @date Aug 7, 2015 1:52:59 PM 
*  
*/ 
@Path("/devices")
public interface DeviceResource {
	/** 
	* @Title: addDevice 
	* @Description: TODO(describe the functions of this method) 
	* @param @param device    
	* @return void    
	* @throws 
	*/ 
	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDevice(final Device device);
	
	/** 
	* @Title: getDevices 
	* @Description: TODO(describe the functions of this method) 
	* @param @return    
	* @return Response    
	* @throws 
	*/ 
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevices();
	    

    /** 
    * @Title: getDeviceById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @return    
    * @return Device    
    * @throws 
    */ 
    @GET
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Device getDeviceById(@PathParam("deviceId") final int deviceId);
    
    /** 
    * @Title: updateDeviceById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @return    
    * @return Device    
    * @throws 
    */ 
    @POST
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Device updateDeviceById(@PathParam("deviceId") final int deviceId);

    /** 
    * @Title: removeDevice 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId    
    * @return void    
    * @throws 
    */ 
    @DELETE
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeDevice(@PathParam("deviceId") final int deviceId);
    
    /** 
    * @Title: relayDeviceById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @param t
    * @param @return    
    * @return String    
    * @throws 
    */ 
    @POST
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String relayDeviceById(@PathParam("deviceId") final int deviceId,JSONObject t);
    
    /** 
    * @Title: relayDeviceByParamAndId 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @param relayState
    * @param @return    
    * @return String    
    * @throws 
    */ 
    @POST
    @Path("/{deviceId}/{relayState}")
    @Produces(MediaType.APPLICATION_JSON)
    public String relayDeviceByParamAndId(@PathParam("deviceId") final int deviceId,
    									  @PathParam("relayState") final int relayState);
    
    /** 
    * @Title: getDeviceDataById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @return    
    * @return Response    
    * @throws 
    */ 
    @GET
    @Path("/{deviceId}/all/temp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceDataById(@PathParam("deviceId") final int deviceId);
    
    /** 
    * @Title: getDevicePeroidDataById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @param t
    * @param @return    
    * @return Response    
    * @throws 
    */ 
    @GET
    @Path("/{deviceId}/peroid/temp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDevicePeroidDataById(@PathParam("deviceId") final int deviceId,QueryDeviceData t);
    
    /** 
    * @Title: postDevicePeroidDataById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @param t
    * @param @return    
    * @return Response    
    * @throws 
    */ 
    @POST
    @Path("/{deviceId}/peroid/temp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postDevicePeroidDataById(@PathParam("deviceId") final int deviceId,QueryDeviceData t);

    /** 
    * @Title: queryDevicePeroidDataById 
    * @Description: TODO(describe the functions of this method) 
    * @param @param deviceId
    * @param @param starttime
    * @param @param endtime
    * @param @param intevals
    * @param @return    
    * @return Response    
    * @throws 
    */ 
    @GET
    @Path("/{deviceId}/peroid/temp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryDevicePeroidDataById(@PathParam("deviceId") final int deviceId,
    										  @DefaultValue("2015-06-18 19:23:38")@QueryParam("starttime") String starttime,
    										  @DefaultValue("2016-06-18 19:23:38")@QueryParam("endtime") String endtime,
    										  @DefaultValue("3600")@QueryParam("endtime") String intevals);

    
}