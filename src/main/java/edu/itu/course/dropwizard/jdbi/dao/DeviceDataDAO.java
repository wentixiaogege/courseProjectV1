package edu.itu.course.dropwizard.jdbi.dao;

import java.util.ArrayList;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.api.beans.DeviceData;
import edu.itu.course.dropwizard.jdbi.mappers.DeviceDataMapper;

@RegisterMapper(DeviceDataMapper.class)
public interface DeviceDataDAO {
	 // NOTE: User is a reserved keyword in Derby
    public static final String DEVICE_DATA_TABLE = "device_data";

    @SqlUpdate("create table " + DEVICE_DATA_TABLE + " (id int(11), device_id int(11), data float, timestamp TIMESTAMP)")
    void createDeviceDataTable();

    @SqlUpdate("insert into " + DEVICE_DATA_TABLE + " (id, device_id, data , timestamp) values (:id, :device_id, :data,:timestamp)")
    void insert(@BindBean final Device device);

    @SqlQuery("select * from " + DEVICE_DATA_TABLE + " where id = :id")
    DeviceData getDeviceDataById(@Bind("id") int id);
    
    @SqlQuery("select * from " + DEVICE_DATA_TABLE + " where device_id = :id")
    List<DeviceData> getDeviceDataByDeviceId(@Bind("id") int id);
    

    @SqlUpdate("delete from " + DEVICE_DATA_TABLE + " where id = :it")
    void removeDevice(@Bind int deviceId);

    @SqlUpdate("update " + DEVICE_DATA_TABLE + "set name = :name  where id = :it ")
	Device updateDevicebyId(String deviceId);
}
