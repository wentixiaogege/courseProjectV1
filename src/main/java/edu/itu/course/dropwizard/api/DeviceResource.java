package edu.itu.course.dropwizard.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    
    @GET
    @Path("/{deviceId}/temp")
    @Produces(MediaType.APPLICATION_JSON)
    public Device getDeviceDataById(@PathParam("deviceId") final int deviceId);

}