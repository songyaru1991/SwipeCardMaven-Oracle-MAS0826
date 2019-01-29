package com.swipecard.model;

import java.sql.Date;
import java.sql.Timestamp;

/*
 * For getYesdayShiftByEmpId  getCurShiftByEmpId
 * */
public class EmpShiftInfos {
	private String Id;
	private Date emp_date;
	private String class_no;
	private String class_desc;
	private String Shift;
	private Timestamp class_start;
	private Timestamp class_end;
	private int shiftDay;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}

	public String getShift() {
		return Shift;
	}
	public void setShift(String shift) {
		Shift = shift;
	}
	
	public int getShiftDay() {
		return shiftDay;
	}
	public void setShiftDay(int shiftDay) {
		this.shiftDay = shiftDay;
	}
	public Date getEmp_date() {
		return emp_date;
	}
	public void setEmp_date(Date emp_date) {
		this.emp_date = emp_date;
	}
	public String getClass_desc() {
		return class_desc;
	}
	public void setClass_desc(String class_desc) {
		this.class_desc = class_desc;
	}
	public Timestamp getClass_start() {
		return class_start;
	}
	public void setClass_start(Timestamp class_start) {
		this.class_start = class_start;
	}
	public Timestamp getClass_end() {
		return class_end;
	}
	public void setClass_end(Timestamp class_end) {
		this.class_end = class_end;
	}
	public String getClass_no() {
		return class_no;
	}
	public void setClass_no(String class_no) {
		this.class_no = class_no;
	}
}
