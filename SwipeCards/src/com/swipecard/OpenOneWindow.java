package com.swipecard;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.swipecard.util.JsonFileUtil;

public class OpenOneWindow {
	private static Logger logger = Logger.getLogger(JsonFileUtil.class);
	private static FileLock lock;
	public static boolean checkLock(){
		String lockFilePath = "D:/swipeCard/logs/lock";
		FileOutputStream fo = null;
		try{
			File file = new File(lockFilePath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			fo = new FileOutputStream(file);
			lock = fo.getChannel().tryLock();
			if(lock == null){
				
				return false;
			}else{
				return true;
			}
		}catch(Exception e){
			logger.error("開啟單例線程時 Error building SqlSession，原因:"+e);
			JOptionPane.showConfirmDialog(null,
					"程序無法打開，請聯繫相關人員", "程序無法打開",
					JOptionPane.DEFAULT_OPTION);
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}
}
