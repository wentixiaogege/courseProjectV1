package edu.itu.course.dropwizard.jdbi.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.jdbi.mappers.DeviceMapper;

@RegisterMapper(DeviceMapper.class)
public interface DeviceDAO {
	 // NOTE: User is a reserved keyword in Derby
    public static final String DEVICE_TABLE = "device";

    @SqlUpdate("create table " + DEVICE_TABLE + " (id int(11), name varchar(100), status int(11), dataType varchar(100))")
    void createDeviceTable();

    @SqlUpdate("insert into " + DEVICE_TABLE + " (id, name, status,dataType) values (:id, :name, :status, :dataType)")
    void insert(@BindBean final Device device);

    @SqlQuery("select * from " + DEVICE_TABLE + " where id = :id")
    Device findDeviceById(@Bind("id") int id);
    
    @SqlQuery("select * from " + DEVICE_TABLE)
    Device findDevices();

    @SqlUpdate("delete from " + DEVICE_TABLE + " where id = :it")
    void removeDevice(@Bind int deviceId);

    //consider later maybe
    @SqlUpdate("update " + DEVICE_TABLE + "set name = :name  where id = :it ")
	Device updateDevicebyId(int deviceId);
}
