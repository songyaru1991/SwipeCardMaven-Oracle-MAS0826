package com.swipecard.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import com.swipecard.SwipeCardNoDB;

/**
* @author yaru Song
* @version 创建时间：2019年1月10日 上午11:15:32
* @ClassName 类名称：
* @Description 类描述：properties文件获取工具类
*/
public class PropertyUtil {
	private static Logger logger = Logger.getLogger(PropertyUtil.class);
	 private static Properties props;
	    static{
	        loadProps();
	    }
	 
	    synchronized static private void loadProps(){
	        props = new Properties();
	        InputStream in = null;
	        try {
	        	//第一种，通过类加载器进行获取properties文件流
	        	//in = PropertyUtil.class.getClassLoader().getResourceAsStream("sysConfig.properties");
	            //第二种，通过类进行获取properties文件流
	            in =Resources.getResourceAsStream("db.properties");
	            props.load(in);
	        } catch (FileNotFoundException e) {
	            logger.error("db.properties文件未找到");
	        } catch (IOException e) {
	            logger.error("出现IOException");
	        } finally {
	            try {
	                if(null != in) {
	                    in.close();
	                }
	            } catch (IOException e) {
	                logger.error("sysConfig.properties文件流关闭出现异常");
	            }
	        }
	        logger.info("加载properties文件内容完成...........");
	      //  logger.info("properties文件内容：" + props);
	    }
	 
	    public static String getProperty(String key){
	        if(null == props) {
	            loadProps();
	        }
	        return props.getProperty(key);
	    }
	 
	    public static String getProperty(String key, String defaultValue) {
	        if(null == props) {
	            loadProps();
	        }
	        return props.getProperty(key, defaultValue);
	    }
}
