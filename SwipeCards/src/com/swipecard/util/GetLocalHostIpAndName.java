package com.swipecard.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.log4j.Logger;

import com.swipecard.SwipeCardNoDB;

public class GetLocalHostIpAndName {	
	     /* 获取本机的IP 
	     * @return Ip地址 
	     */ 
	  private static Logger logger = Logger.getLogger(GetLocalHostIpAndName.class);
	  static JsonFileUtil jsonFileUtil = new JsonFileUtil();
	  static String defaultWorkshopNo = jsonFileUtil.getSaveWorkshopNo();
		
	     public static String getLocalHostIP() { 
	          String ip=""; 
	          try { 
	               /**返回本地主机。*/ 
	               InetAddress addr = InetAddress.getLocalHost(); 
	               ip = addr.getHostAddress();  
	          } catch(Exception ex) { 
	        	    logger.error("获取本机的IP 異常,原因:"+ex);
					SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
					throw ExceptionFactory.wrapException("获取本机的IP 異常,原因:" + ex, ex);
	          } 
	            
	          return ip; 
	     } 
	       
	     /** 
	      * 获取本机主机名 
	      * @return 主机名
	      */ 
	     public static String getLocalHostName() { 
	          String hostName=""; 
	          try { 
	               InetAddress addr = InetAddress.getLocalHost(); 
	               hostName = addr.getHostName(); 
	          }catch(Exception ex){ 
	        	  logger.error("获取本机的主机名 異常,原因:"+ex);
				 SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
				 throw ExceptionFactory.wrapException("获取本机的主机名 異常,原因:" + ex, ex);
	          } 
	            
	          return hostName; 
	     } 
	     
	     public static String getLocalIp() {
	 		// TODO Auto-generated method stub
	 				Enumeration allNetInterfaces = null;
	 				try {
	 					allNetInterfaces = NetworkInterface.getNetworkInterfaces();
	 				} catch (SocketException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}
	 				InetAddress ip = null;
	 				String ipv4 = "";
	 				while (allNetInterfaces.hasMoreElements()) {
	 					NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//	 					System.out.println(netInterface.getName());
	 					Enumeration addresses = netInterface.getInetAddresses();
	 					while (addresses.hasMoreElements()) {
	 						ip = (InetAddress) addresses.nextElement();
	 						if (ip != null && ip instanceof Inet4Address) {
	 							if(ip.getHostAddress().equals("127.0.0.1")){  
	 		                        continue;  
	 		                    }
	 							ipv4 += ip.getHostAddress()+"/";
	 						}
	 					}
	 				}
	 				return ipv4;
	 	}
	     
	     public static void main(String[] args) {
	 		// TODO Auto-generated method stub
	 		 System.out.println("IP：" + getLocalHostIP()); 
	          System.out.println("NAME：" + getLocalHostName());
	          System.out.println("IPV4:" + getLocalIp());
	 	}
	 	

}
