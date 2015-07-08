package edu.itu.course.CourseProjectV1;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Authentication.User;
import org.junit.ClassRule;

import edu.itu.course.dropwizard.MyApplication;
import edu.itu.course.dropwizard.MyApplicationConfiguration;
import edu.itu.course.dropwizard.api.beans.Device;

public class TestDevice {
	Client client;
    @ClassRule
    public static final DropwizardAppRule<MyApplicationConfiguration> RULE = new DropwizardAppRule<>(MyApplication.class, "config.yml");

    private final Device sampleDevice = new Device(11, "test Device", 0 , "lightData");

    @org.junit.Before
    public void setUp() throws Exception {
    	client = ClientBuilder.newClient();

    }

    @org.junit.After
    public void tearDown() throws Exception {

    	client.close();
    }

    @org.junit.Test
    public void testAddUser() throws Exception {
     
        // add a user
        final int localPort = RULE.getLocalPort();
        Response response = client.target(String.format("http://localhost:%d/devices", localPort))
        		                   .request()
        		                   .put(Entity.entity(sampleDevice, MediaType.APPLICATION_JSON_TYPE));
        		                  
        						  
       //for jersey client 1.18.1
       /* ClientResponse response = client.resource(
                String.format("http://localhost:%d/user", localPort)).type(MediaType.APPLICATION_JSON_TYPE)
                .put(ClientResponse.class, sampleDevice);*/

        // make sure server returns the proper status code
        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));

        // now lets try to retrieve it
        response =
        		 client.target(String.format("http://localhost:%d/devices/%s", localPort, sampleDevice.getId()))
        		 .request()
                 .accept(MediaType.APPLICATION_JSON).get();
//                 .get(User.class);
        		//jersey client 1.18.1
               /* client.resource(String.format("http://localhost:%d/user/%s", localPort, sampleDevice.getId()))
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .get(ClientResponse.class);*/

        // first check the server response code
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        // validate the returned user vs the inserted one
        final Device device = response.readEntity(Device.class);
        assertEquals(sampleDevice.getId(), device.getId());
        assertEquals(sampleDevice.getName(), device.getName());
        assertEquals(sampleDevice.getStatus(), device.getStatus());
        assertEquals(sampleDevice.getDataType(), device.getDataType());

        // now lets delete the user.
        response =
        		client.target(String.format("http://localhost:%d/devices/%s", localPort, sampleDevice.getId()))
       		    .request().delete();
//                .accept(MediaType.APPLICATION_JSON_TYPE).get();
        		//jersey version 1.18.1
                /*client.resource(String.format("http://localhost:%d/devices/%s", localPort, sampleDevice.getId()))
                        .delete(ClientResponse.class);*/

        // check the server response code
        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));

        // lets try to retrieve it again
        response =
        		 client.target(String.format("http://localhost:%d/devices/%s", localPort, sampleDevice.getId()))
        		 .request()
                 .accept(MediaType.APPLICATION_JSON_TYPE).get();
        		//jersey version 1.18.1
               /* client.resource(String.format("http://localhost:%d/devices/%s", localPort, sampleDevice.getId()))
                        .get(ClientResponse.class);*/

        // we shouldn't have gotten anything
        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));

    }
}
