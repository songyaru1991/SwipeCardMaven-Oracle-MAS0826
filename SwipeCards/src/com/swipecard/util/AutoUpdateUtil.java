package com.swipecard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JOptionPane;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
/**  
* 说明： CheckCurrentVersion时若有最新版本，关闭程式后自动从服务器下载最新版刷卡程式，并自动重新启动程式，打包为AutoUpdateUtil.jar
*  
* @author  
* @version 创建时间：2018-11-28
*/   
public class AutoUpdateUtil {
	private static Logger logger = Logger.getLogger(AutoUpdateUtil.class);
	public static void main(String[] args) {
		try {
			Thread.sleep(2000);//2S
		    String serverIP=PropertyUtil.getProperty("serverIP");
		    String serverUserName=PropertyUtil.getProperty("serverUserName");
		    String serverPassword=PropertyUtil.getProperty("serverPassword");
			boolean R = downFile(serverIP, serverUserName, serverPassword, "", "SwipeCard.jar", "D:/SwipeCard");
			if (R) {
					Process localProcess = Runtime.getRuntime().exec("java -jar D:/SwipeCard/SwipeCard.jar");

			} else {
				System.out.println("n");
				int i = JOptionPane.showConfirmDialog(null, "更新程序失败，请重新打开程序或者联系管理员", "更新程序警告", -1);
			}

		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("AutoUpdateUtil.class出现InterruptedException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 logger.error("AutoUpdateUtil.class出现IOException");
		}
		System.exit(0);
	}
	  
	  public static boolean downFile(String url, String username, String password, String remotePath, String fileName, String localPath)
	  {
	    boolean success = false;
	    FTPClient ftp = new FTPClient();
	    try
	    {
	      File file = new File(localPath);
	      if (!file.exists()) {
	        file.mkdirs();
	      }
	      ftp.connect(url, 21);
	      
	      ftp.login(username, password);
	      int reply = ftp.getReplyCode();
	      if (!FTPReply.isPositiveCompletion(reply))
	      {
	        ftp.disconnect();
	        boolean bool1 = success;return bool1;
	      }
	      ftp.changeWorkingDirectory(remotePath);
	      FTPFile[] fs = ftp.listFiles();
	      for (FTPFile ff : fs)
	      {
	        if (ff.getName().equals(fileName))
	        {
	          File localFile = new File(localPath + "/" + ff.getName());
	          OutputStream is = new FileOutputStream(localFile);
	          ftp.retrieveFile(ff.getName(), is);
	         // System.out.println(ff.getName() + fileName);
	          is.close();
	          success = true;
	        }
	      }
	      ftp.logout();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	      if (ftp.isConnected()) {
	        try
	        {
	          ftp.disconnect();
	        }
	        catch (IOException localIOException2) {}
	      }
	    }
	    finally
	    {
	      if (ftp.isConnected()) {
	        try
	        {
	          ftp.disconnect();
	        }
	        catch (IOException localIOException3) {}
	      }
	    }
	    return success;
	  }

}
