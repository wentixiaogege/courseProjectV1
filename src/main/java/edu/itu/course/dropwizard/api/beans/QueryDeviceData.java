package edu.itu.course.dropwizard.api.beans;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class QueryDeviceData {

	
	public int getIntervals() {
		return intervals;
	}
	public void setIntervals(int intervals) {
		this.intervals = intervals;
	}
	
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	@JsonProperty("intervals")
	int intervals;
	@JsonProperty("starttime")
	String starttime;
	@JsonProperty("endtime")
	String endtime;
	
}
