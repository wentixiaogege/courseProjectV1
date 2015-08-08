package edu.itu.course.dropwizard.api.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 *  
 *
 * @author Jack Li E-mail:wentixiaogege@gmail.com
 * @ClassName: Device
 * @Description: TODO(functions of this type)
 * @date Aug 7, 2015 1:53:52 PM
 */ 
@XmlRootElement
public class Device {
	
	/** The id. */
	private int id;
    
    /** The name. */
    private String name;
    
    /** The status. */
    private int status;
    
    /** The data type. */
    private String dataType;
	
	/** The device datas. */
	private List<DeviceData> deviceDatas;
    

	

	/** The Constant EMPTY_DEVICE. */
	public static final Device EMPTY_DEVICE = new Device(Integer.MIN_VALUE, "", Integer.MIN_VALUE,"");

    /**
     *  
     * <p>Title: </p> 
     * <p>Description: </p>.
     */ 
    public Device() {
    }

    /**
     *  
     * <p>Title: </p> 
     * <p>Description: </p> .
     *
     * @param id the id
     * @param name the name
     * @param status the status
     * @param type the type
     */ 
    public Device(int id, String name, int status, String type) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.dataType = type;
    }

    /**
     *  
     *
     * @return int
     * @Title: getId
     * @Description: TODO(describe the functions of this method)
     */ 
    public int getId() {
        return id;
    }

    /**
     *  
     *
     * @return String
     * @Title: getName
     * @Description: TODO(describe the functions of this method)
     */ 
    public String getName() {
        return name;
    }
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
		return status;
	}

	
	/**
	 * Gets the data type.
	 *
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Gets the device datas.
	 *
	 * @return the device datas
	 */
	public List<DeviceData> getDeviceDatas() {
			return deviceDatas;
		}
	
	/**
	 * Sets the device datas.
	 *
	 * @param deviceDatas the deviceDatas to set
	 */
	public void setDeviceDatas(List<DeviceData> deviceDatas) {
		this.deviceDatas = deviceDatas;
	}
    
    /* (non Javadoc) 
    * <p>Title: equals</p> 
    * <p>Description: </p> 
    * @param o
    * @return 
    * @see java.lang.Object#equals(java.lang.Object) 
    */ 
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

    /* (non Javadoc) 
    * <p>Title: hashCode</p> 
    * <p>Description: </p> 
    * @return 
    * @see java.lang.Object#hashCode() 
    */ 
    @Override
    public int hashCode() {
        int result = id != Integer.MIN_VALUE ? String.valueOf(id).hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != Integer.MIN_VALUE ? String.valueOf(status).hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }

	/* (non Javadoc) 
	* <p>Title: toString</p> 
	* <p>Description: </p> 
	* @return 
	* @see java.lang.Object#toString() 
	*/ 
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [id=" + id + ", name=" + name + ", status=" + status
				+ ", type=" + dataType + ", deviceDatas=" + deviceDatas + "]";
	}
    
    
}
