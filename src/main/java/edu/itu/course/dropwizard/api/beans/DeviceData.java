package edu.itu.course.dropwizard.api.beans;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceData {

	private int id;
    private int deviceId;
    private float data;
    private Date timestamp;
    
	public DeviceData(int id, int deviceId, float data, Timestamp timestamp) {
		super();
		this.id = id;
		this.deviceId = deviceId;
		this.data = data;
		this.timestamp = timestamp;
	}
	public int getId() {
		return id;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public float getData() {
		return data;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceData [id=" + id + ", deviceId=" + deviceId + ", data="
				+ data + ", timestamp=" + timestamp + "]";
	}
    
}
