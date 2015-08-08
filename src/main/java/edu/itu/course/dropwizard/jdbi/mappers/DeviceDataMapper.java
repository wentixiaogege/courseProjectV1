package edu.itu.course.dropwizard.jdbi.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.itu.course.dropwizard.api.beans.DeviceData;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceDataMapper.
 */
public class DeviceDataMapper implements ResultSetMapper<DeviceData> {

	 /* (non Javadoc) 
 	* <p>Title: map</p> 
 	* <p>Description: </p> 
 	* @param index
 	* @param r
 	* @param ctx
 	* @return
 	* @throws SQLException 
 	* @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext) 
 	*/ 
 	public DeviceData map(int index, ResultSet r, StatementContext ctx) throws SQLException {
	        return new DeviceData(r.getInt("id"), r.getInt("device_id"), r.getFloat("data"),r.getTimestamp("timestamp"));
	    }
}
