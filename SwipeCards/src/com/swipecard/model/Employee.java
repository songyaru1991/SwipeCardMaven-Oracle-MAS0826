package com.swipecard.model;

import java.sql.Date;

public class Employee {
	private String Id;
	private String Name;
	private String depid;
	private String depname;
	private String CardID;
	private String Direct;
	private String CostID;
	private int	Permission;
	private int isOnWork;
	private Date UpdateDate;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDepid() {
		return depid;
	}
	public void setDepid(String depid) {
		this.depid = depid;
	}
	public String getDepname() {
		return depname;
	}
	public void setDepname(String depname) {
		this.depname = depname;
	}
	public String getCardID() {
		return CardID;
	}
	public void setCardID(String cardID) {
		CardID = cardID;
	}
	public String getDirect() {
		return Direct;
	}
	public void setDirect(String direct) {
		Direct = direct;
	}
	public String getCostID() {
		return CostID;
	}
	public void setCostID(String costID) {
		CostID = costID;
	}
	public int getPermission() {
		return Permission;
	}
	public void setPermission(int permission) {
		Permission = permission;
	}
	public int getIsOnWork() {
		return isOnWork;
	}
	public void setIsOnWork(int isOnWork) {
		this.isOnWork = isOnWork;
	}
	public Date getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(Date updateDate) {
		UpdateDate = updateDate;
	}
	@Override
	public String toString() {
		return "Employee [Id=" + Id + ", Name=" + Name + ", depid=" + depid + ", depname=" + depname + ", CardID="
				+ CardID + ", Direct=" + Direct + ", CostID=" + CostID + ", Permission=" + Permission + ", isOnWork="
				+ isOnWork + ", UpdateDate=" + UpdateDate + "]";
	}
	
	
}
