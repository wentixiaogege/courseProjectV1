package edu.itu.course.dropwizard.api.beans;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;


// TODO: Auto-generated Javadoc
/**
 * The Class QueryDeviceData.
 */
@XmlRootElement
public class QueryDeviceData {

	
	/**
	 * Gets the intervals.
	 *
	 * @return the intervals
	 */
	public int getIntervals() {
		return intervals;
	}
	
	/**
	 * Sets the intervals.
	 *
	 * @param intervals the new intervals
	 */
	public void setIntervals(int intervals) {
		this.intervals = intervals;
	}
	
	/**
	 * Gets the starttime.
	 *
	 * @return the starttime
	 */
	public String getStarttime() {
		return starttime;
	}
	
	/* (non Javadoc) 
	* <p>Title: toString</p> 
	* <p>Description: </p> 
	* @return 
	* @see java.lang.Object#toString() 
	*/ 
	@Override
	public String toString() {
		return "QueryDeviceData [intervals=" + intervals + ", starttime="
				+ starttime + ", endtime=" + endtime + "]";
	}
	
	/**
	 * Sets the starttime.
	 *
	 * @param starttime the new starttime
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	/**
	 * Gets the endtime.
	 *
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}
	
	/**
	 * Sets the endtime.
	 *
	 * @param endtime the new endtime
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	/** The intervals. */
	@JsonProperty("intervals")
	int intervals;
	
	/** The starttime. */
	@JsonProperty("starttime")
	String starttime;
	
	/** The endtime. */
	@JsonProperty("endtime")
	String endtime;
	
}
