package edu.itu.course.dropwizard.jdbi.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import edu.itu.course.dropwizard.api.beans.DeviceData;

public class DeviceDataMapper implements ResultSetMapper<DeviceData> {

	 public DeviceData map(int index, ResultSet r, StatementContext ctx) throws SQLException {
	        return new DeviceData(r.getInt("id"), r.getInt("device_id"), r.getFloat("data"),r.getTimestamp("timestamp"));
	    }
}
