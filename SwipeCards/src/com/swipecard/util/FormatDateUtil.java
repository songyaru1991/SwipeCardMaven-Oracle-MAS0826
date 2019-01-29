package com.swipecard.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDateUtil {
	public static String getStrTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String ti = df.format(new Date());
		return ti;
	}
	
	public static Date getDateTime() {
		Date nowTime = new Date();
		return nowTime;
	}

	public static String getCurDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String curDate  = df.format(new Date());
		return curDate;
	}
	
	public static String getHHMM(){
		SimpleDateFormat df = new SimpleDateFormat("-HH-mm");
		String ti  = df.format(new Date());
		return ti;
	}
	
	public static String getYesterdayDate(){
		Date nowTime = new Date();   //当前时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(nowTime);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天

		Date dBefore = calendar.getTime();   //得到前一天的时间

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String yesterdayDate = df.format(dBefore);    
		return yesterdayDate;
	}
	
	public static String changeTimeToStr(Date Time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ti = df.format(Time);
		return ti;
	}

}
