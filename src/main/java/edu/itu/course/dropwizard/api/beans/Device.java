package edu.itu.course.dropwizard.api.beans;

import java.util.ArrayList;
import java.util.List;

public class Device {
	private int id;
    private String name;
    private int status;
    private String dataType;
	private List<DeviceData> deviceDatas;
    

	/**
	 * @param deviceDatas the deviceDatas to set
	 */
	public void setDeviceDatas(List<DeviceData> deviceDatas) {
		this.deviceDatas = deviceDatas;
	}

	public static final Device EMPTY_DEVICE = new Device(Integer.MIN_VALUE, "", Integer.MIN_VALUE,"");

    public Device() {
    }

    public Device(int id, String name, int status, String type) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.dataType = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public int getStatus() {
		return status;
	}

	
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	public List<DeviceData> getDeviceDatas() {
			return deviceDatas;
		}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (dataType != null ? !dataType.equals(device.dataType) : device.dataType != null) return false;
        if (status != Integer.MIN_VALUE ? !(status == device.status) : device.status != Integer.MIN_VALUE) return false;
        if (id != Integer.MIN_VALUE ? !(id == device.id) : device.id != Integer.MIN_VALUE) return false;
        if (name != null ? !name.equals(device.name) : device.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != Integer.MIN_VALUE ? String.valueOf(id).hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != Integer.MIN_VALUE ? String.valueOf(status).hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [id=" + id + ", name=" + name + ", status=" + status
				+ ", type=" + dataType + ", deviceDatas=" + deviceDatas + "]";
	}
    
    
}
