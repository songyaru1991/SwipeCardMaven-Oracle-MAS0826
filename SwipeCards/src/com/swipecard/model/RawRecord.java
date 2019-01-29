package com.swipecard.model;

import java.util.Date;

public class RawRecord {
	private String Id;
	private String CardID;
	private Date SwipeCardTime;
	private String Record_Status;
	private String swipeCardHostIp;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getCardID() {
		return CardID;
	}
	public void setCardID(String cardID) {
		CardID = cardID;
	}
	public Date getSwipeCardTime() {
		return SwipeCardTime;
	}
	public void setSwipeCardTime(Date swipeCardTime2) {
		SwipeCardTime = swipeCardTime2;
	}
	public String getRecord_Status() {
		return Record_Status;
	}
	public void setRecord_Status(String record_Status) {
		Record_Status = record_Status;
	}
	public String getSwipeCardHostIp() {
		return swipeCardHostIp;
	}
	public void setSwipeCardHostIp(String swipeCardHostIp) {
		this.swipeCardHostIp = swipeCardHostIp;
	}
}
