package edu.itu.course.dropwizard.jdbi.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.itu.course.dropwizard.api.beans.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceMapper.
 */
public class DeviceMapper implements ResultSetMapper<Device>  {

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
  	public Device map(int index, ResultSet r, StatementContext ctx) throws SQLException {
	        return new Device(r.getInt("id"), r.getString("name"), r.getInt("status"),r.getString("dataType"));
	    }
}
