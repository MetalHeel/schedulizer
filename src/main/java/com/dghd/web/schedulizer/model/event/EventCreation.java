package com.dghd.web.schedulizer.model.event;

import java.util.Date;

public class EventCreation {
	private String eventName;
	private String description;
	private String bannerImage;
	private String posterImage;
	private String location;
	private Date startTime;
	private Date endTime;
	
	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getBannerImage() {
		return bannerImage;
	}
	
	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}
	
	public String getPosterImage() {
		return posterImage;
	}
	
	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
