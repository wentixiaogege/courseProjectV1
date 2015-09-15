package edu.itu.course.dropwizard.jdbi.dao;


import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import edu.itu.course.dropwizard.api.beans.Device;
import edu.itu.course.dropwizard.jdbi.mappers.DeviceMapper;

// TODO: Auto-generated Javadoc
/**
 * The Interface DeviceDAO.
 */
@RegisterMapper(DeviceMapper.class)
public interface DeviceDAO {
	 
 	/** The Constant DEVICE_TABLE. */
 	// NOTE: User is a reserved keyword in Derby
    public static final String DEVICE_TABLE = "device";

    /**
     * Creates the device table.
     */
    @SqlUpdate("create table " + DEVICE_TABLE + " (id int(11) NOT NULL AUTO_INCREMENT, name varchar(100), status int(11), dataType varchar(100),PRIMARY KEY (id))")
    void createDeviceTable();

    /**
     * Insert.
     *
     * @param device the device
     */
    @SqlUpdate("insert into " + DEVICE_TABLE + " (id, name, status,dataType) values (:id, :name, :status, :dataType)")
    void insert(@BindBean final Device device);

    /**
     * Find device by id.
     *
     * @param id the id
     * @return the device
     */
    @SqlQuery("select * from " + DEVICE_TABLE + " where id = :id")
    Device findDeviceById(@Bind("id") int id);
    
    /**
     * Find devices.
     *
     * @return the device
     */
    @SqlQuery("select * from " + DEVICE_TABLE )
    List<Device> findDevices();

    /**
     * Removes the device.
     *
     * @param deviceId the device id
     */
    @SqlUpdate("delete from " + DEVICE_TABLE + " where id = :it")
    void removeDevice(@Bind int deviceId);

    /**
     * Update deviceby id.
     *
     * @param deviceId the device id
     * @return the device
     */
    //consider later maybe
    @SqlUpdate("update " + DEVICE_TABLE + " set name = :name  where id = :deviceId ")
	Device updateDevicebyId(int deviceId);
    
    /**
     * Update device statusby id.
     *
     * @param deviceId the device id
     * @param status the status
     */
    //consider later maybe
    @SqlUpdate("update " + DEVICE_TABLE + " set status = :status  where id = :deviceId ")
	void updateDeviceStatusbyId(@Bind("deviceId")int deviceId,@Bind("status")int status);
}
