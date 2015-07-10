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

import edu.itu.course.dropwizard.api.beans.Device;

@Path("/devices")
public interface DeviceResource {
	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDevice(final Device device);

    @GET
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Device getDeviceById(@PathParam("deviceId") final int deviceId);
    
    @POST
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Device updateDeviceById(@PathParam("deviceId") final int deviceId);

    @DELETE
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeDevice(@PathParam("deviceId") final int deviceId);
    
    @POST
    @Path("/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Device relayDeviceById(@PathParam("deviceId") final int deviceId,JSONObject t);
    
    @POST
    @Path("/{deviceId}/{relayState}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Device relayDeviceByParamAndId(@PathParam("deviceId") final int deviceId,
    									  @PathParam("relayState") final int relayState);
    
    @GET
    @Path("/{deviceId}/all/temp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceDataById(@PathParam("deviceId") final int deviceId);
    
    @GET
    @Path("/{deviceId}/peroid/temp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDevicePeroidDataById(@PathParam("deviceId") final int deviceId,JSONObject t);

    @GET
    @Path("/{deviceId}/peroid/temp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryDevicePeroidDataById(@PathParam("deviceId") final int deviceId,
    										  @DefaultValue("2015-06-18 19:23:38")@QueryParam("starttime") String starttime,
    										  @DefaultValue("2016-06-18 19:23:38")@QueryParam("endtime") String endtime,
    										  @DefaultValue("3600")@QueryParam("endtime") String intevals);

    
}