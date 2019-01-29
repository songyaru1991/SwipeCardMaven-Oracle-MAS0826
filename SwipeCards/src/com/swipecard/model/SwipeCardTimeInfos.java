package com.swipecard.model;

import java.util.Date;

public class SwipeCardTimeInfos {
	
	private String EMP_ID;
	private String SWIPE_DATE;
	private Date SwipeCardTime;
	private Date SwipeCardTime2;
	private String CheckState;
	private String ProdLineCode;
	private String WorkshopNo;
	private String PRIMARY_ITEM_NO;
	private String RC_NO;
	private String Shift;
	private String CLASS_NO;
	
	public Date getSwipeCardTime() {
		return SwipeCardTime;
	}
	public void setSwipeCardTime(Date swipeCardTime3) {
		SwipeCardTime = swipeCardTime3;
	}
	
	public Date getSwipeCardTime2() {
		return SwipeCardTime2;
	}
	public void setSwipeCardTime2(Date swipeCardTime22) {
		SwipeCardTime2 = swipeCardTime22;
	}
	
	public String getCheckState() {
		return CheckState;
	}
	public void setCheckState(String checkState) {
		CheckState = checkState;
	}
	public String getProdLineCode() {
		return ProdLineCode;
	}
	public void setProdLineCode(String prodLineCode) {
		ProdLineCode = prodLineCode;
	}
	public String getWorkshopNo() {
		return WorkshopNo;
	}
	public void setWorkshopNo(String workshopNo) {
		WorkshopNo = workshopNo;
	}

	public String getShift() {
		return Shift;
	}
	public void setShift(String shift) {
		Shift = shift;
	}

	public String getEMP_ID() {
		return EMP_ID;
	}
	public void setEMP_ID(String eMP_ID) {
		EMP_ID = eMP_ID;
	}

	public String getPRIMARY_ITEM_NO() {
		return PRIMARY_ITEM_NO;
	}
	public void setPRIMARY_ITEM_NO(String pRIMARY_ITEM_NO) {
		PRIMARY_ITEM_NO = pRIMARY_ITEM_NO;
	}
	public String getRC_NO() {
		return RC_NO;
	}
	public void setRC_NO(String rC_NO) {
		RC_NO = rC_NO;
	}
	public String getCLASS_NO() {
		return CLASS_NO;
	}
	public void setCLASS_NO(String cLASS_NO) {
		CLASS_NO = cLASS_NO;
	}
	public String getSWIPE_DATE() {
		return SWIPE_DATE;
	}
	public void setSWIPE_DATE(String sWIPE_DATE) {
		SWIPE_DATE = sWIPE_DATE;
	}
	@Override
	public String toString() {
		return "SwipeCardTimeInfos [EMP_ID=" + EMP_ID + ", SWIPE_DATE=" + SWIPE_DATE + ", SwipeCardTime="
				+ SwipeCardTime + ", SwipeCardTime2=" + SwipeCardTime2 + ", CheckState=" + CheckState
				+ ", ProdLineCode=" + ProdLineCode + ", WorkshopNo=" + WorkshopNo + ", PRIMARY_ITEM_NO="
				+ PRIMARY_ITEM_NO + ", RC_NO=" + RC_NO + ", Shift=" + Shift + ", CLASS_NO=" + CLASS_NO + "]";
	}
	
	
}
